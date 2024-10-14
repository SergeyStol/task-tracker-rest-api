package dev.sstol.tasktrackerrestapi.features.tasks;

import dev.sstol.tasktrackerrestapi.features.users.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Sergey Stol
 * 2024-10-10
 */
@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false, insertable = false, updatable = false)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "owner_id", nullable = false)
   private User owner;

   @Column(name = "title", nullable = false)
   private String title;

   @Column(name = "description", length = 1024)
   private String description;

   @Column(name = "completed_date")
   private Timestamp completedDate;

   @Column(name = "completed")
   private boolean completed = false;

   @Override
   public final boolean equals(Object o) {
      if (this == o) return true;
      if (o == null) return false;
      Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
      Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
      if (thisEffectiveClass != oEffectiveClass) return false;
      Task task = (Task) o;
      return getId() != null && Objects.equals(getId(), task.getId());
   }

   @Override
   public final int hashCode() {
      return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
   }

   @Override
   public String toString() {
      return getClass().getSimpleName() + "(" +
             "id = " + id + ", " +
             "title = " + title + ", " +
             "description = " + description + ", " +
             "completedDate = " + completedDate + ", " +
             "completed = " + completed + ")";
   }
}
