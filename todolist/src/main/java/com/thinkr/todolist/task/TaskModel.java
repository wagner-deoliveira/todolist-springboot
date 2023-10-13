package com.thinkr.todolist.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "tb_task")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String description;
    @Column(length = 50)
    private String title;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private String priority;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private UUID idUser;

    public void setTitle(String title) throws IllegalArgumentException {
        if (title.length() > 50) {
            throw new IllegalArgumentException("Title cannot have more than 50 characters");
        }

        this.title = title;
    }
}
