package com.example.todo.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.todo.Entity.Task;
import com.example.todo.Entity.User;
import com.example.todo.Service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, @AuthenticationPrincipal User user) {
        task.setUser(user);
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasksByUser(@AuthenticationPrincipal User user) {
        List<Task> tasks = taskService.getTasksByUser(user);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task updatedTask, @AuthenticationPrincipal User user) {
        Task existingTask = taskService.getTaskById(taskId);
        if (existingTask == null || !existingTask.getUser().getId().equals(user.getId())) {
            return ResponseEntity.notFound().build();
        }

        updatedTask.setId(taskId);
        updatedTask.setUser(user);
        Task savedTask = taskService.updateTask(updatedTask);
        return ResponseEntity.ok(savedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId, @AuthenticationPrincipal User user) {
        Task existingTask = taskService.getTaskById(taskId);
        if (existingTask == null || !existingTask.getUser().getId().equals(user.getId())) {
            return ResponseEntity.notFound().build();
        }

        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}