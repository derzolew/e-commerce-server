package com.ecommerceserver.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "storage", schema = "public")
public class StorageEntity implements Serializable {

    private Long id;
    private String path;

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

    @Column(name = "storage_path", nullable = false)
    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }
}
