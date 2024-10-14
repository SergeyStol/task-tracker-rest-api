package dev.sstol.tasktrackerrestapi.features.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserRepository extends JpaRepository<User, Long> {
   User findByEmailIgnoreCase(@NonNull String email);

   User findUserByEmail(String email);
}