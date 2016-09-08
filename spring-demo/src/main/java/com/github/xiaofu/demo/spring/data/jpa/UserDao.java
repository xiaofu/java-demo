package com.github.xiaofu.demo.spring.data.jpa;

 
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import com.github.xiaofu.demo.spring.orm.jpa.AccountInfo;


public interface UserDao extends Repository<AccountInfo, Long> { 
   public void save(AccountInfo model);
    public AccountInfo findByAccountId(long id);
    public AccountInfo findByAccountIdOrderByAccountIdAsc(long id);
    List<AccountInfo> findBybalance(int balance, Pageable pageable);
    Slice<AccountInfo> queryBybalance(int balance, Pageable pageable);
    Page<AccountInfo> readBybalance(int balance, Pageable pageable);
 }