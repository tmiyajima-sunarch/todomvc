import { createApp } from "vue";
import App from "./App.vue";
import axios from "./plugins/axios";
import router from "./router";
import store from "./store";
import "bootstrap/dist/css/bootstrap.css";

const app = createApp(App);

app.use(axios);
app.use(store);
app.use(router);

app.mount("#app");
