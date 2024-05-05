package dev.codescreen.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;

//This class' purpose is to define the response body of the PUT requests
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {
    public String messageId;
    public String userId;
    public ResponseCode responseCode;
    public TransactionAmount balance; //named balance to match the schema

    public TransactionResponse(String messageId, String userId, ResponseCode responseCode, TransactionAmount balance) {
        this.messageId = messageId;
        this.userId = userId;
        this.responseCode = responseCode;
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public TransactionAmount getBalance() {
        return balance;
    }

    public void setBalance(TransactionAmount balance) {
        this.balance = balance;
    }
}
