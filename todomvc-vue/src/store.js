import Vue from "vue";
import Vuex from "vuex";
import { axios } from "@/plugins/axios";

Vue.use(Vuex);

const filters = {
  none: "NONE",
  notCompletedOnly: "NOT_COMPLETED_ONLY"
};

export default new Vuex.Store({
  state: {
    todos: [],
    filter: filters.notCompletedOnly
  },

  mutations: {
    loadTodos(state, todos) {
      state.todos = todos;
    },

    addTodo(state, newTodo) {
      state.todos.push(newTodo);
    },

    updateTodo(state, newTodo) {
      const index = state.todos.findIndex(todo => todo.id === newTodo.id);
      if (index === -1) {
        state.todos.push(newTodo);
      } else {
        state.todos.splice(index, 1, newTodo);
      }
    },

    setFilter(state, filter) {
      state.filter = filter;
    }
  },

  actions: {
    async loadTodos({ commit }) {
      const { data } = await axios.get("/api/todos");
      commit("loadTodos", data);
    },

    async addTodo({ commit }, title) {
      const { data: newTodo } = await axios.post("/api/todos", {
        title
      });
      commit("addTodo", newTodo);
    },

    async toggleTodo({ commit }, todoId) {
      const { data: updated } = await axios.post(
        `/api/todos/${todoId}/_toggle`
      );
      commit("updateTodo", updated);
    },

    showAll({ commit }) {
      commit("setFilter", filters.none);
    },

    showNotCompletedOnly({ commit }) {
      commit("setFilter", filters.notCompletedOnly);
    }
  },

  getters: {
    filter(state) {
      return state.filter;
    },

    filteredTodos(state) {
      if (state.filter === filters.notCompletedOnly) {
        return state.todos.filter(todo => !todo.completed);
      }
      return state.todos;
    }
  }
});
