package dev.sstol.tasktrackerrestapi.features.users;

import dev.sstol.tasktrackerrestapi.features.tasks.Task;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails {
   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
   @SequenceGenerator(name = "user_seq")
   @Column(name = "id")
   private Long id;

   @Column(name = "email", nullable = false, unique = true)
   private String email;

   @Column(name = "password", nullable = false)
   private String password;

   @OneToMany(mappedBy = "owner")
   private Set<Task> tasks = new LinkedHashSet<>();

   public User(String email, String password) {
      this.email = email;
      this.password = password;
   }

   public User(Long id, String email) {
      this.id = id;
      this.email = email;
   }

   public User(User user) {
      this.id = user.getId();
      this.email = user.getEmail();
      this.password = user.getPassword();
      this.tasks = new HashSet<>(user.getTasks());
   }

   @Override
   public final boolean equals(Object o) {
      if (this == o) return true;
      if (o == null) return false;
      Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
      Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
      if (thisEffectiveClass != oEffectiveClass) return false;
      User user = (User) o;
      return getId() != null && Objects.equals(getId(), user.getId());
   }

   @Override
   public final int hashCode() {
      return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
   }

   @Override
   public String toString() {
      return getClass().getSimpleName() + "(" +
             "id = " + id + ", " +
             "email = " + email + ")";
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of();
   }

//   @Override
//   public String getPassword() {
//      return password;
//   }

   @Override
   public String getUsername() {
      return email;
   }
}