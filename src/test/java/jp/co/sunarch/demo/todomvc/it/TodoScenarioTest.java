package jp.co.sunarch.demo.todomvc.it;

import jp.co.sunarch.demo.todomvc.controller.TodoController.AddTodoBody;
import jp.co.sunarch.demo.todomvc.controller.TodoController.UpdateTodoBody;
import jp.co.sunarch.demo.todomvc.domain.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TodoScenarioTest {

  private static final String USERNAME = "user";
  private static final String PASSWORD = "user";

  @Autowired
  TestRestTemplate restTemplate;

  @BeforeEach
  void setUp() {
    restTemplate = restTemplate.withBasicAuth(USERNAME, PASSWORD);
  }

  @Test
  void test() {
    assertThat(fetchTodos())
        .as("TODOリストは、空")
        .isEmpty();

    // 1件追加
    Todo todo1 = addTodo("test1");

    assertThat(fetchTodos())
        .as("TODOリストは、1件のTODOを含む")
        .hasSize(1)
        .contains(todo1);

    // 更に1件追加
    Todo todo2 = addTodo("test2");

    assertThat(fetchTodos())
        .as("TODOリストは、2件のTODOを含む")
        .hasSize(2)
        .contains(todo2);

    // 1件目のTODOタイトルを更新
    updateTodo(todo1, "new-title");

    assertThat(fetchTodos())
        .as("TODOリストで、更新後のタイトルが確認できる")
        .extracting(Todo::getTitle).contains("new-title");

    // 2件目のTODOをトグルする
    toggleTodo(todo2);

    assertThat(fetchTodos())
        .as("TODOリストで、トグル後の状態が確認できる")
        .extracting(Todo::isCompleted).contains(true);

    // 1件目のTODOを削除する
    deleteTodo(todo1);

    assertThat(fetchTodos())
        .as("TODOリストから、削除済みのTODOがなくなる")
        .extracting(Todo::getId).doesNotContain(todo1.getId());
  }

  private Todo[] fetchTodos() {
    return restTemplate.getForObject("/api/todos", Todo[].class);
  }

  private Todo addTodo(String title) {
    Todo added = restTemplate.postForObject("/api/todos", addTodoBody(title), Todo.class);

    assertThat(added.getId()).as("追加したTODOは、IDが採番される").isNotNull();
    assertThat(added.getTitle()).as("追加したTODOは、指定したタイトルを持つ").isEqualTo(title);
    assertThat(added.getUsername()).as("追加したTODOは、ログインユーザーに紐づく").isEqualTo(USERNAME);
    assertThat(added.isCompleted()).as("追加したTODOは、未完了状態").isFalse();

    return added;
  }

  private Todo updateTodo(Todo todo, String newTitle) {
    HttpEntity<UpdateTodoBody> entity = new HttpEntity<>(updateTodoBody(newTitle));
    ResponseEntity<Todo> response = restTemplate.exchange("/api/todos/{id}", HttpMethod.PUT, entity, Todo.class, todo.getId());
    Todo updated = response.getBody();

    assertThat(updated.getTitle()).as("更新時は、指定したタイトルに更新される").isEqualTo(newTitle);
    assertThat(updated.isCompleted()).as("更新時は、完了状態は変更しない").isEqualTo(todo.isCompleted());

    return updated;
  }

  private Todo toggleTodo(Todo todo) {
    Todo updated = restTemplate.postForObject("/api/todos/{id}/_toggle", null, Todo.class, todo.getId());

    assertThat(updated.isCompleted()).as("トグル時は、完了状態を反転する").isEqualTo(!todo.isCompleted());

    return updated;
  }

  private void deleteTodo(Todo todo) {
    restTemplate.delete("/api/todos/{id}", todo.getId());
  }

  private static AddTodoBody addTodoBody(String title) {
    AddTodoBody body = new AddTodoBody();
    body.setTitle(title);
    return body;
  }

  private static UpdateTodoBody updateTodoBody(String title) {
    UpdateTodoBody body = new UpdateTodoBody();
    body.setTitle(title);
    return body;
  }
}
