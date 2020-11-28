package com.bdg.banktransaction.repository;

import com.bdg.banktransaction.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Tatevik Mirzoyan
 * Created on 22-Nov-20
 */
@Repository
public interface UserRepository extends CrudRepository<User,Long> {
}
