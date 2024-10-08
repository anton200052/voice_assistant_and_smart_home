package me.vasylkov.ai_module.repository;

import me.vasylkov.ai_module.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Long>
{
    @Modifying
    @Query("DELETE FROM Message m WHERE m.id > (SELECT MIN(m2.id) FROM Message m2)")
    void deleteAllExceptFirst();

    @Modifying
    @Query("DELETE FROM Message m WHERE m.id IN (SELECT m2.id FROM Message m2 ORDER BY m2.id ASC LIMIT 6 OFFSET 1)")
    void deleteFirstFiveExceptFirst();

    Message findFirstByOrderByIdAsc();
}
