package dev.sstol.tasktrackerrestapi.features.tasks;

import java.io.Serializable;

/**
 * @author Sergey Stol
 * 2024-10-10
 */
public record NewTaskDto(String title, String description) implements Serializable {
}
