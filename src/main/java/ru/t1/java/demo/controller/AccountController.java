package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import org.example.aspectspringbootstarter.annotation.DataSourceError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.model.Account;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    @DataSourceError
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto request) {
        Account account = accountService.create(
            request.getClientId(),
            request.getBalance(),
            request.getType()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return accountService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountDto request) {
        Account updatedAccount = accountService.update(
            id,
            request.getBalance(),
            request.getType()
        );
        return ResponseEntity.ok(updatedAccount);
    }
}
