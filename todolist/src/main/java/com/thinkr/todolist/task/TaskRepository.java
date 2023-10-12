package com.thinkr.todolist.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskModel, UUID> {
    TaskModel findByIdUser(UUID idUser);

    List<TaskModel> findAllById(UUID idUser);
}
