package com.smh.bs.server.repository.redis;

import com.smh.bs.server.util.BundleUploadTraceUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class BundleUploadTraceRepositoryImplTest {

    @Autowired
    BundleUploadTraceRepository bundleUploadTraceRepository;

    static BundleUploadTrace bundleUploadTrace;

    @BeforeAll
    static void createRandomBundleTraceInstance(){
        bundleUploadTrace=BundleUploadTraceUtils.generateRandom();
    }

    @Order(1)
    @Test
    @DisplayName("SaveBundleUploadTrace")
    void save(){
        bundleUploadTraceRepository.save(bundleUploadTrace);
    }

    @Order(2)
    @Test
    @DisplayName("FindBundleUploadTrace")
    void find(){
        BundleUploadTrace bundleUploadTraceFromRedis=bundleUploadTraceRepository.find(bundleUploadTrace.getBundleId());
        assertEquals(bundleUploadTraceFromRedis,bundleUploadTrace);
    }

    @Order(3)
    @Test
    @DisplayName("UpdateBundleUploadTrace")
    void update(){
        bundleUploadTrace.setUploadedSize(20000);
        bundleUploadTraceRepository.update(bundleUploadTrace);
    }

    @Order(4)
    @Test
    @DisplayName("FindUpdatedBundleUploadTrace")
    void findUpdated(){
        BundleUploadTrace bundleUploadTraceFromRedis=bundleUploadTraceRepository.find(bundleUploadTrace.getBundleId());
        assertEquals(bundleUploadTraceFromRedis,bundleUploadTrace);
    }

    @Order(5)
    @Test
    @DisplayName("DeleteBundleUploadTrace")
    void delete(){
        bundleUploadTraceRepository.delete(bundleUploadTrace.getBundleId());
    }

}