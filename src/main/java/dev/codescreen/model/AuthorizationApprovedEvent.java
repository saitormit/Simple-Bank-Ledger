package dev.codescreen.model;

import com.fasterxml.jackson.annotation.JsonProperty;

//Event when the user has enough balance and authorization process is approved
public class AuthorizationApprovedEvent extends Event{
    @JsonProperty
    private String transactionType;

    @JsonProperty
    private String amount;

    @JsonProperty
    private ResponseCode responseCode;

    public AuthorizationApprovedEvent(String userId, String transactionId, TransactionAmount transactionAmount) {
        super(userId, transactionId);
        this.transactionType = "authorization";
        this.amount = transactionAmount.getAmount();
        this.responseCode = ResponseCode.APPROVED;
    }
}
