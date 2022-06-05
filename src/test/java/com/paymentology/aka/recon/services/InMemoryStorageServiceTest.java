package com.paymentology.aka.recon.services;

import com.paymentology.aka.recon.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class InMemoryStorageServiceTest {

    @Autowired
    InMemoryStorageService storageService;

    @Test
    public void testSaveAndReadFile() throws Exception {

        MultipartFile file = TestUtils.getTestDataFile();
        String filename = file.getName();

        storageService.saveFile(filename, file);

        InputStream savedStream = storageService.readFile(filename);

        BufferedReader reader = new BufferedReader(new InputStreamReader(savedStream));

        String firstLine = reader.readLine();

        assertThat(firstLine).isEqualTo(TestUtils.getLine1());
    }

}
