package com.ecommerceserver.service;

import com.ecommerceserver.model.ImageEntity;
import com.ecommerceserver.model.StorageEntity;
import com.ecommerceserver.repository.ImageRepository;
import com.ecommerceserver.repository.StorageRepository;
import com.ecommerceserver.service.dto.ImageDto;
import com.ecommerceserver.service.exception.BadImageSizeException;
import com.ecommerceserver.service.utils.RandomStringGenerator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("imageService")
public class ImageServiceImpl implements ImageService {

    private static final int PUBLIC_IMAGE_NAME_LENGTH = 12;
    public static final int MIN_IMAGE_HEIGHT = 350;
    public static final int MIN_IMAGE_WIDTH = 500;
    private static final String ORIGINAL_FILE_NAME = "orig.jpg";
    public static final int ORIGINAL_FILE_HEIGHT = 1500;
    private static final int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
    private static final ColorModel RGB_OPAQUE = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);
    private static final String PNG = "png";
    private Logger logger;
    @Resource(name = "imageRepository")
    private ImageRepository imageRepository;
    @Resource(name = "storageRepository")
    private StorageRepository storageRepository;
    @Resource(name = "conversionService")
    private ConversionService conversionService;

    @Value("${ecommerce.upload.directory}")
    private String uploadDirectoryPath;

    public ImageServiceImpl() {
        logger = LogManager.getLogger(ImageServiceImpl.class);
    }

    @PostConstruct
    public void init() {
        Path uploadPath = Paths.get(uploadDirectoryPath);
        if (!Files.exists(uploadPath) || !Files.isDirectory(uploadPath)) {
            if (!(new File(uploadDirectoryPath).mkdirs())) {
                logger.fatal("Upload directory path, described IN application.properties is not writable! Please, check is this folder exists: " + uploadDirectoryPath);
            }

        }
    }

    @Override
    public ImageDto saveImage(MultipartFile file) throws IOException, BadImageSizeException, InterruptedException {
        Path uploadPath = Paths.get(uploadDirectoryPath);
        if (Files.exists(uploadPath) && Files.isDirectory(uploadPath) && Files.isWritable(uploadPath)) {
            if (!checkDimensions(file)) {
                throw new BadImageSizeException("Size of this image is not valid!");
            }
            String directory = getDateFileName();
            String fileFolder = generateFileName(11);
            Path targetDirectory = Paths.get(uploadDirectoryPath, directory, fileFolder);
            if (!(new File(targetDirectory.toUri()).mkdirs())) {
                logger.fatal("Upload directory path, described IN application.properties is not writable! Please, check is this folder exists: " + uploadDirectoryPath);
                throw new IOException("Upload directory is not writable!");
            }
            String originalFileName = createReducedImage(uploadDirectoryPath, Paths.get(directory, fileFolder).toString(), file, ORIGINAL_FILE_NAME, ORIGINAL_FILE_HEIGHT);
            ImageEntity entity = saveImageInformation(Paths.get(directory, fileFolder).toString(), originalFileName);
            return conversionService.convert(entity, ImageDto.class);
        } else {
            logger.fatal("Upload directory path, described IN application.properties is not writable! Please, check is this folder exists: " + uploadDirectoryPath);
            throw new IOException("Upload directory is not writable!");
        }
    }

    @Override
    public boolean isImage(MultipartFile file) {
        String mimetype = file.getContentType();
        if (!mimetype.contains("/")) {
            return false;
        }
        String type = mimetype.split("/")[0];
        return type.equals("image");
    }

    @Override
    public org.springframework.core.io.Resource getFullImageAsResource(String fileName) {
        ImageEntity imageEntity = imageRepository.findOneByPublicFileName(fileName);
        File imageFile = Paths.get(imageEntity.getStorage().getPath(), imageEntity.getDirectory(), imageEntity.getOriginalFileName()).toFile();
        org.springframework.core.io.Resource resource = new FileSystemResource(imageFile);
        return resource;
    }

    @Override
    public List<ImageDto> getImages() {
        Iterable<ImageEntity> imageEntities = imageRepository.findAll();
        return StreamSupport.stream(imageEntities.spliterator(), false).map(entity -> conversionService.convert(entity, ImageDto.class)).collect(Collectors.toList());
    }

    public void deleteImage(String imageName) {
        ImageEntity imageEntity = imageRepository.findOneByPublicFileName(imageName);
        imageRepository.delete(imageEntity);
    }

    private ImageEntity saveImageInformation(String directory, String fileName) {
        ImageEntity imageEntity = new ImageEntity();
        StorageEntity storage = getOrCreateCurrentStorage();
        imageEntity.setStorage(storage);
        imageEntity.setDirectory(directory);
        imageEntity.setOriginalFileName(fileName);
        imageEntity.setPublicFileName(generateFileName(PUBLIC_IMAGE_NAME_LENGTH) + ".jpg");
        return imageRepository.save(imageEntity);
    }

    private StorageEntity getOrCreateCurrentStorage() {
        StorageEntity currentStorage = storageRepository.findOneByPath(uploadDirectoryPath);
        if (currentStorage == null) {
            StorageEntity newStorage = new StorageEntity();
            newStorage.setPath(uploadDirectoryPath);
            return storageRepository.save(newStorage);
        }
        return currentStorage;
    }

    private String generateFileName(int length) {
        RandomStringGenerator stringGenerator = new RandomStringGenerator.RandomStringGeneratorBuilder().useLower(true).useUpper(true).useDigits(true).build();
        return stringGenerator.generate(length);
    }

    private String getDateFileName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(new Date());
    }

    private boolean checkDimensions(MultipartFile file) {
        BufferedImage sourceImage = null;
        try {
            sourceImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
        } catch (IOException e) {
            logger.error("ImageService could not check this image size", e);
            return false;
        }
        return !(sourceImage.getHeight() < MIN_IMAGE_HEIGHT || sourceImage.getWidth() < MIN_IMAGE_WIDTH);
    }

    private String createReducedImage(String uploadBaseDirectory, String directory, MultipartFile originalFile, String newFilename, int targetHeight) throws IOException, InterruptedException {
        File fileForWrite = Paths.get(uploadBaseDirectory, directory, newFilename).toFile();
        BufferedImage sourceImage = ImageIO.read(new ByteArrayInputStream(originalFile.getBytes()));
        if (isPng(originalFile)) {
            sourceImage = bufferedImageForPng(originalFile);
        }

        if (sourceImage.getHeight() < targetHeight) {
            if (ImageIO.write(sourceImage, "jpeg", fileForWrite)) {
                return newFilename;
            } else {
                throw new IOException("Could not write preview file to " + fileForWrite.toString());
            }
        } else {
            BufferedImage resizedImage = Scalr.resize(sourceImage, Scalr.Method.BALANCED, Scalr.Mode.FIT_TO_HEIGHT, targetHeight);
            File outputFile = Paths.get(uploadBaseDirectory, directory, newFilename).toFile();
            if (ImageIO.write(resizedImage, "jpeg", outputFile)) {
                return newFilename;
            } else {
                throw new IOException("Could not write preview file to " + outputFile.toString());
            }
        }
    }


    private boolean isPng(MultipartFile originalFile) {
        return originalFile.getContentType().contains(PNG);
    }

    private BufferedImage bufferedImageForPng(MultipartFile originalFile) throws InterruptedException, IOException {
        Image img = Toolkit.getDefaultToolkit().createImage(originalFile.getBytes());
        PixelGrabber pg = new PixelGrabber(img, 0, 0, -1, -1, true);
        pg.grabPixels();
        int width = pg.getWidth(), height = pg.getHeight();
        DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
        WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
        return new BufferedImage(RGB_OPAQUE, raster, false, null);
    }

}
