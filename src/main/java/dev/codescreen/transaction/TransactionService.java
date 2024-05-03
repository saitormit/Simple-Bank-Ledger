package dev.codescreen.transaction;

import dev.codescreen.user.User;
import dev.codescreen.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    //Method to get the current server time with exception handling
    public ResponseEntity<Object> pingServer(){
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            String formattedTime = currentTime.format(DateTimeFormatter.ISO_DATE_TIME);
            return ResponseEntity.ok(new PingRecord(formattedTime));
        }catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    //Method to authorize transaction if the user has enough balance
    public ResponseEntity<Object> authorizeFunds(TransactionRequest transactionRequest, int id){

        try {
            User user = userRepository.findUserById(Integer.toString(id));
            //Default response body
            TransactionResponse transactionResponse = new TransactionResponse(
                    transactionRequest.getMessageId(),
                    transactionRequest.getUserId(),
                    ResponseCode.DECLINED,
                    new Balance(
                            Double.toString(user.getBalance()),
                            transactionRequest.getTransactionAmount().getCurrency(),
                            transactionRequest.getTransactionAmount().getDebitOrCredit()
                    )
            );

            double netBalance = user.getBalance() - Double.parseDouble(transactionRequest.getTransactionAmount().getAmount());
            if(netBalance >= 0){
                user.setBalance(netBalance);
                transactionResponse.setResponseCode(ResponseCode.APPROVED);
                transactionResponse.getBalance().setAmount(Double.toString(netBalance));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);

        }catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    public ResponseEntity<Object> loadFunds(TransactionRequest transactionRequest, int id){
        return null;
    }
}
