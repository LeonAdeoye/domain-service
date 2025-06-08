package com.leon.services;

import com.leon.models.Account;
import com.leon.repositories.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private List<Account> mockAccounts;
    private Account mockAccount1;
    private Account mockAccount2;

    @Before
    public void setUp() {
        mockAccounts = new ArrayList<>();
        mockAccount1 = new Account();
        mockAccount1.setAccountId(UUID.randomUUID());
        mockAccount1.setAccountName("Test Account 1");
        mockAccount1.setAccountMnemonic("ACC1");
        mockAccount1.setLegalEntity("LE1");
        mockAccount1.setFirmAccount(true);
        mockAccount1.setRiskAccount(false);
        mockAccount1.setActive(true);
        mockAccount1.setCustomFlags("FLAG1");

        mockAccount2 = new Account();
        mockAccount2.setAccountId(UUID.randomUUID());
        mockAccount2.setAccountName("Test Account 2");
        mockAccount2.setAccountMnemonic("ACC2");
        mockAccount2.setLegalEntity("LE2");
        mockAccount2.setFirmAccount(false);
        mockAccount2.setRiskAccount(true);
        mockAccount2.setActive(true);
        mockAccount2.setCustomFlags("FLAG2");

        mockAccounts.add(mockAccount1);
        mockAccounts.add(mockAccount2);

        when(accountRepository.findAll()).thenReturn(mockAccounts);
        accountService.initialize();
    }

    @Test
    public void reconfigure_shouldClearAndReloadAccounts() {
        // Arrange
        List<Account> newAccounts = new ArrayList<>();
        Account newAccount = new Account();
        newAccount.setAccountId(UUID.randomUUID());
        newAccount.setAccountName("New Account");
        newAccount.setAccountMnemonic("NEW");
        newAccount.setLegalEntity("LE3");
        newAccount.setFirmAccount(true);
        newAccount.setRiskAccount(true);
        newAccount.setActive(true);
        newAccount.setCustomFlags("FLAG3");
        newAccounts.add(newAccount);
        when(accountRepository.findAll()).thenReturn(newAccounts);

        // Act
        accountService.reconfigure();

        // Assert
        List<Account> result = accountService.getAll();
        assertEquals(1, result.size());
        assertEquals("New Account", result.get(0).getAccountName());
        assertEquals("NEW", result.get(0).getAccountMnemonic());
        assertEquals("LE3", result.get(0).getLegalEntity());
        assertTrue(result.get(0).isFirmAccount());
        assertTrue(result.get(0).isRiskAccount());
        assertTrue(result.get(0).isActive());
        assertEquals("FLAG3", result.get(0).getCustomFlags());
        verify(accountRepository, times(2)).findAll();
    }

    @Test
    public void getAll_shouldReturnAllAccounts() {
        // Act
        List<Account> result = accountService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Test Account 1", result.get(0).getAccountName());
        assertEquals("ACC1", result.get(0).getAccountMnemonic());
        assertEquals("LE1", result.get(0).getLegalEntity());
        assertTrue(result.get(0).isFirmAccount());
        assertFalse(result.get(0).isRiskAccount());
        assertTrue(result.get(0).isActive());
        assertEquals("FLAG1", result.get(0).getCustomFlags());

        assertEquals("Test Account 2", result.get(1).getAccountName());
        assertEquals("ACC2", result.get(1).getAccountMnemonic());
        assertEquals("LE2", result.get(1).getLegalEntity());
        assertFalse(result.get(1).isFirmAccount());
        assertTrue(result.get(1).isRiskAccount());
        assertTrue(result.get(1).isActive());
        assertEquals("FLAG2", result.get(1).getCustomFlags());
    }

    @Test
    public void deleteAccount_withExistingAccount_shouldDeleteAccount() {
        // Arrange
        String accountId = mockAccount1.getAccountId().toString();
        when(accountRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockAccount1));

        // Act
        accountService.deleteAccount(accountId);

        // Assert
        verify(accountRepository).delete(mockAccount1);
        List<Account> result = accountService.getAll();
        assertEquals(1, result.size());
        assertEquals("Test Account 2", result.get(0).getAccountName());
    }

    @Test
    public void deleteAccount_withNonExistingAccount_shouldNotDeleteAnything() {
        // Arrange
        String accountId = UUID.randomUUID().toString();
        when(accountRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        accountService.deleteAccount(accountId);

        // Assert
        verify(accountRepository, never()).delete(any(Account.class));
        List<Account> result = accountService.getAll();
        assertEquals(2, result.size());
    }

    @Test
    public void createAccount_withNewAccount_shouldCreateAccount() {
        // Arrange
        Account newAccount = new Account();
        newAccount.setAccountId(UUID.randomUUID());
        newAccount.setAccountName("New Account");
        newAccount.setAccountMnemonic("NEW");
        newAccount.setLegalEntity("LE3");
        newAccount.setFirmAccount(true);
        newAccount.setRiskAccount(true);
        newAccount.setActive(true);
        newAccount.setCustomFlags("FLAG3");
        lenient().when(accountRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenReturn(newAccount);

        // Act
        Account result = accountService.createAccount(newAccount);

        // Assert
        assertNotNull(result);
        assertEquals("New Account", result.getAccountName());
        assertEquals("NEW", result.getAccountMnemonic());
        assertEquals("LE3", result.getLegalEntity());
        assertTrue(result.isFirmAccount());
        assertTrue(result.isRiskAccount());
        assertTrue(result.isActive());
        assertEquals("FLAG3", result.getCustomFlags());
        verify(accountRepository).save(newAccount);
        List<Account> allAccounts = accountService.getAll();
        assertEquals(3, allAccounts.size());
    }

    @Test
    public void createAccount_withExistingAccount_shouldNotCreateAccount() {
        // Arrange
        lenient().when(accountRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockAccount1));

        // Act
        Account result = accountService.createAccount(mockAccount1);

        // Assert
        assertNull(result);
        verify(accountRepository, never()).save(any(Account.class));
        List<Account> allAccounts = accountService.getAll();
        assertEquals(2, allAccounts.size());
    }

    @Test
    public void updateAccount_withExistingAccount_shouldUpdateAccount() {
        // Arrange
        Account updatedAccount = new Account();
        updatedAccount.setAccountId(mockAccount1.getAccountId());
        updatedAccount.setAccountName("Updated Account");
        updatedAccount.setAccountMnemonic("UPD");
        updatedAccount.setLegalEntity("LE4");
        updatedAccount.setFirmAccount(false);
        updatedAccount.setRiskAccount(true);
        updatedAccount.setActive(false);
        updatedAccount.setCustomFlags("FLAG4");
        when(accountRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockAccount1));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        // Act
        Account result = accountService.updateAccount(updatedAccount);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Account", result.getAccountName());
        assertEquals("UPD", result.getAccountMnemonic());
        assertEquals("LE4", result.getLegalEntity());
        assertFalse(result.isFirmAccount());
        assertTrue(result.isRiskAccount());
        assertFalse(result.isActive());
        assertEquals("FLAG4", result.getCustomFlags());
        verify(accountRepository).save(updatedAccount);
    }

    @Test
    public void updateAccount_withNonExistingAccount_shouldNotUpdateAccount() {
        // Arrange
        Account nonExistingAccount = new Account();
        nonExistingAccount.setAccountId(UUID.randomUUID());
        when(accountRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        Account result = accountService.updateAccount(nonExistingAccount);

        // Assert
        assertNull(result);
        verify(accountRepository, never()).save(any(Account.class));
    }
} 