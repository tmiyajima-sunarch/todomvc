package jp.co.sunarch.demo.todomvc.service;

import jp.co.sunarch.demo.todomvc.domain.Todo;
import jp.co.sunarch.demo.todomvc.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

  @InjectMocks
  private TodoService sut;

  @Mock
  private TodoRepository todoRepository;

  @Nested
  class FindTodos {
    @Test
    void 指定ユーザーに紐づくTODOを取得する() {
      sut.findTodos("user");

      verify(todoRepository).findByUsername("user");
    }

    @Test
    void 取得したTODOを返却する() {
      when(todoRepository.findByUsername(any()))
          .thenReturn(Arrays.asList(todo(1L, "a"), todo(2L, "b")));

      List<Todo> todos = sut.findTodos("user");

      assertThat(todos).containsExactly(todo(1L, "a"), todo(2L, "b"));
    }
  }

  @Nested
  class FindTodo {
    @Test
    void 指定されたTODOを取得する() {
      when(todoRepository.findById(anyLong()))
          .thenReturn(Optional.of(todo(1L, "a", "user")));

      sut.findTodo("user", 1L);

      verify(todoRepository).findById(1L);
    }

    @Test
    void 取得したTODOを返却する() {
      when(todoRepository.findById(anyLong()))
          .thenReturn(Optional.of(todo(1L, "a", "user")));

      Todo todo = sut.findTodo("user", 1L);

      assertThat(todo).isEqualTo(todo(1L, "a", "user"));
    }

    @Nested
    class 指定されたTODOが見つからない場合はNotFoundExceptionを投げる {
      @Test
      void 指定されたIDのTODOが見つからない場合() {
        when(todoRepository.findById(anyLong()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.findTodo("user", 1L))
            .isInstanceOf(NotFoundException.class);
      }

      @Test
      void 指定されたIDのTODOが別ユーザーのものの場合() {
        when(todoRepository.findById(anyLong()))
            .thenReturn(Optional.of(todo(1L, "a", "user2")));

        assertThatThrownBy(() -> sut.findTodo("user", 1L))
            .isInstanceOf(NotFoundException.class);
      }
    }
  }

  @Nested
  class AddTodo {
    @BeforeEach
    void setUp() {
      when(todoRepository.save(any()))
          .then((invocation) -> {
            Todo todo = invocation.getArgument(0);
            Todo copy = todo.clone();
            copy.setId(1L);
            return copy;
          });
    }

    @Test
    void 指定されたタイトルのTODOをユーザーに紐付けて保存する() {
      sut.addTodo("user", "test");

      ArgumentCaptor<Todo> captor = ArgumentCaptor.forClass(Todo.class);
      verify(todoRepository).save(captor.capture());
      Todo todo = captor.getValue();
      assertThat(todo).isEqualTo(todo(null, "test", "user"));
    }

    @Test
    void 保存されたTODOのIDを返却する() {
      long id = sut.addTodo("user", "test");

      assertThat(id).isEqualTo(1L);
    }
  }

  @Nested
  class DeleteTodo {
    @Test
    void 指定されたTODOを取得する() {
      when(todoRepository.findById(anyLong()))
          .thenReturn(Optional.of(todo(1L, "a", "user")));

      sut.deleteTodo("user", 1L);

      verify(todoRepository).findById(1L);
    }

    @Test
    void 取得したTODOを削除する() {
      when(todoRepository.findById(anyLong()))
          .thenReturn(Optional.of(todo(1L, "a", "user")));

      sut.deleteTodo("user", 1L);

      verify(todoRepository).delete(todo(1L, "a", "user"));
    }

    @Nested
    class 指定されたTODOが見つからない場合はNotFoundExceptionを投げる {
      @Test
      void 指定されたIDのTODOが見つからない場合() {
        when(todoRepository.findById(anyLong()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.deleteTodo("user", 1L))
            .isInstanceOf(NotFoundException.class);
      }

      @Test
      void 指定されたIDのTODOが別ユーザーのものの場合() {
        when(todoRepository.findById(anyLong()))
            .thenReturn(Optional.of(todo(1L, "a", "user2")));

        assertThatThrownBy(() -> sut.deleteTodo("user", 1L))
            .isInstanceOf(NotFoundException.class);
      }
    }
  }

  @Nested
  class UpdateTodo {
    @Test
    void 指定されたTODOを取得する() {
      when(todoRepository.findById(anyLong()))
          .thenReturn(Optional.of(todo(1L, "a", "user")));

      sut.updateTodo("user", 1L, "b");

      verify(todoRepository).findById(1L);
    }

    @Test
    void 取得したTODOのタイトルを更新して保存する() {
      when(todoRepository.findById(anyLong()))
          .thenReturn(Optional.of(todo(1L, "a", "user")));

      sut.updateTodo("user", 1L, "b");

      verify(todoRepository).save(todo(1L, "b", "user"));
    }

    @Nested
    class 指定されたTODOが見つからない場合はNotFoundExceptionを投げる {
      @Test
      void 指定されたIDのTODOが見つからない場合() {
        when(todoRepository.findById(anyLong()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.updateTodo("user", 1L, "b"))
            .isInstanceOf(NotFoundException.class);
      }

      @Test
      void 指定されたIDのTODOが別ユーザーのものの場合() {
        when(todoRepository.findById(anyLong()))
            .thenReturn(Optional.of(todo(1L, "a", "user2")));

        assertThatThrownBy(() -> sut.updateTodo("user", 1L, "b"))
            .isInstanceOf(NotFoundException.class);
      }
    }
  }

  @Nested
  class ToggleTodo {
    @Test
    void 指定されたTODOを取得する() {
      when(todoRepository.findById(anyLong()))
          .thenReturn(Optional.of(todo(1L, "a", "user")));

      sut.toggleTodo("user", 1L);

      verify(todoRepository).findById(1L);
    }

    @Nested
    class 取得したTODOの状態をトグルして保存する {
      @Test
      void TODOが未完了の場合は完了にする() {
        when(todoRepository.findById(anyLong()))
            .thenReturn(Optional.of(todo(1L, "a", "user", false)));

        sut.toggleTodo("user", 1L);

        verify(todoRepository).save(todo(1L, "a", "user", true));
      }

      @Test
      void TODOが完了の場合は未完了にする() {
        when(todoRepository.findById(anyLong()))
            .thenReturn(Optional.of(todo(1L, "a", "user", true)));

        sut.toggleTodo("user", 1L);

        verify(todoRepository).save(todo(1L, "a", "user", false));
      }
    }

    @Nested
    class 指定されたTODOが見つからない場合はNotFoundExceptionを投げる {
      @Test
      void 指定されたIDのTODOが見つからない場合() {
        when(todoRepository.findById(anyLong()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.toggleTodo("user", 1L))
            .isInstanceOf(NotFoundException.class);
      }

      @Test
      void 指定されたIDのTODOが別ユーザーのものの場合() {
        when(todoRepository.findById(anyLong()))
            .thenReturn(Optional.of(todo(1L, "a", "user2")));

        assertThatThrownBy(() -> sut.toggleTodo("user", 1L))
            .isInstanceOf(NotFoundException.class);
      }
    }
  }


  private static Todo todo(Long id, String title) {
    Todo todo = new Todo();
    todo.setId(id);
    todo.setTitle(title);
    return todo;
  }

  private static Todo todo(Long id, String title, String username) {
    Todo todo = new Todo();
    todo.setId(id);
    todo.setTitle(title);
    todo.setUsername(username);
    return todo;
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