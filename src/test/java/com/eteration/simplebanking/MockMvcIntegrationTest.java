// src/test/java/com/eteration/simplebanking/MockMvcIntegrationTest.java
package com.eteration.simplebanking;

import com.eteration.simplebanking.services.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * NOT:
 * Bu testler, controller'da /credit çağrısından sonra hesap persist edilmediği (service.save(account) yok)
 * "mevcut davranışa" göre yazıldı. Bu yüzden:
 * - /credit 200 döner ve approvalCode boş değildir
 * - Hemen ardından /debit 50 => 400 (Insufficient balance) döner (çünkü bakiye kalıcı olarak artmadı)
 * - GET /account/v1/{acc} çağrısı balance=0 gösterir
 *
 * Eğer ileride controller'da post sonrası service.save(account) eklersen:
 *   - debit_status_shouldBe400_withoutPersist() testindeki 400 beklentisini 200 yap,
 *   - get_should_show_zero_balance_withoutPersist() testindeki balance beklentisini 950.0/853.5 gibi güncelle.
 */
@SpringBootTest
@AutoConfigureMockMvc
class MockMvcIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired
    BankAccountService service;

    private final String accNo = "669-7788";

    @BeforeEach
    void init() {

        service.create("Kerem Karaca", accNo);
    }

    @Test
    void credit_should_return200_and_nonEmptyApprovalCode() throws Exception {
        mvc.perform(post("/account/v1/credit/{acc}", accNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.approvalCode", not(emptyString())));
    }

    @Test
    void debit_status_shouldBe400_withoutPersist() throws Exception {
        // Önce credit (persist yok; sonraki debit başarısız olacak)
        mvc.perform(post("/account/v1/credit/{acc}", accNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000.0}"))
                .andExpect(status().isOk());

        // Bakiye kalıcı artmadığı için debit 50 => 400 beklenir
        mvc.perform(post("/account/v1/debit/{acc}", accNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":50.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("DECLINED"));
    }

    @Test
    void bill_payment_shouldBe400_withoutPersist() throws Exception {
        // Credit çağrısı yapılsa da persist yok; bill-payment 96.5 => 400 beklenir
        mvc.perform(post("/account/v1/credit/{acc}", accNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000.0}"))
                .andExpect(status().isOk());

        mvc.perform(post("/account/v1/bill-payment/{acc}", accNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"payee\":\"Vodafone\",\"amount\":96.5}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("DECLINED"));
    }

    @Test
    void get_should_show_zero_balance_withoutPersist() throws Exception {
        // Credit sonrası dahi, persist yoksa GET balance 0 döner
        mvc.perform(post("/account/v1/credit/{acc}", accNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000.0}"))
                .andExpect(status().isOk());

        mvc.perform(get("/account/v1/{acc}", accNo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(accNo))
                .andExpect(jsonPath("$.owner").value("Kerem Karaca"))
                .andExpect(jsonPath("$.balance").value(0.0));
    }
}
