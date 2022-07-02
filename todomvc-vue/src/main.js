import { createApp } from "vue";
import "./plugins/bootstrap-vue";
import "./plugins/axios";
import App from "./App.vue";
import router from "./router";
import store from "./store";

const app = createApp(App);

app.use(store);
app.use(router);

app.mount("#app");
