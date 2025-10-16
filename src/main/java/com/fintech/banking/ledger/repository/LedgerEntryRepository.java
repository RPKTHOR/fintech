package com.fintech.banking.ledger.repository;

import com.fintech.banking.ledger.model.LedgerEntry;
import com.fintech.banking.ledger.model.AccountHead;
import com.fintech.banking.ledger.model.EntryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByAccountId(Long accountId);
    List<LedgerEntry> findByTransactionId(String transactionId);
    List<LedgerEntry> findByAccountHead(AccountHead accountHead);
    
    @Query("SELECT SUM(l.amount) FROM LedgerEntry l WHERE l.accountId = :accountId AND l.entryType = :entryType")
    BigDecimal sumByAccountIdAndEntryType(Long accountId, EntryType entryType);
}