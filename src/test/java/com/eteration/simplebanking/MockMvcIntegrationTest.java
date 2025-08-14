// src/test/java/com/eteration/simplebanking/MockMvcIntegrationTest.java
package com.eteration.simplebanking;

import com.eteration.simplebanking.services.BankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MockMvcIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired BankAccountService service;

    @Test
    void credit_should_persist_and_get_balance_is_1000() throws Exception {
        String accNo = "acc-" + System.nanoTime();
        service.create("Kerem Karaca", accNo);

        mvc.perform(post("/account/v1/credit/{acc}", accNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000.0}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.approvalCode", not(emptyString())));

        mvc.perform(get("/account/v1/{acc}", accNo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountNumber").value(accNo))
                .andExpect(jsonPath("$.owner").value("Kerem Karaca"))
                .andExpect(jsonPath("$.balance").value(1000.0));
    }

    @Test
    void debit_after_credit_should_persist_and_balance_is_950() throws Exception {
        String accNo = "acc-" + System.nanoTime();
        service.create("Kerem Karaca", accNo);

        mvc.perform(post("/account/v1/credit/{acc}", accNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000.0}"))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(post("/account/v1/debit/{acc}", accNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":50.0}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.approvalCode", not(emptyString())));

        mvc.perform(get("/account/v1/{acc}", accNo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(950.0));
    }

    @Test
    void bill_payment_should_persist_and_balance_is_903_5() throws Exception {
        String accNo = "acc-" + System.nanoTime();
        service.create("Kerem Karaca", accNo);

        mvc.perform(post("/account/v1/credit/{acc}", accNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000.0}"))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(post("/account/v1/bill-payment/{acc}", accNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"payee\":\"Vodafone\",\"amount\":96.5,\"phoneNumber\":\"5551234567\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.approvalCode", not(emptyString())));

        mvc.perform(get("/account/v1/{acc}", accNo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(903.5));
    }

    @Test
    void get_nonexistent_account_should_return_404() throws Exception {
        String accNo = "non-existent-" + System.nanoTime();

        mvc.perform(get("/account/v1/{acc}", accNo))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("DECLINED"))
                .andExpect(jsonPath("$.message").exists());
    }
}
