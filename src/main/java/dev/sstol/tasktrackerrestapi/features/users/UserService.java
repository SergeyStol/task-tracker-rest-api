package dev.sstol.tasktrackerrestapi.features.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sstol.tasktrackerrestapi.features.auth.NewUserDto;
import dev.sstol.tasktrackerrestapi.infrastructure.exceptions.NotFoundException;
import dev.sstol.tasktrackerrestapi.infrastructure.exceptions.AlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static dev.sstol.tasktrackerrestapi.infrastructure.rabbitmq.RabbitMQConfig.EXCHANGE_NAME;

/**
 * @author Sergey Stol
 * 2024-10-07
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

   private final UserRepository repo;
   private final UserMapper mapper;
   private final ObjectMapper objectMapper;
   private final PasswordEncoder passwordEncoder;
   private final RabbitTemplate rabbitTemplate;

   public User save(NewUserDto newUserDto) {
      validate(newUserDto);

      var user = new User(newUserDto.email(), passwordEncoder.encode(newUserDto.password()));

      try {
         log.info("User={}, trying to save to db ...", user.getEmail());
         User savedUser = repo.save(user);
         log.info("User {} saved to database. id={} has been assigned", savedUser.getEmail(), savedUser.getId());
         log.info("Sending notification to other services (user={})", user.getEmail());
         rabbitTemplate.convertAndSend(EXCHANGE_NAME, "USER_CREATED",
           objectMapper.writeValueAsString(mapper.toDto(user)));
         log.info("Notification had been sent successfully (user={})", savedUser.getEmail());
         return savedUser;
      } catch (DataIntegrityViolationException e) {
         log.warn(e.getMessage());
         throw new AlreadyExistsException("User already exists", e);
      } catch (Exception e) {
         log.warn(e.getMessage());
         throw new RuntimeException(e);
      }
   }

   private void validate(NewUserDto newUserDto) {
      if (Strings.isBlank(newUserDto.email())) {
         throw new IllegalArgumentException("Email is empty or absent.");
      }
      if (Strings.isBlank(newUserDto.password())) {
         throw new IllegalArgumentException("Password is empty or absent.");
      }
   }

   public UserDto getUserByEmail(String email) {
      return mapper.toDto(repo.findUserByEmail(email));
   }

   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      User user = repo.findByEmailIgnoreCase(email);
      if (user == null) {
         log.warn("Can't find user with email/username = {}", email);
         throw new NotFoundException("User details not found for " + email);
      }
      return user;
   }
}
