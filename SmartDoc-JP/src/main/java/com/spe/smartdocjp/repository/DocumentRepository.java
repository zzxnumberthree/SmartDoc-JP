package com.spe.smartdocjp.repository;

import com.spe.smartdocjp.model.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // zeng
public interface DocumentRepository extends JpaRepository<Document, Long>{

    // 查找某用户所有未删除的文件
    // select * from users where username = ? and is_deleted = 0     ?????
    List<Document> findByUserIdAndIsDeletedFalse(Long userId);



}
