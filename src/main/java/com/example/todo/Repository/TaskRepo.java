package com.example.todo.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.todo.Entity.Task;


public interface TaskRepo extends JpaRepository<Task, Long> {

    List<Task> findByUser_Id(Long userId);


}
