package com.smh.bs.server.controller;

import com.smh.bs.server.dto.BundleUploadDto;
import com.smh.bs.server.model.Bundle;
import com.smh.bs.server.util.BundleUtils;
import io.netty.handler.codec.base64.Base64Encoder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BundleControllerTest {

    RestTemplate restTemplate;
    Base64Encoder base64Encoder;

    @BeforeEach
    void initialize(){
        restTemplate=new RestTemplate();
    }

    @SneakyThrows
    @Test
    @DisplayName("BundleUpload")
    void uploadTest(){
        ResponseEntity<Bundle> bundleResponseEntity=restTemplate.exchange("http://localhost:9000/bundle/upload",HttpMethod.POST,
                new HttpEntity<BundleUploadDto>(BundleUtils.generateBundleUploadDto()),Bundle.class);
        System.out.println(bundleResponseEntity.getBody());
    }

}