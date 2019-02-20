<template>
  <div class="home">
    <b-navbar toggleable="lg" type="light" variant="light">
      <b-navbar-brand href="#">Todo MVC Vue sample</b-navbar-brand>
    </b-navbar>
    <b-container>
      <h1 class="my-3">Todo</h1>
      <b-form class="my-3" @submit.prevent="addTodo">
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
          @click="toggleTodo(todo)"
        >
          {{ todo.title }}
        </b-list-group-item>
      </b-list-group>
      <b-nav pills>
        <b-nav-item :active="showAll" @click="setShowAll(true)">
          All todo
        </b-nav-item>
        <b-nav-item :active="!showAll" @click="setShowAll(false)">
          Not completed
        </b-nav-item>
      </b-nav>
    </b-container>
  </div>
</template>

<script>
// TODO delete
let id = 0;

export default {
  data() {
    return {
      newTodo: "",
      todos: [],
      showAll: true
    };
  },

  computed: {
    filteredTodos() {
      if (this.showAll) {
        return this.todos;
      }
      return this.todos.filter(todo => !todo.completed);
    }
  },

  methods: {
    addTodo() {
      this.todos.push({ id: ++id, title: this.newTodo, completed: false });
      this.newTodo = "";
    },

    toggleTodo(todo) {
      todo.completed = !todo.completed;
    },

    setShowAll(value) {
      this.showAll = value;
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
