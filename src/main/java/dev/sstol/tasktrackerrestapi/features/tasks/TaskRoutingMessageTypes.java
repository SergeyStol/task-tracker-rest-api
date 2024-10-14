package dev.sstol.tasktrackerrestapi.features.tasks;

/**
 * @author Sergey Stol
 * 2024-10-13
 */
public interface TaskRoutingMessageTypes {
   String TASK_CREATED_QUEUE_NAME = "TASK_CREATED";
   String TASK_UPDATED_QUEUE_NAME = "TASK_UPDATED";
}
