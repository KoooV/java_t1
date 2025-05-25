package ru.t1.java.demo.service;

import ru.t1.java.demo.model.Account;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

public interface AccountService {
    List<Account> getAllAccounts();
    Optional<Account> findById(Long id);
    Account create(Long clientId, BigDecimal balance, Account.Type type);
    void delete(Long id);
    Account update(Long id, BigDecimal balance, Account.Type type);
}
