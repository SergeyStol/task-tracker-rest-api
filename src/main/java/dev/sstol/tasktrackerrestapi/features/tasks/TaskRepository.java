package dev.sstol.tasktrackerrestapi.features.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Sergey Stol
 * 2024-10-10
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
   List<Task> findAllByOwner_Id(Long id);

   void deleteTaskByIdAndOwner_Id(Long taskId, Long principalId);

   @Transactional
   @Modifying
   @Query("UPDATE Task t SET t.completed = :completed, t.completedDate = :completedDate WHERE t.id = :taskId AND t.owner.id = :userId")
   int updateCompletedStatus(
     @Param("userId") Long userId,
     @Param("taskId") Long taskId,
     @Param("completed") boolean completed,
     @Param("completedDate") Timestamp completedDate
   );
}
