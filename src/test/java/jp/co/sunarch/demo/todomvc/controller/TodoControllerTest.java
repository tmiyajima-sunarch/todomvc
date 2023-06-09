package jp.co.sunarch.demo.todomvc.controller;

import jp.co.sunarch.demo.todomvc.SecurityConfiguration;
import jp.co.sunarch.demo.todomvc.domain.Todo;
import jp.co.sunarch.demo.todomvc.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
@Import(SecurityConfiguration.class)
class TodoControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  TodoService todoService;

  @Nested
  class ListTodos {

    @Nested
    class 正常系 {
      @Test
      @WithMockUser
      void ログインユーザーに紐づくTODOを取得する() throws Exception {
        mvc.perform(get("/api/todos"));

        verify(todoService).findTodos("user");
      }

      @Nested
      @WithMockUser
      class 取得したTODOを返却する {
        @Test
        void ステータスコードは200() throws Exception {
          mvc.perform(get("/api/todos"))
              .andExpect(status().is(200));
        }

        @Test
        void ContentTypeはapplication_json() throws Exception {
          mvc.perform(get("/api/todos"))
              .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Nested
        @WithMockUser
        class レスポンスボディはTODOの配列 {
          @Test
          void TODOが0件の場合は空の配列() throws Exception {
            when(todoService.findTodos(any()))
                .thenReturn(Collections.singletonList(todo(1L, "OK")));

            mvc.perform(get("/api/todos"))
                .andExpect(content().json("[{id:1}]"));
          }

          @Test
          void TODOが1件の場合は1件の配列() throws Exception {
            when(todoService.findTodos(any()))
                .thenReturn(Collections.singletonList(todo(1L, "OK")));

            mvc.perform(get("/api/todos"))
                .andExpect(content().json("[{id:1}]"));
          }

          @Test
          void TODOが複数件の場合はその件数分の配列() throws Exception {
            when(todoService.findTodos(any()))
                .thenReturn(Arrays.asList(todo(1L, "OK"), todo(2L, "NG")));

            mvc.perform(get("/api/todos"))
                .andExpect(content().json("[{id:1},{id:2}]"));
          }
        }
      }
    }

    @Nested
    class 異常系 {
      @Nested
      @WithAnonymousUser
      class 認証されていない場合はエラーになる {
        @Test
        void ステータスコードは401() throws Exception {
          mvc.perform(get("/api/todos"))
              .andExpect(status().is(401));
        }
      }
    }
  }

  @Nested
  class AddTodo {
    @Nested
    class 正常系 {
      @Test
      @WithMockUser
      void ログインユーザーに紐づくTODOを追加する() throws Exception {
        mvc.perform(post("/api/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"植木に水をあげる\"}"));

        verify(todoService).addTodo("user", "植木に水をあげる");
      }

      @Test
      @WithMockUser
      void 追加したTODOを取得する() throws Exception {
        when(todoService.addTodo(any(), any()))
            .thenReturn(1L);

        mvc.perform(post("/api/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"植木に水をあげる\"}"));

        verify(todoService).findTodo("user", 1L);
      }

      @Nested
      @WithMockUser
      class 追加したTODOを返却する {
        @BeforeEach
        void setUp() {
          when(todoService.findTodo(any(), anyLong()))
              .thenReturn(todo(1L, "植木に水をあげる"));
        }

        @Test
        void ステータスコードは200() throws Exception {
          mvc.perform(post("/api/todos")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"title\":\"植木に水をあげる\"}"))
              .andExpect(status().is(200));
        }

        @Test
        void ContentTypeはapplication_json() throws Exception {
          mvc.perform(post("/api/todos")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"title\":\"植木に水をあげる\"}"))
              .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        void レスポンスボディはTODO() throws Exception {
          mvc.perform(post("/api/todos")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"title\":\"植木に水をあげる\"}"))
              .andExpect(content().json("{id:1, title:植木に水をあげる}"));
        }
      }
    }

    @Nested
    class 異常系 {
      @Nested
      @WithAnonymousUser
      class 認証されていない場合はエラーになる {
        @Test
        void ステータスコードは401() throws Exception {
          mvc.perform(post("/api/todos")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"title\":\"植木に水をあげる\"}"))
              .andExpect(status().is(401));
        }
      }

      @Nested
      @WithMockUser
      class TODOのタイトルが空の場合はエラーになる {
        @Test
        void ステータスコードは400() throws Exception {
          mvc.perform(post("/api/todos")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"title\":\"\"}"))
              .andExpect(status().is(400));
        }
      }
    }
  }

  @Nested
  @WithMockUser
  class DeleteTodo {
    @Nested
    @WithMockUser
    class 正常系 {
      @Test
      void 指定されたTODOを削除する() throws Exception {
        mvc.perform(delete("/api/todos/1"));

        verify(todoService).deleteTodo("user", 1L);
      }

      @Test
      void ステータスコード204を返す() throws Exception {
        mvc.perform(delete("/api/todos/1"))
            .andExpect(status().is(204));
      }
    }

    @Nested
    class 異常系 {
      @Nested
      @WithAnonymousUser
      class 認証されていない場合はエラーになる {
        @Test
        void ステータスコードは401() throws Exception {
          mvc.perform(delete("/api/todos/1"))
              .andExpect(status().is(401));
        }
      }
    }
  }

  @Nested
  @WithMockUser
  class UpdateTodo {
    @Nested
    class 正常系 {
      @Test
      @WithMockUser
      void 指定されたTODOのタイトルを更新する() throws Exception {
        mvc.perform(put("/api/todos/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"がんばる\"}"));

        verify(todoService).updateTodo("user", 1L, "がんばる");
      }

      @Test
      @WithMockUser
      void 更新後のTODOを取得する() throws Exception {
        when(todoService.findTodo(any(), anyLong()))
            .thenReturn(todo(1L, "がんばる"));

        mvc.perform(put("/api/todos/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"がんばる\"}"));

        verify(todoService).findTodo("user", 1L);
      }

      @Nested
      @WithMockUser
      class 更新後のTODOを返却する {
        @BeforeEach
        void setUp() {
          when(todoService.findTodo(any(), anyLong()))
              .thenReturn(todo(1L, "がんばる"));
        }

        @Test
        void ステータスコードは200() throws Exception {
          mvc.perform(put("/api/todos/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"title\":\"がんばる\"}"))
              .andExpect(status().is(200));
        }

        @Test
        void ContentTypeはapplication_json() throws Exception {
          mvc.perform(put("/api/todos/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"title\":\"がんばる\"}"))
              .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        void レスポンスボディはTODO() throws Exception {
          mvc.perform(put("/api/todos/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"title\":\"がんばる\"}"))
              .andExpect(content().json("{id:1, title:がんばる}"));
        }
      }
    }

    @Nested
    class 異常系 {
      @Nested
      @WithAnonymousUser
      class 認証されていない場合はエラーになる {
        @Test
        void ステータスコードは401() throws Exception {
          mvc.perform(put("/api/todos/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"title\":\"がんばる\"}"))
              .andExpect(status().is(401));
        }
      }

      @Nested
      @WithMockUser
      class TODOのタイトルが空の場合はエラーになる {
        @Test
        void ステータスコードは400() throws Exception {
          mvc.perform(put("/api/todos/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"title\":\"\"}"))
              .andExpect(status().is(400));
        }
      }
    }
  }

  @Nested
  @WithMockUser
  class ToggleTodo {
    @Nested
    class 正常系 {
      @Test
      @WithMockUser
      void 指定されたTODOのステータスをトグルする() throws Exception {
        mvc.perform(post("/api/todos/1/_toggle"));

        verify(todoService).toggleTodo("user", 1L);
      }

      @Test
      @WithMockUser
      void 更新後のTODOを取得する() throws Exception {
        mvc.perform(post("/api/todos/1/_toggle"));

        verify(todoService).findTodo("user", 1L);
      }

      @Nested
      @WithMockUser
      class 更新後のTODOを返却する {
        @BeforeEach
        void setUp() {
          when(todoService.findTodo(any(), anyLong()))
              .thenReturn(todo(1L, "がんばる"));
        }

        @Test
        void ステータスコードは200() throws Exception {
          mvc.perform(post("/api/todos/1/_toggle"))
              .andExpect(status().is(200));
        }

        @Test
        void ContentTypeはapplication_json() throws Exception {
          mvc.perform(post("/api/todos/1/_toggle"))
              .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        void レスポンスボディはTODO() throws Exception {
          mvc.perform(post("/api/todos/1/_toggle"))
              .andExpect(content().json("{id:1, title: がんばる}"));
        }
      }
    }

    @Nested
    class 異常系 {
      @Nested
      @WithAnonymousUser
      class 認証されていない場合はエラーになる {
        @Test
        void ステータスコードは401() throws Exception {
          mvc.perform(post("/api/todos/1/_toggle"))
              .andExpect(status().is(401));
        }
      }
    }
  }

  private static Todo todo(Long id, String title) {
    Todo todo = new Todo();
    todo.setId(id);
    todo.setTitle(title);
    return todo;
  }
}