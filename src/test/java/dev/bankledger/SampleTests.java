package dev.bankledger;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bankledger.model.DebitOrCredit;
import dev.bankledger.model.TransactionAmount;
import dev.bankledger.model.TransactionRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SampleTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //action, msgId, userId, debitOrCredit, responseCode, balance
    //LOAD,1,1,CREDIT,100,APPROVED,100.00
    @Test
    @Order(1)
    public void testLoad() throws Exception {
        TransactionRequest load = new TransactionRequest(
                "1",
                "1",
                new TransactionAmount(String.format("%.2f", 100.00), "USD", DebitOrCredit.CREDIT)
        );
        String requestJson = objectMapper.writeValueAsString(load);
        mockMvc.perform(MockMvcRequestBuilders.put("/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageId").value(load.getMessageId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(load.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.amount").value("100.00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("APPROVED"));
    }

    //LOAD,2,1,CREDIT,3.23,APPROVED,103.23
    @Test
    @Order(2)
    public void testLoad2() throws Exception {
        TransactionRequest load = new TransactionRequest(
                "1",
                "2",
                new TransactionAmount(Double.toString(3.23), "USD", DebitOrCredit.CREDIT)
        );
        String requestJson = objectMapper.writeValueAsString(load);
        mockMvc.perform(MockMvcRequestBuilders.put("/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageId").value(load.getMessageId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(load.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.amount").value("103.23"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("APPROVED"));
    }

    //AUTHORIZATION,3,1,DEBIT,100,APPROVED,3.23
    @Test
    @Order(3)
    public void testAuthorization3() throws Exception {
        TransactionRequest authorization = new TransactionRequest(
                "1",
                "3",
                new TransactionAmount(String.format("%.2f", 100.00), "USD", DebitOrCredit.DEBIT)
        );
        String requestJson = objectMapper.writeValueAsString(authorization);
        mockMvc.perform(MockMvcRequestBuilders.put("/authorization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageId").value(authorization.getMessageId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(authorization.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.amount").value("3.23"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("APPROVED"));
    }

    //AUTHORIZATION,4,1,DEBIT,10,DENIED,3.23
    @Test
    @Order(4)
    public void testAuthorization4() throws Exception {
        TransactionRequest authorization = new TransactionRequest(
                "1",
                "4",
                new TransactionAmount(String.format("%.2f", 10.00), "USD", DebitOrCredit.DEBIT)
        );
        String requestJson = objectMapper.writeValueAsString(authorization);
        mockMvc.perform(MockMvcRequestBuilders.put("/authorization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageId").value(authorization.getMessageId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(authorization.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.amount").value("3.23"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("DECLINED"));
    }

    //AUTHORIZATION,5,2,DEBIT,50.01,DENIED,0.00
    @Test
    @Order(5)
    public void testAuthorization5() throws Exception {
        TransactionRequest authorization = new TransactionRequest(
                "2",
                "5",
                new TransactionAmount(String.format("%.2f", 50.01), "USD", DebitOrCredit.DEBIT)
        );
        String requestJson = objectMapper.writeValueAsString(authorization);
        mockMvc.perform(MockMvcRequestBuilders.put("/authorization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageId").value(authorization.getMessageId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(authorization.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.amount").value("0.00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("DECLINED"));
    }

    //LOAD,6,3,CREDIT,APPROVED,50.01
    @Test
    @Order(6)
    public void testLoad6() throws Exception {
        TransactionRequest load = new TransactionRequest(
                "3",
                "6",
                new TransactionAmount(Double.toString(50.01), "USD", DebitOrCredit.CREDIT)
        );
        String requestJson = objectMapper.writeValueAsString(load);
        mockMvc.perform(MockMvcRequestBuilders.put("/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageId").value(load.getMessageId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(load.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.amount").value("50.01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("APPROVED"));
    }

    //AUTHORIZATION,7,2,DEBIT,50.01,APPROVED,0.00
    //NOTE: The test sample indicates "APPROVED", but following the logic the expected result should be DECLINED
    @Test
    @Order(7)
    public void testAuthorization7() throws Exception {
        TransactionRequest authorization = new TransactionRequest(
                "2",
                "7",
                new TransactionAmount(Double.toString(50.01), "USD", DebitOrCredit.DEBIT)
        );
        String requestJson = objectMapper.writeValueAsString(authorization);
        mockMvc.perform(MockMvcRequestBuilders.put("/authorization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageId").value(authorization.getMessageId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(authorization.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.amount").value("0.00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("DECLINED"));
    }
}
