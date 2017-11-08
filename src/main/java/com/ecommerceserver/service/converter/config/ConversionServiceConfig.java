package com.ecommerceserver.service.converter.config;

import com.ecommerceserver.service.converter.ImageToDtoConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ConversionServiceConfig {
    @Bean
    public ConversionServiceFactoryBean conversionService()
    {
        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        Set<Converter> converters = new HashSet<>();
        converters.add(new ImageToDtoConverter());
        bean.setConverters(converters);
        return bean;
    }
}
