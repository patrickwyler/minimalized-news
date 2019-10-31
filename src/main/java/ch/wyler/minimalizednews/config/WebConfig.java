package ch.wyler.minimalizednews.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Web configuration
 */
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * Add resources to webcontext
     * @param registry registry
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        // Images folder
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/images/");

        // CSS folder
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/css/");

        // Java-doc folder
        registry.addResourceHandler("/java-doc/**")
                .addResourceLocations("classpath:/java-doc/");
    }

}