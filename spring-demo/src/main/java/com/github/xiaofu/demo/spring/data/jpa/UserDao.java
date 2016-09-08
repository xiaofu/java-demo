package com.github.xiaofu.demo.spring.data.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.github.xiaofu.demo.spring.orm.jpa.AccountInfo;

public interface UserDao extends Repository<AccountInfo, Long> {
	public void save(AccountInfo model);

	// first annotation
	// native query ,use custom query
	@Query(value = "select u from AccountInfo as u where u.accountId >= :id and 1=1")
	public AccountInfo findByAccountId(@Param("id") long id);

	@Query(value = "select u from AccountInfo as u where u.accountId >= ?1 and 1=1")
	public AccountInfo findByAccountIdOrderByAccountIdAsc(long id);

	// @Query(value="select * from t_accountinfo as u where u.accountId >= ?1 and 1=1",countQuery="select count(*) from t_accountinfo as u where u.accountId >= ?1",nativeQuery=true)
	List<AccountInfo> findBybalance(int balance, Pageable pageable);

	Slice<AccountInfo> queryBybalance(int balance, Pageable pageable);

	Page<AccountInfo> readBybalance(int balance, Pageable pageable);
	//TODO:must  @Transactional  ,只是因为用了@Query，所以需要显示添加事务标注
	@Transactional
	@Modifying
	@Query("update AccountInfo  u set u.balance=?1 where u.accountId =?2")
	int updateData(int balance,long id);

}