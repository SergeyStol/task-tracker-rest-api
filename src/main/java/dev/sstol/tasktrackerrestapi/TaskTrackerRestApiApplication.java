package dev.sstol.tasktrackerrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskTrackerRestApiApplication {

   public static void main(String[] args) {
      SpringApplication.run(TaskTrackerRestApiApplication.class, args);
   }
}
