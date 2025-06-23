module.exports = {
    devServer: {
        port: 8082,
        proxy: {
            '^/ws': {                       // WebSocket 엔드포인트 prefix
                target: 'ws://localhost:8080',
                ws: true,                    // 💡 필수: WebSocket 프록시 on
                changeOrigin: true,
                logLevel: 'debug'
            },
        }
    }
};