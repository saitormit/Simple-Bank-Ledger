package dev.codescreen.transaction;

public class Balance extends TransactionAmount{
    public Balance(String amount, String currency, DebitOrCredit debitOrCredit) {
        super(amount, currency, debitOrCredit);
    }
}
