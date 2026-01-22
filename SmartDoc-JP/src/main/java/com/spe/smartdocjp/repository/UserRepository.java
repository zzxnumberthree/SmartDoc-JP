package com.spe.smartdocjp.repository;

import com.spe.smartdocjp.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // zeng
public interface UserRepository extends JpaRepository<User, Long> { // User是表名，Long是主键类型
    // 自动生成 select * from users where username = ?
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

}
