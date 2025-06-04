package com.leon.controllers;

import com.leon.models.Account;
import com.leon.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController()
@RequestMapping("/account")
public class AccountController
{
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    private AccountService accountService;

    @CrossOrigin
    @RequestMapping(value="/reconfigure", method=GET)
    public ResponseEntity<Void> reconfigure()
    {
        logger.info("Received request to reconfigure.");
        this.accountService.reconfigure();
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(method=GET, produces = "application/json")
    public ResponseEntity<List<Account>> getAll()
    {
        logger.info("Received request to get all accounts.");
        return new ResponseEntity<>(this.accountService.getAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method=POST , consumes = "application/json"  ,produces = "application/json")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        if (account == null || account.getAccountId() == null || account.getAccountId().toString().isEmpty()) {
            logger.error("Attempted to create an account with null or empty ID.");
            return null;
        }

        if(account.getAccountName() == null || account.getAccountName().isEmpty()) {
            logger.error("Attempted to create an account with null or empty name.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to create account: {}", account);
        Account createdAccount = accountService.createAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(method = PUT, consumes = "application/json"  ,produces = "application/json")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
        if (account == null || account.getAccountId() == null || account.getAccountId().toString().isEmpty()) {
            logger.error("Attempted to update an account with null or empty ID.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(account.getAccountName() == null || account.getAccountName().isEmpty()) {
            logger.error("Attempted to update an account with null or empty name.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Received request to update account: {}", account);
        Account updatedAccount = accountService.updateAccount(account);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/{accountId}", method = DELETE)
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        logger.info("Received request to delete account with ID: {}", accountId);
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}
