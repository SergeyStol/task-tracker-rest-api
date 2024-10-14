package dev.sstol.tasktrackerrestapi.features.tasks;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
   Task toEntity(NewTaskDto newTaskDto);

   @Mapping(target = "completed", expression = "java(task.getCompletedDate() != null)")
   @Mapping(target = "completedDate", conditionExpression = "java(task.getCompletedDate() != null)")
   TaskDto toDto(Task task);

   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   Task partialUpdate(TaskDto taskDto, @MappingTarget Task task);
}