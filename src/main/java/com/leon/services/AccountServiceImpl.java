package com.leon.services;

import com.leon.models.Account;
import com.leon.repositories.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl  implements AccountService
{
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Autowired
    private AccountRepository accountRepository;
    private List<Account> accounts = new ArrayList<>();

    @PostConstruct
    public void initialize()
    {
        List<Account> result = accountRepository.findAll();
        accounts.addAll(result);
        logger.info("Loaded account service with {} account(s).", result.size());
    }

    @Override
    public void reconfigure()
    {
        accounts.clear();
        initialize();
    }

    @Override
    public List<Account> getAll()
    {
        return accounts;
    }

    @Override
    public void deleteAccount(String accountId)
    {
        Account existingAccount = accountRepository.findById(UUID.fromString(accountId)).orElse(null);
        if (existingAccount == null)
        {
            logger.warn("Account with ID {} does not exist. Not deleting.", accountId);
            return;
        }
        accountRepository.delete(existingAccount);
        accounts.removeIf(a -> a.getAccountId().equals(existingAccount.getAccountId()));
    }

    @Override
    public Account createAccount(Account account)
    {
        if(accounts.stream().anyMatch(a -> a.getAccountId().equals(account.getAccountId())))
        {
            logger.warn("Account with ID {} already exists. Not creating a new one.", account.getAccountId());
            return null;
        }

        accountRepository.save(account);
        accounts.add(account);
        logger.info("Created new account with ID: {}", account.getAccountId());
        return account;
    }

    @Override
    public Account updateAccount(Account account)
    {
        Account existingAccount = accountRepository.findById(account.getAccountId()).orElse(null);
        if (existingAccount == null)
        {
            logger.warn("Account with ID {} does not exist. Not updating.", account.getAccountId());
            return null;
        }
        Account updatedAccount = accountRepository.save(account);
        accounts.removeIf(a -> a.getAccountId().equals(existingAccount.getAccountId()));
        accounts.add(updatedAccount);
        logger.info("Updated account with ID: {}", updatedAccount.getAccountId());
        return  updatedAccount;
    }
}
