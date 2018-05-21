package com.dinfo.dispatcher.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.dinfo.dispatcher.config.properties.LibraryProperties;
import com.dinfo.dispatcher.core.DispatcherContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangxf
 */
@Configuration
public class AppConfig {

    /**
     * initialize
     */
    @Bean
    public CommandLineRunner commandLineRunner(LibraryProperties properties) {
        return (args) -> DispatcherContext.initialize(properties.getPath());
    }

    /**
     * Use FastJson serializer instead of Jackson.
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastConverter);
    }

}
