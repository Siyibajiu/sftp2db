package com.adidas.sftp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
public class SftpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SftpApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate(){

        RestTemplate restTemplate = new RestTemplate();

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);

        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode() != HttpStatus.UNAUTHORIZED.value()
                        && response.getRawStatusCode() != HttpStatus.BAD_REQUEST.value()){
                    super.handleError(response);
                }
            }
        });
        return restTemplate;
    }

}
