package jp.co.sunarch.demo.todomvc.controller;

import jp.co.sunarch.demo.todomvc.domain.Todo;
import jp.co.sunarch.demo.todomvc.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
  public Todo addTodo(Principal principal, @RequestBody AddTodoBody body) {
    long todoId = this.todoService.addTodo(principal.getName(), body.getTitle());
    return this.todoService.findTodo(principal.getName(), todoId);
  }

  @DeleteMapping("{todoId}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void deleteTodo(Principal principal, @PathVariable long todoId) {
    this.todoService.deleteTodo(principal.getName(), todoId);
  }

  @PutMapping("{todoId}")
  public Todo updateTodo(Principal principal, @PathVariable long todoId, @RequestBody UpdateTodoBody body) {
    this.todoService.updateTodo(principal.getName(), todoId, body.getTitle());
    return this.todoService.findTodo(principal.getName(), todoId);
  }

  @PostMapping("{todoId}/_toggle")
  public Todo toggleTodo(Principal principal, @PathVariable long todoId) {
    this.todoService.toggleTodo(principal.getName(), todoId);
    return this.todoService.findTodo(principal.getName(), todoId);
  }

  public static class AddTodoBody {
    private String title;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }
  }

  public static class UpdateTodoBody {
    private String title;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }
  }
}
