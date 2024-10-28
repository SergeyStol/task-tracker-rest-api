package dev.sstol.tasktrackerrestapi.features.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sstol.tasktrackerrestapi.features.auth.RegisterNewUserDto;
import dev.sstol.tasktrackerrestapi.infrastructure.jwt.JwtTokenService;
import dev.sstol.tasktrackerrestapi.infrastructure.api.AlreadyExistsException409;
import dev.sstol.tasktrackerrestapi.infrastructure.api.BadRequestException400;
import dev.sstol.tasktrackerrestapi.infrastructure.api.InternalServerErrorException500;
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
   private final JwtTokenService jwtTokenService;
   private final UserMapper mapper;
   private final ObjectMapper objectMapper;
   private final PasswordEncoder passwordEncoder;
   private final RabbitTemplate rabbitTemplate;

   public String saveAndGetToken(RegisterNewUserDto registerNewUserDto) {
      if (Strings.isBlank(registerNewUserDto.email())) {
         throw new BadRequestException400("Please, specify owner email.");
      }
      if (Strings.isBlank(registerNewUserDto.password())) {
         throw new BadRequestException400("Please, specify owner password.");
      }

      var user = new User(registerNewUserDto.email(), passwordEncoder.encode(registerNewUserDto.password()));

      try {
         log.info("User={}, trying to save to db ...", user.getEmail());
         User savedUser = repo.save(user);
         log.info("User {} saved to database. id={} has been assigned", savedUser.getEmail(), savedUser.getId());
         log.info("Sending notification to other services (user={})", user.getEmail());
         rabbitTemplate.convertAndSend(EXCHANGE_NAME, "USER_CREATED",
           objectMapper.writeValueAsString(mapper.toDto(user)));
         log.info("Notification had been sent successfully (user={})", savedUser.getEmail());
      } catch (DataIntegrityViolationException e) {
         throw new AlreadyExistsException409("User already exists", e);
      } catch (Exception e) {
         log.warn(e.getMessage());
         throw new InternalServerErrorException500(e);
      }
      return jwtTokenService.getToken(user.getUsername(), user.getId(), user.getEmail());
   }

   public UserDto getUserByEmail(String email) {
      return mapper.toDto(repo.findUserByEmail(email));
   }

   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      User user = repo.findByEmailIgnoreCase(email);
      if (user == null) {
         throw new UsernameNotFoundException("User details not found for " + email);
      }
      return user;
   }
}
