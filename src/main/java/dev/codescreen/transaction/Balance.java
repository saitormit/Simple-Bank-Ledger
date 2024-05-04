package dev.codescreen.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Balance extends TransactionAmount{
    public Balance(String amount, String currency, DebitOrCredit debitOrCredit) {
        super(amount, currency, debitOrCredit);
    }
}
