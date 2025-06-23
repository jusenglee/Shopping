// vue.config.js
const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
    outputDir: '../src/main/resources/static',

    devServer: {
        port: 8082,          // Vue DevServer
        allowedHosts: 'all',

        proxy: {
            // ─────────── REST API ───────────
            '^/api': {
                target: 'http://localhost:8080',
                changeOrigin: true,
                pathRewrite: { '^/api': '' },
                logLevel: 'debug'
            },

            // ───────── WebSocket 엔드포인트 ─────────
            '^/ws': {
                target: 'ws://localhost:8080', // 💡 WS 프로토콜
                ws: true,                      // 💡 WebSocket 프록시 활성화
                changeOrigin: true,
                logLevel: 'debug'
            }
        }
    }
});
