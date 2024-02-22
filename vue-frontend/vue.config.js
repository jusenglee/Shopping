const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    outputDir: "../src/main/resources/static",
    devServer: {
        port: 8081, // 'prot' 오타를 'port'로 수정하세요
        proxy: 'http://localhost:8080',
        allowedHosts: 'all', // disableHostCheck 대신 사용
    },
});
