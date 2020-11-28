package com.bdg.banktransaction.repository;

import com.bdg.banktransaction.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Tatevik Mirzoyan
 * Created on 22-Nov-20
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}
