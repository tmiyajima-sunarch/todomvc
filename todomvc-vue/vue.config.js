module.exports = {
  publicPath: "./",
  outputDir: "../target/classes/static",
  devServer: {
    port: 4000,
    proxy: {
      "^/api/": {
        target: "http://localhost:8080",
        onProxyReq(proxyReq) {
          // Set basic auth for developing
          proxyReq.setHeader("Authorization", "Basic dXNlcjp1c2Vy");
        }
      }
    }
  }
};
