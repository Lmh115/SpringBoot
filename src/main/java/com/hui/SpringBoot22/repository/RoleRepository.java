package com.hui.SpringBoot22.repository;

import com.hui.SpringBoot22.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface RoleRepository extends JpaRepository<Role,Long> {
//    @Query(nativeQuery = true,
//            value="select * from user_roles " +
//                    "where username = (select username from test_user where id=?1 limit (0,1))")
//    Role findRoleByUserid(Long id);
    @Query(nativeQuery = true,
        value="select r.id,r.username,r.role_name from user_roles u left join user_roles r on u.username=r.username " +
                "where u.id=?1")
    Role findRoleByUserid(Long id);

    @Transactional
    @Modifying
    @Query("update Role set roles = ?2 where username = (select username from User where id = ?1)")
    void updateRoleForUserById(Long id,String roles);
}
