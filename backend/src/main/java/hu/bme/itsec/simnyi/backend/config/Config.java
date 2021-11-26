package hu.bme.itsec.simnyi.backend.config;

import hu.bme.itsec.simnyi.backend.model.Caff;
import org.springframework.content.fs.config.FilesystemStoreConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;

@Configuration
public class Config {

    // Set password encoding schema
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilesystemStoreConfigurer configurer() {
        //TODO It has to be in that form. It can't be simplified because of the api.
        return new FilesystemStoreConfigurer() {

            @Override
            public void configureFilesystemStoreConverters(ConverterRegistry registry) {
                registry.addConverter(new Converter<Caff, String>() {

                    @Override
                    public String convert(Caff caff) {
                        return caff.getName() + ".caff";
                    }
                });
            }
        };
    }

}
