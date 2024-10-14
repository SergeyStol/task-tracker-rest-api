package dev.sstol.tasktrackerrestapi.features.tasks;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link Task}
 */

public record TaskDto(
  Long id,
  String title,
  String description,
  boolean completed,
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonFormat(
    shape = JsonFormat.Shape.STRING,
    pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
  Timestamp completedDate
) implements Serializable { }