package ru.t1.java.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.annotation.DataSourceError;
import ru.t1.java.demo.annotation.Metric;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Metric
    @DataSourceError
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Metric
    @DataSourceError
    public Account getAccountById(UUID accountId) {
        return accountRepository.findByAccountId(accountId);
    }

    @Metric
    @DataSourceError
    @Transactional
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Metric
    @DataSourceError
    @Transactional
    public Account updateAccount(UUID accountId, Account accountDetails) {
        Account account = accountRepository.findByAccountId(accountId);
        if (account != null) {
            account.setBalance(accountDetails.getBalance());
            account.setStatus(accountDetails.getStatus());
            return accountRepository.save(account);
        }
        return null;
    }

    @Metric
    @DataSourceError
    @Transactional
    public void deleteAccount(UUID accountId) {
        Account account = accountRepository.findByAccountId(accountId);
        if (account != null) {
            accountRepository.delete(account);
        }
    }

    @Metric
    @DataSourceError
    @Transactional
    public Account updateBalance(UUID accountId, BigDecimal newBalance) {
        Account account = accountRepository.findByAccountId(accountId);
        if (account != null) {
            account.setBalance(newBalance);
            return accountRepository.save(account);
        }
        return null;
    }
}
