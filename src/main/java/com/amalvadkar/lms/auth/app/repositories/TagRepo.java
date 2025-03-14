package com.amalvadkar.lms.auth.app.repositories;

import com.amalvadkar.lms.auth.app.entities.TagEntity;
import com.amalvadkar.lms.auth.app.models.dto.KeyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepo extends JpaRepository<TagEntity, String> {

    @Query(value = """
            select
                t.id as `key` ,
                t.name as `value`
            from tags as t
            where t.created_by = :userId
            and t.delete_flag = false
            """, nativeQuery = true)
    List<KeyValue<String, String>> findTagsForUser(@Param("userId") String userId);

}