package com.hui.SpringBoot22.repository;

import com.hui.SpringBoot22.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findUserById(Long id);

    @Transactional
    @Modifying
    @Query("update User set username=?2,password=?3 where id=?1")
    int updateUserById(Long id,String username,String password);

    @Transactional
    @Modifying
    @Query("delete from User where id=?1")
    void deleteUserById(Long id);
}
