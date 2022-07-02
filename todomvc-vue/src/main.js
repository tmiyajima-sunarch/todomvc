import Vue, { createApp } from "vue";
import "./plugins/bootstrap-vue";
import "./plugins/axios";
import App from "./App.vue";
import router from "./router";
import store from "./store";

Vue.config.productionTip = false;

const app = createApp({
  router,
  store,
  ...App,
});

app.mount("#app");
