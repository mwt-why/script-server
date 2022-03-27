package com.mwt.repository.res;

import com.mwt.beans.res.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account,String> {

}
