package com.paymentology.aka.recon.controller;

import com.paymentology.aka.recon.TestUtils;
import com.paymentology.aka.recon.model.ProcessingResults;
import com.paymentology.aka.recon.model.ReconStatus;
import com.paymentology.aka.recon.model.Response;
import com.paymentology.aka.recon.services.StorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FileUploadControllerTest {

    @Autowired
    private FileUploadController fileUploadController;

    @Autowired
    private StorageService storageService;

    @Test
    public void testFileUploadAndProcessingSuccess() throws Exception {
        MultipartFile file = TestUtils.getTestDataFile();

        Response response = fileUploadController.handleFileUpload(file, file.getName());

        assertThat(response.getStatus()).isEqualTo(ReconStatus.SUCCESS);

        long timeout = 5000; // Check for 5 secs till it processes, if it didn't process by then, something is wrong

        while (timeout > 0) {
            Response statusResponse = fileUploadController.getFileUploadStatus(file.getName());

            if (statusResponse.getStatus() == ReconStatus.SUCCESS) {

                ProcessingResults results = storageService.getProcessingResults(file.getName());

                assertThat(results).isNotNull();

                assertThat(results.getRecordsCount()).isEqualTo(TestUtils.getRecordsCount());

                break;
            } else {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                    // Ignore. There's nothing we can do about this
                }

                timeout -= 1000;
            }
        }
    }
}
