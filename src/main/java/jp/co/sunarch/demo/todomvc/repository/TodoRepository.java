package jp.co.sunarch.demo.todomvc.repository;

import jp.co.sunarch.demo.todomvc.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

  List<Todo> findByUsername(String username);
}
