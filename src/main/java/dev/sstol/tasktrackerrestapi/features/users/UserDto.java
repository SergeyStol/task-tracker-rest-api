package dev.sstol.tasktrackerrestapi.features.users;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record UserDto(Long id, String email) implements Serializable {
}