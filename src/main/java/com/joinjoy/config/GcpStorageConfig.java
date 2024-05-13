package com.joinjoy.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Configuration
public class GcpStorageConfig {

    @Bean
    Storage storage() throws IOException {
	    GoogleCredentials credentials = GoogleCredentials
	        .fromStream(new FileInputStream("src/main/resources/omega-chimera-419314-596f14d72e37.json"))
	        .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
	    return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
	}
}
