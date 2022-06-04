package com.paymentology.aka.recon;

import com.paymentology.aka.recon.controller.FileUploadController;
import com.paymentology.aka.recon.controller.ReconController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DataReconciliationApplicationTests {

	@Autowired
	FileUploadController fileUploadController;

	@Autowired
	ReconController reconController;

	@Test
	void contextLoads() {
		assertThat(fileUploadController).isNotNull();
		assertThat(reconController).isNotNull();
	}

}
