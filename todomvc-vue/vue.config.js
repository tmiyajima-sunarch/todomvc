module.exports = {
  devServer: {
    port: 4000,
    proxy: {
      "^/api/": {
        target: "http://localhost:8080",
        onProxyReq(proxyReq) {
          proxyReq.setHeader("Authorization", `Basic dXNlcjp1c2Vy`); // user:user
        }
      }
    }
  }
};
