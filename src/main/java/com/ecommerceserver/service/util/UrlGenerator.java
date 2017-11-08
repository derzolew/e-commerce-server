package com.ecommerceserver.service.util;

import com.ecommerceserver.model.ImageEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("urlGenerator")
public class UrlGenerator
{
    @Value("${ecommerce.site.url}")
    private String url;

    public String getUrl(){
        return url;
    }

    public static String getUrlForImage(ImageEntity entity)
    {
        if (entity.getPublicFileName() != null)
        {
            return "/upload/image/" + entity.getPublicFileName();
        }
        return null;
    }

}