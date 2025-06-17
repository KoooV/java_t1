package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.t1.java.demo.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Получить транзакцию по ID (используем стандартный метод findById)
    Optional<Transaction> findById(Long id);

    // Получить все транзакции (используем стандартный метод findAll)
    List<Transaction> findAll();
    
    // Найти все транзакции по ID счета
    List<Transaction> findByAccountId(Long accountId);
    
    // Сохранить транзакцию (используем стандартный метод save)
    Transaction save(Transaction transaction);
    
    // Удалить транзакцию (используем стандартный метод deleteById)
    void deleteById(Long id);

    Transaction findByTransactionId(UUID transactionId);

    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId AND t.timestamp >= :startTime ORDER BY t.timestamp DESC")
    List<Transaction> findRecentTransactionsByAccount(@Param("accountId") UUID accountId, @Param("startTime") LocalDateTime startTime);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.account.accountId = :accountId AND t.status = 'REJECTED'")
    long countRejectedTransactionsByAccount(@Param("accountId") UUID accountId);
}
