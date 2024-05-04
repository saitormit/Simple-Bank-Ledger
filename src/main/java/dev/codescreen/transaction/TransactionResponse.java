package dev.codescreen.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {
    public String messageId;
    public String userId;
    public ResponseCode responseCode;
    public Balance balance;

    public TransactionResponse(String messageId, String userId, ResponseCode responseCode, Balance balance) {
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

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }
}
