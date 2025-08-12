package com.leon.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document("Account")
public class Account {
    @Id
    private UUID accountId;
    private String accountName;
    private String accountMnemonic;
    private String legalEntity;
    private boolean isFirmAccount;
    private boolean isRiskAccount;
    private boolean isActive;
    private String customFlags;

    public Account() {
        this.accountId = UUID.randomUUID();
        this.accountName = "";
        this.accountMnemonic = "";
        this.legalEntity = "";
        this.isFirmAccount = false;
        this.isRiskAccount = false;
        this.isActive = true;
        this.customFlags = "";
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountMnemonic() {
        return accountMnemonic;
    }

    public void setAccountMnemonic(String accountMnemonic) {
        this.accountMnemonic = accountMnemonic;
    }

    public String getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(String legalEntity) {
        this.legalEntity = legalEntity;
    }

    public boolean isFirmAccount() {
        return isFirmAccount;
    }

    public void setFirmAccount(boolean firmAccount) {
        isFirmAccount = firmAccount;
    }

    public boolean isRiskAccount() {
        return isRiskAccount;
    }

    public void setRiskAccount(boolean riskAccount) {
        isRiskAccount = riskAccount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCustomFlags() {
        return customFlags;
    }

    public void setCustomFlags(String customFlags) {
        this.customFlags = customFlags;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                ", accountMnemonic='" + accountMnemonic + '\'' +
                ", legalEntity='" + legalEntity + '\'' +
                ", isFirmAccount=" + isFirmAccount +
                ", isRiskAccount=" + isRiskAccount +
                ", isActive=" + isActive +
                ", customFlags='" + customFlags + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return isFirmAccount() == account.isFirmAccount() && isRiskAccount() == account.isRiskAccount() && isActive() == account.isActive() && getAccountId().equals(account.getAccountId()) && getAccountName().equals(account.getAccountName()) && getAccountMnemonic().equals(account.getAccountMnemonic()) && getLegalEntity().equals(account.getLegalEntity()) && Objects.equals(getCustomFlags(), account.getCustomFlags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountId(), getAccountName(), getAccountMnemonic(), getLegalEntity(), isFirmAccount(), isRiskAccount(), isActive(), getCustomFlags());
    }
}
