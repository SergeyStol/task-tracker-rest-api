package dev.sstol.tasktrackerrestapi.features.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Sergey Stol
 * 2024-10-10
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
   List<Task> findAllByOwner_Id(@Param("id") Long id);

   Optional<Task> findByIdAndOwner_Id(@Param("id") Long taskId, @Param("ownerId") Long ownerId);

   @Modifying
   @Query("DELETE FROM Task t WHERE t.id = :id AND t.owner.id = :ownerId")
   @Transactional
   void deleteTaskByIdAndOwnerId(@Param("id") Long id, @Param("ownerId") Long ownerId);

   @Query("SELECT count(*) FROM Task t WHERE t.completed = false AND t.completedDate IS NULL")
   long countOpenedTasks();

   @Query("SELECT count(*) FROM Task t WHERE t.completed = true OR t.completedDate IS NOT NULL")
   long countCompletedTasks();
}
