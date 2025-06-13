module.exports = {
    root: true,
    env: {
        node: true,
        browser: true,
    },
    extends: [
        // Vue 3 규칙 추가
        "plugin:vue/vue3-recommended",
        "eslint:recommended"
    ],
    parserOptions: {
        parser: "@babel/eslint-parser",
        requireConfigFile: false,
    },
    rules: {
        // 여기에 추가적인 규칙을 정의할 수 있습니다.
    }
};
