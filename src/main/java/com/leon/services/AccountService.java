package com.leon.services;

import com.leon.models.Account;

import java.util.List;

public interface AccountService 
{

    void reconfigure();

    List<Account> getAll();

    void deleteAccount(String accountId);

    Account createAccount(Account account);

    Account updateAccount(Account account);
}
