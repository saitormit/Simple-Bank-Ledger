package dev.codescreen.transaction;

import dev.codescreen.user.User;
import dev.codescreen.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<String> helloWorld(){
        return List.of("Hello", "World", "Roberto");
    }

    //Tests the availability of the service and returns the current server time
    @GetMapping("/ping")
    public ResponseEntity<Object> pingServer(){
        return transactionService.pingServer();
    }

    //Removes funds from user's account if there's enough fund
    @PutMapping("/authorization/{id}")
    public ResponseEntity<Object> authorizeFunds(@RequestBody TransactionRequest transactionRequest, @PathVariable int id){
        return transactionService.authorizeFunds(transactionRequest, id);
    }

    //Adds funds to the user's account
    @PutMapping("/load/{id}")
    public ResponseEntity<Object> loadFunds(@RequestBody TransactionRequest transactionRequest, @PathVariable int id){
        return transactionService.loadFunds(transactionRequest, id);
    }
}
