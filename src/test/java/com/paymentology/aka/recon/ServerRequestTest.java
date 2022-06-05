package com.paymentology.aka.recon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentology.aka.recon.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServerRequestTest {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void helloShouldReturnDefaultMessage() throws Exception {

        ResponseEntity<Object[]> responseEntity  = this.restTemplate.getForEntity("http://localhost:" + port + "/file/hello",
                Object[].class);

        Transaction transaction = mapper.convertValue(responseEntity.getBody()[0], Transaction.class);

        assertThat(transaction.getDescription()).isEqualTo("Hello");
    }
}
