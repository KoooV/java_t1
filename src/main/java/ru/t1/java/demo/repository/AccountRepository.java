package ru.t1.java.demo.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.t1.java.demo.dto.AccountStatusDto;
import ru.t1.java.demo.model.Account;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT new ru.t1.java.demo.dto.AccountStatusDto(c.accountId, c.status) FROM  Account c")
    List<AccountStatusDto> accountToUnarrested(Pageable pageable);

    Account findByAccountId(UUID accountId);


}
