import vue from '@vitejs/plugin-vue';
import path from 'path';

export default {
  base: './',
  build: {
    outDir: '../target/classes/static'
  },
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
    },
  },
  server: {
    port: 4000,
    proxy: {
      "^/api/": {
        target: "http://localhost:8080",
        onProxyReq(proxyReq) {
          // Set basic auth for developing
          proxyReq.setHeader("Authorization", "Basic dXNlcjp1c2Vy");
        },
      },
    },
  },
};
