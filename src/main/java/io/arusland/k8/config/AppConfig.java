package io.arusland.k8.config;

import io.arusland.k8.catalog.fs.FileCatalogSystemProvider;
import io.arusland.k8.catalog.fs.FileSkipProvider;
import io.arusland.k8.search.ResultParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

/**
 * Created by ruslan on 27.06.2015.
 */
@Configuration
public class AppConfig  extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
    }

    @Bean
    public ResultParser resultParser(){
        return new ResultParser(Arrays.asList(fileCatalogSystemProvider()));
    }

    @Bean
    public FileCatalogSystemProvider fileCatalogSystemProvider(){
        return new FileCatalogSystemProvider(fileSkipper());
    }

    @Bean
    public FileSkipProvider fileSkipper() {
        return new FileSkipProvider();
    }
}
