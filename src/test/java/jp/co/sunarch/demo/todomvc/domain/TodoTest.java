package jp.co.sunarch.demo.todomvc.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

class TodoTest {

  @Nested
  @JsonTest
  class Json {
    @Autowired
    JacksonTester<Todo> json;

    @Test
    void TODOをJSONに変換できる() throws Exception {
      Todo todo = todo(1L, "test", "user", false);

      assertThat(json.write(todo))
          .isEqualToJson("{id:1, title:test, username:user, completed:false}");
    }

    @Test
    void JSONからTODOを復元できる() throws Exception {
      String content = "{" +
          "\"id\":1," +
          "\"title\":\"test\"," +
          "\"username\":\"user\"," +
          "\"completed\":false" +
          "}";

      assertThat(json.parse(content))
          .isEqualTo(todo(1L, "test", "user", false));
    }
  }

  private static Todo todo(Long id, String title, String username, boolean completed) {
    Todo todo = new Todo();
    todo.setId(id);
    todo.setTitle(title);
    todo.setUsername(username);
    todo.setCompleted(completed);
    return todo;
  }
}