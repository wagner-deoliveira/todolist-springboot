package com.thinkr.todolist.task;

import com.thinkr.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var id = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) id);

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartedAt()) || currentDate.isAfter(taskModel.getFinishedAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start/end date should be past the current date " + currentDate);
        }

        if (taskModel.getStartedAt().isAfter(taskModel.getFinishedAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End date should be past the start date " + taskModel.getStartedAt());
        }

        taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully");
    }

    @GetMapping
    public List<TaskModel> listTasks(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasks = taskRepository.findByIdUser((UUID) idUser);

        return tasks;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTask(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        var task = taskRepository.findById(id).orElseThrow();
        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found.");
        }

        var idUser = request.getAttribute("idUser");
        if (!task.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user does not have the permission to update the task.");
        }
        Utils.copyNonNullProperties(taskModel, task);

        return ResponseEntity.ok().body(taskRepository.save(task));
    }
}
