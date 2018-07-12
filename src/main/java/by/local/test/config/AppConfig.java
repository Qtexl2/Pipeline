package by.local.test.config;

import by.local.test.converter.YamlHttpMessageConverter;
import by.local.test.model.Pipeline;
import by.local.test.repository.PipelineRepository;
import by.local.test.service.impl.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Bean
    @Scope("prototype")
    public Task getTask(){
        return new Task();
    }

    @Override
    public void extendMessageConverters  (List<HttpMessageConverter<?>> converters) {
        converters.add(new YamlHttpMessageConverter<>());
    }
}
