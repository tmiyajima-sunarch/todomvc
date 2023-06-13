package jp.co.sunarch.demo.todomvc.service;


import jp.co.sunarch.demo.todomvc.domain.Todo;
import jp.co.sunarch.demo.todomvc.repository.TodoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TodoService {
  private static final Logger LOG = LoggerFactory.getLogger(TodoService.class);
 
  private final TodoRepository todoRepository;

  public TodoService(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }

  public List<Todo> findTodos(String username) {
    return this.todoRepository.findByUsername(username);
  }

  public Todo findTodo(String username, long todoId) {
    return this.findTodoOrElseThrow(username, todoId);
  }

  @Transactional
  public long addTodo(String username, String title) {
    Todo todo = new Todo();
    todo.setUsername(username);
    todo.setTitle(title);
    Todo saved = this.todoRepository.save(todo);
    LOG.info("Todo '{}' added by '{}'", saved.getId(), username);
    return saved.getId();
  }

  @Transactional
  public void deleteTodo(String username, long todoId) {
    Todo todo = this.findTodoOrElseThrow(username, todoId);
    this.todoRepository.delete(todo);
    LOG.info("Todo '{}' deleted by '{}'", todoId, username);
  }

  @Transactional
  public void updateTodo(String username, long todoId, String newTitle) {
    Todo todo = this.findTodoOrElseThrow(username, todoId);
    todo.setTitle(newTitle);
    this.todoRepository.save(todo);
    LOG.info("Todo '{}' updated by '{}'", todoId, username);
  }

  @Transactional
  public void toggleTodo(String username, long todoId) {
    Todo todo = this.findTodoOrElseThrow(username, todoId);
    todo.toggle();
    this.todoRepository.save(todo);
    LOG.info("Todo '{}' toggled by '{}'", todoId, username);
  }

  private Todo findTodoOrElseThrow(String username, long todoId) {
    return this.todoRepository.findById(todoId)
        .filter(todo -> todo.getUsername().equals(username))
        .orElseThrow(() -> new NotFoundException("Todo of id '" + todoId + "' is not found"));
  }
}
