package com.ecommerceserver.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "image", schema = "public")
public class ImageEntity implements Serializable
{
    private Long id;
    private StorageEntity storage;
    private String directory;
    private String originalFileName;
    private String reducedFileName;
    private String publicFileName;


    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Column(name = "public_file_name", nullable = false, unique = true)
    public String getPublicFileName()
    {
        return publicFileName;
    }

    public void setPublicFileName(String publicFileName)
    {
        this.publicFileName = publicFileName;
    }

    @ManyToOne(targetEntity = StorageEntity.class)
    @JoinColumn(name = "fk_storage_id")
    public StorageEntity getStorage()
    {
        return storage;
    }

    public void setStorage(StorageEntity storage)
    {
        this.storage = storage;
    }

    @Column(name = "directory", nullable = false)
    public String getDirectory()
    {
        return directory;
    }

    public void setDirectory(String directory)
    {
        this.directory = directory;
    }

    @Column(name = "original_image_file_name", nullable = false)
    public String getOriginalFileName()
    {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName)
    {
        this.originalFileName = originalFileName;
    }

}
