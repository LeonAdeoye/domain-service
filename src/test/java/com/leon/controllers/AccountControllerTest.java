package com.leon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.models.Account;
import com.leon.services.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void reconfigure_shouldCallServiceReconfigure() throws Exception {
        mockMvc.perform(get("/account/reconfigure"))
                .andExpect(status().isNoContent());
        verify(accountService, times(1)).reconfigure();
    }

    @Test
    public void getAll_shouldReturnListOfAccounts() throws Exception {
        List<Account> accounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setAccountId(UUID.randomUUID());
        account1.setAccountName("Account 1");
        Account account2 = new Account();
        account2.setAccountId(UUID.randomUUID());
        account2.setAccountName("Account 2");
        accounts.add(account1);
        accounts.add(account2);

        when(accountService.getAll()).thenReturn(accounts);

        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].accountName").value("Account 1"))
                .andExpect(jsonPath("$[1].accountName").value("Account 2"));
    }

    @Test
    public void createAccount_withValidData_shouldCreateAccount() throws Exception {
        Account account = new Account();
        account.setAccountId(UUID.randomUUID());
        account.setAccountName("New Account");
        when(accountService.createAccount(any(Account.class))).thenReturn(account);

        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountName").value("New Account"));
    }

    @Test
    public void createAccount_withNullId_shouldReturnBadRequest() throws Exception {
        Account account = new Account();
        account.setAccountId(null);
        account.setAccountName("New Account");

        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createAccount_withEmptyName_shouldReturnBadRequest() throws Exception {
        Account account = new Account();
        account.setAccountId(UUID.randomUUID());
        account.setAccountName("");

        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateAccount_withValidData_shouldUpdateAccount() throws Exception {
        Account account = new Account();
        account.setAccountId(UUID.randomUUID());
        account.setAccountName("Updated Account");
        when(accountService.updateAccount(any(Account.class))).thenReturn(account);

        mockMvc.perform(put("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountName").value("Updated Account"));
    }

    @Test
    public void updateAccount_withNullId_shouldReturnBadRequest() throws Exception {
        Account account = new Account();
        account.setAccountId(null);
        account.setAccountName("Updated Account");

        mockMvc.perform(put("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateAccount_withEmptyName_shouldReturnBadRequest() throws Exception {
        Account account = new Account();
        account.setAccountId(UUID.randomUUID());
        account.setAccountName("");

        mockMvc.perform(put("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteAccount_withValidId_shouldDeleteAccount() throws Exception {
        String accountId = UUID.randomUUID().toString();
        doNothing().when(accountService).deleteAccount(accountId);

        mockMvc.perform(delete("/account/" + accountId))
                .andExpect(status().isNoContent());
        verify(accountService, times(1)).deleteAccount(accountId);
    }
} 