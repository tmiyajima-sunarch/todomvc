package jp.co.sunarch.demo.todomvc.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Todo implements Cloneable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String title;
  private boolean completed;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public void toggle() {
    this.completed = !this.completed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Todo todo = (Todo) o;
    return completed == todo.completed && Objects.equals(id, todo.id) && Objects.equals(username, todo.username) && Objects.equals(title, todo.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, title, completed);
  }

  @Override
  public Todo clone() {
    try {
      return (Todo) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "Todo{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", title='" + title + '\'' +
        ", completed=" + completed +
        '}';
  }
}
