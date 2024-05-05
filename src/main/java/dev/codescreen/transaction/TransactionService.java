package dev.codescreen.transaction;

import dev.codescreen.event.*;
import dev.codescreen.user.User;
import dev.codescreen.exception.UserNotFoundException;
import dev.codescreen.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//This class contains all the business logic to manage all the users' funds
@Component
public class TransactionService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

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

    //Method to modify the user's funds with exception handling
    public ResponseEntity<Object> modifyFunds(TransactionRequest transactionRequest, String transactionType){

        try {
            User user = userRepository.findUserById(transactionRequest.getUserId());
            DebitOrCredit debitOrCredit = transactionRequest.getTransactionAmount().getDebitOrCredit();

            //Check if there's ambiguity in the transaction type
            if((transactionType.equals("load") && debitOrCredit.equals(DebitOrCredit.DEBIT)) ||
                    (transactionType.equals("authorization") && debitOrCredit.equals(DebitOrCredit.CREDIT))){
                ErrorResponse errorResponse = new ErrorResponse("Ambiguity in transaction type was found", HttpStatus.BAD_REQUEST.toString());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            //Default response body
            TransactionResponse transactionResponse = new TransactionResponse(
                    transactionRequest.getMessageId(),
                    transactionRequest.getUserId(),
                    null,
                    new TransactionAmount(
                            Double.toString(user.getBalance()),
                            transactionRequest.getTransactionAmount().getCurrency(),
                            null
                    )
            );
            //Process the debit transaction if the user has enough funds
            if(transactionType.equals("authorization")){
                transactionResponse.setResponseCode(ResponseCode.DECLINED);
                transactionResponse.getBalance().setDebitOrCredit(DebitOrCredit.DEBIT);

                double netBalance = user.getBalance() - Double.parseDouble(transactionRequest.getTransactionAmount().getAmount());

                if(netBalance >= 0){
                    //Update the user's balance in the in-memory object
                    user.setBalance(netBalance);
                    transactionResponse.setResponseCode(ResponseCode.APPROVED);
                    transactionResponse.getBalance().setAmount(Double.toString(netBalance));

                    //Saving the authorization approval into event repository
                    eventRepository.addEventToRepository(transactionRequest.getUserId(), new AuthorizationApprovedEvent(
                            transactionRequest.getUserId(),
                            "SomeTransactionID",
                            transactionRequest.getTransactionAmount()
                    ));
                    return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);
                }
                //Saving the authorization denial into event repository
                eventRepository.addEventToRepository(transactionRequest.getUserId(), new AuthorizationDeclinedEvent(
                        transactionRequest.getUserId(),
                        "SomeTransactionID",
                        transactionRequest.getTransactionAmount()
                ));
                return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);

            }
            //Process the addition of funds to the user's balance
            else if (transactionType.equals("load")) {
                transactionResponse.getBalance().setDebitOrCredit(DebitOrCredit.CREDIT);
                transactionResponse.setResponseCode(ResponseCode.APPROVED);

                double netBalance = user.getBalance() + Double.parseDouble(transactionRequest.getTransactionAmount().getAmount());
                user.setBalance(netBalance);
                transactionResponse.getBalance().setAmount(Double.toString(netBalance));

                //Saving load process into event repository
                eventRepository.addEventToRepository(transactionRequest.getUserId(), new LoadEvent(
                        transactionRequest.getUserId(),
                        "SomeTransactionID",
                        transactionRequest.getTransactionAmount()
                ));
                return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);
            }

            ErrorResponse errorResponse = new ErrorResponse("Bad request", HttpStatus.BAD_REQUEST.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }catch (UserNotFoundException e){
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    //Method to get the user information with exception handling
    public ResponseEntity<Object> searchForUser(Long id){
        try {
            return ResponseEntity.ok(userRepository.findUserById(Long.toString(id)));
        }catch (UserNotFoundException e){
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
