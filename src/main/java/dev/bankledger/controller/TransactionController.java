package dev.bankledger.controller;

import dev.bankledger.model.Event;
import dev.bankledger.repository.EventRepository;
import dev.bankledger.model.TransactionRequest;
import dev.bankledger.service.TransactionService;
import dev.bankledger.model.User;
import dev.bankledger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//This controller contains all endpoints of this API
@RestController
public class TransactionController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public TransactionController(TransactionService transactionService, UserRepository userRepository, EventRepository eventRepository) {
        this.transactionService = transactionService;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    //Tests the availability of the service and returns the current server time
    @GetMapping("/ping")
    public ResponseEntity<Object> pingServer(){
        return transactionService.pingServer();
    }

    //Removes funds from user's account if there's enough fund
    @PutMapping("/authorization")
    public ResponseEntity<Object> authorizeFunds(@RequestBody TransactionRequest transactionRequest){
        return transactionService.modifyFunds(transactionRequest, "authorization");
    }

    //Adds funds to the user's account
    @PutMapping("/load")
    public ResponseEntity<Object> loadFunds(@RequestBody TransactionRequest transactionRequest){
        return transactionService.modifyFunds(transactionRequest, "load");
    }

    //Gets all the registered users and their information
    @GetMapping
    public List<User> showUsers(){
        return userRepository.getAllUsers();
    }

    //Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id){
        return transactionService.searchForUser(id);
    }

    //Gets the history of all approved and declined transactions from all users
    @GetMapping("/events")
    public Map<String, List<Event>> getEventRecord(){
        return eventRepository.getEventRepository();
    }
}
