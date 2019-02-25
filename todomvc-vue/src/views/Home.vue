<template>
  <div class="home">
    <b-navbar toggleable="lg" type="light" variant="light">
      <b-navbar-brand href="#">Todo MVC Vue sample</b-navbar-brand>
    </b-navbar>
    <b-container>
      <h1 class="my-3">Todo</h1>
      <b-form class="my-3" @submit.prevent="dispatchAddTodo">
        <b-input-group>
          <b-form-input name="title" v-model="newTodo" />
          <b-input-group-append>
            <b-button type="submit">Add</b-button>
          </b-input-group-append>
        </b-input-group>
      </b-form>
      <b-list-group class="my-3">
        <b-list-group-item
          v-for="todo in filteredTodos"
          :key="todo.id"
          class="todo"
          :class="{ 'is-completed': todo.completed }"
          @click="dispatchToggleTodo(todo)"
        >
          {{ todo.title }}
        </b-list-group-item>
      </b-list-group>
      <b-nav pills>
        <b-nav-item :active="filter === 'NONE'" @click="showAll">
          All todo
        </b-nav-item>
        <b-nav-item
          :active="filter === 'NOT_COMPLETED_ONLY'"
          @click="showNotCompletedOnly"
        >
          Not completed
        </b-nav-item>
      </b-nav>
    </b-container>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";

export default {
  data() {
    return {
      newTodo: ""
    };
  },

  mounted() {
    this.loadTodos();
  },

  computed: {
    ...mapGetters(["filteredTodos", "filter"])
  },

  methods: {
    ...mapActions([
      "loadTodos",
      "addTodo",
      "toggleTodo",
      "showAll",
      "showNotCompletedOnly"
    ]),

    dispatchAddTodo() {
      this.addTodo(this.newTodo);
      this.newTodo = "";
    },

    dispatchToggleTodo(todo) {
      this.toggleTodo(todo.id);
    }
  }
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
