package jp.co.sunarch.demo.todomvc.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jp.co.sunarch.demo.todomvc.domain.Todo;
import jp.co.sunarch.demo.todomvc.service.TodoService;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

  private final TodoService todoService;

  public TodoController(TodoService todoService) {
    this.todoService = todoService;
  }

  @GetMapping
  public List<Todo> listTodos(Principal principal) {
    return this.todoService.findTodos(principal.getName());
  }

  @PostMapping
  public Todo addTodo(Principal principal, @Valid @RequestBody AddTodoBody body) {
    long todoId = this.todoService.addTodo(principal.getName(), body.getTitle());
    return this.todoService.findTodo(principal.getName(), todoId);
  }

  @DeleteMapping("{todoId}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void deleteTodo(Principal principal, @PathVariable long todoId) {
    this.todoService.deleteTodo(principal.getName(), todoId);
  }

  @PutMapping("{todoId}")
  public Todo updateTodo(Principal principal, @PathVariable long todoId, @Valid @RequestBody UpdateTodoBody body) {
    this.todoService.updateTodo(principal.getName(), todoId, body.getTitle());
    return this.todoService.findTodo(principal.getName(), todoId);
  }

  @PostMapping("{todoId}/_toggle")
  public Todo toggleTodo(Principal principal, @PathVariable long todoId) {
    this.todoService.toggleTodo(principal.getName(), todoId);
    return this.todoService.findTodo(principal.getName(), todoId);
  }

  public static class AddTodoBody {
    @NotBlank
    private String title;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }
  }

  public static class UpdateTodoBody {
    @NotBlank
    private String title;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }
  }
}
