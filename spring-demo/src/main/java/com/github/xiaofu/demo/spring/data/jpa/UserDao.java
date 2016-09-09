package com.github.xiaofu.demo.spring.data.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import  javax.persistence.QueryHint;

import com.github.xiaofu.demo.spring.orm.jpa.AccountInfo;

public interface UserDao extends  JpaRepository<AccountInfo, Long>,JpaSpecificationExecutor<AccountInfo>  {
	 

	// first use @Query then @NamesQuery annotation
	//use spel replace class type
	@Query(value = "select u from #{#entityName} as u where u.id >= :id and 1=1")
	public AccountInfo findByAccountId(@Param("id") long id);

	@Query(value = "select u from #{#entityName} as u where u.id >= ?1 and 1=1")
	public AccountInfo findByAccountIdOrderByAccountIdAsc(long id);
	
	//failed
	// @Query(value="select * from t_accountinfo as u where u.accountId >= ?1 and 1=1",countQuery="select count(*) from t_accountinfo as u where u.accountId >= ?1",nativeQuery=true)
	List<AccountInfo> findBybalance(int balance, Pageable pageable);

	Slice<AccountInfo> queryBybalance(int balance, Pageable pageable);
	@QueryHints(value = { @QueryHint(name = "name", value = "value")},
            forCounting = false)
	Page<AccountInfo> readBybalance(int balance, Pageable pageable);
	//TODO:must  @Transactional  ,只是因为用了@Query，所以需要显示添加事务标注
	@Transactional
	@Modifying
	@Query("update #{#entityName}  u set u.balance=?1 where u.id =?2")
	int updateData(int balance,long id);

}