package dev.codescreen.model;

import com.fasterxml.jackson.annotation.JsonInclude;

//Class to organize transaction details meeting the schema provided
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionAmount {
    public String amount;
    public String currency;
    public DebitOrCredit debitOrCredit;

    public TransactionAmount(String amount, String currency, DebitOrCredit debitOrCredit) {
        this.amount = amount;
        this.currency = currency;
        this.debitOrCredit = debitOrCredit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public DebitOrCredit getDebitOrCredit() {
        return debitOrCredit;
    }

    public void setDebitOrCredit(DebitOrCredit debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }
}
