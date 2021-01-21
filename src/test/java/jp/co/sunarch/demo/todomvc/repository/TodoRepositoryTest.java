package jp.co.sunarch.demo.todomvc.repository;

import jp.co.sunarch.demo.todomvc.domain.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TodoRepositoryTest {

  @Autowired
  TodoRepository sut;

  @Autowired
  TestEntityManager testEntityManager;

  @Nested
  class FindByUsername {
    @BeforeEach
    void setUp() {
      testEntityManager.persist(todo("a", "user1"));
      testEntityManager.persist(todo("b", "user2"));
      testEntityManager.persist(todo("c", "user2"));
    }

    @Test
    void 指定したユーザーのTODOが0件の場合は空のリストを返す() {
      List<Todo> result = sut.findByUsername("user0");

      assertThat(result).isEmpty();
    }

    @Test
    void 指定したユーザーのTODOが1件の場合は1件のリストを返す() {
      List<Todo> result = sut.findByUsername("user1");

      assertThat(result)
          .hasSize(1)
          .extracting(Todo::getTitle)
          .containsExactlyInAnyOrder("a");
    }

    @Test
    void 指定したユーザーのTODOが複数件の場合はその件数分のリストを返す() {
      List<Todo> result = sut.findByUsername("user2");

      assertThat(result)
          .hasSize(2)
          .extracting(Todo::getTitle)
          .containsExactlyInAnyOrder("b", "c");
    }
  }

  private static Todo todo(String title, String username) {
    Todo todo = new Todo();
    todo.setTitle(title);
    todo.setUsername(username);
    return todo;
  }
}