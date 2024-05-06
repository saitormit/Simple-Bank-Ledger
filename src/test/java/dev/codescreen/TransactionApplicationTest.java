package dev.codescreen;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.codescreen.model.DebitOrCredit;
import dev.codescreen.model.TransactionAmount;
import dev.codescreen.model.TransactionRequest;
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
public class TransactionApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //LOAD User ID: 1; Message ID: 1; 200 USD; CREDIT
    @Test
    @Order(1)
    public void testLoad() throws Exception{
        TransactionRequest load = new TransactionRequest(
                "1",
                "1",
                new TransactionAmount(Double.toString(200.00), "USD", DebitOrCredit.CREDIT)
        );
        String requestJson = objectMapper.writeValueAsString(load);
        mockMvc.perform(MockMvcRequestBuilders.put("/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageId").value(load.getMessageId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(load.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("APPROVED"));
    }

    //Tests for authorization calls
    //User has enough funds
    @Test
    @Order(2)
    public void testAuthorizationEnoughFunds() throws Exception {
        //AUTHORIZATION User ID: 2; Message ID: 2; 800 USD; DEBIT
        TransactionRequest authorization1 = new TransactionRequest(
                "2",
                "2",
                new TransactionAmount(Double.toString(800.00), "USD", DebitOrCredit.DEBIT)
        );
        String requestJson = objectMapper.writeValueAsString(authorization1);
        mockMvc.perform(MockMvcRequestBuilders.put("/authorization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageId").value(authorization1.getMessageId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(authorization1.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("DECLINED"));
    }

    //User does not have enough funds
    @Test
    @Order(3)
    public void testAuthorizationNoFunds() throws Exception{
        //AUTHORIZATION User ID: 2; Message ID: 2; 4500 USD; DEBIT
        TransactionRequest authorization1 = new TransactionRequest(
                "2",
                "2",
                new TransactionAmount(Double.toString(4000.00), "USD", DebitOrCredit.DEBIT)
        );
        String requestJson = objectMapper.writeValueAsString(authorization1);
        mockMvc.perform(MockMvcRequestBuilders.put("/authorization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageId").value(authorization1.getMessageId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(authorization1.getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("DECLINED"));
    }

    //Test when there's contradiction between load and authorization with Debit or Credit
    @Test
    @Order(4)
    public void testAmbiguousTransaction() throws Exception{
        //LOAD and AUTHORIZATION User ID: 1; Message ID: 1; 100 USD;
        //For Load
        TransactionRequest load = new TransactionRequest(
                "1",
                "1",
                new TransactionAmount(Double.toString(100.00), "USD", DebitOrCredit.DEBIT)
        );
        String requestJson1 = objectMapper.writeValueAsString(load);
        mockMvc.perform(MockMvcRequestBuilders.put("/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson1))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        //For Authorization
        TransactionRequest authorization = new TransactionRequest(
                "1",
                "1",
                new TransactionAmount(Double.toString(100.00), "USD", DebitOrCredit.CREDIT)
        );
        String requestJson2 = objectMapper.writeValueAsString(authorization);
        mockMvc.perform(MockMvcRequestBuilders.put("/authorization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson2))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Transaction attempt when user of given id is not found
    @Test
    @Order(5)
    public void testNoUserFound() throws Exception{
        TransactionRequest load = new TransactionRequest(
                "5",
                "1",
                new TransactionAmount(Double.toString(100.00), "USD", DebitOrCredit.CREDIT)
        );
        String requestJson1 = objectMapper.writeValueAsString(load);
        mockMvc.perform(MockMvcRequestBuilders.put("/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
