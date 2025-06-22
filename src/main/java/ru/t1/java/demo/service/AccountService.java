package ru.t1.java.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.aspectspringbootstarter.annotation.DataSourceError;
import org.example.aspectspringbootstarter.annotation.Metric;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.repository.ClientRepository;
import ru.t1.java.demo.model.Client;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

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

    public Account create(Long clientId, BigDecimal balance, Account.Type type) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + clientId));
        Account account = new Account();
        account.setClient(client);
        account.setBalance(balance);
        account.setType(type);
        account.setStatus(Account.Status.OPEN);
        account.setFrozenAmount(BigDecimal.ZERO);
        return accountRepository.save(account);
    }

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

    public Account update(Long id, BigDecimal balance, Account.Type type) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + id));
        account.setBalance(balance);
        account.setType(type);
        return accountRepository.save(account);
    }
}
