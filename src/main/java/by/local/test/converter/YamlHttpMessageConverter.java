package by.local.test.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;


import java.io.IOException;
import java.io.OutputStreamWriter;

public class YamlHttpMessageConverter<T>
        extends AbstractHttpMessageConverter<T> {
    private ObjectMapper mapper;
    public YamlHttpMessageConverter () {
        super(new MediaType("text", "yaml"));
        mapper = new ObjectMapper(new YAMLFactory());
    }

    @Override
    protected boolean supports (Class<?> clazz) {
        return true;
    }

    @Override
    protected T readInternal (Class<? extends T> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return mapper.readValue(inputMessage.getBody(),clazz);
    }

    @Override
    protected void writeInternal (T t, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody());
        mapper.writeValue(writer,t);
        writer.close();
    }
}