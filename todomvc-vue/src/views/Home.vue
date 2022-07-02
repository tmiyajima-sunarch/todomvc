<template>
  <div class="home">
    <nav class="navbar navbar-light bg-light">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">Todo MVC Vue sample</a>
      </div>
    </nav>
    <div class="container">
      <h1 class="my-3">Todo</h1>
      <form class="my-3" @submit.prevent="dispatchAddTodo">
        <div class="input-group">
          <input type="text" class="form-control" name="title" v-model="newTodo" aria-describedby="button-add" />
          <button type="submit" class="btn btn-primary" id="button-add">Add</button>
        </div>
      </form>
      <ul class="list-group my-3">
        <li
          class="list-group-item todo"
          v-for="todo in filteredTodos"
          :key="todo.id"
          :class="{ 'is-completed': todo.completed }"
          @click="dispatchToggleTodo(todo)"
        >
          {{ todo.title }}
        </li>
      </ul>
      <ul class="nav nav-pills">
        <li class="nav-item">
          <a class="nav-link" :class="{ active: filter === 'NONE' }" @click="showAll">
            All todo
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" :class="{ active: filter === 'NOT_COMPLETED_ONLY' }" @click="showNotCompletedOnly">
            Not completed
          </a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";

export default {
  data() {
    return {
      newTodo: "",
    };
  },

  mounted() {
    this.loadTodos();
  },

  computed: {
    ...mapGetters(["filteredTodos", "filter"]),
  },

  methods: {
    ...mapActions([
      "loadTodos",
      "addTodo",
      "toggleTodo",
      "showAll",
      "showNotCompletedOnly",
    ]),

    dispatchAddTodo() {
      this.addTodo(this.newTodo);
      this.newTodo = "";
    },

    dispatchToggleTodo(todo) {
      this.toggleTodo(todo.id);
    },
  },
};
</script>

<style scoped>
.todo {
  cursor: pointer;
}
.is-completed {
  text-decoration: line-through;
  color: #ccc;
}
</style>
