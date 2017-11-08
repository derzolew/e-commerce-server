package com.ecommerceserver.service.converter;

import com.ecommerceserver.model.ImageEntity;
import com.ecommerceserver.service.dto.ImageDto;
import com.ecommerceserver.service.util.UrlGenerator;
import org.springframework.core.convert.converter.Converter;

public class ImageToDtoConverter implements Converter<ImageEntity, ImageDto> {
    @Override
    public ImageDto convert(ImageEntity imageEntity) {
        ImageDto imageDto = new ImageDto();
        imageDto.setUrl(UrlGenerator.getUrlForImage(imageEntity));
        imageDto.setFileName(imageEntity.getPublicFileName());
        return imageDto;
    }
}
