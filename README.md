# Shopping

쇼핑몰 프로젝트입니다.

## 프로젝트 구조

- `src` : Spring Boot 기반 백엔드 소스
- `frontend` : Vue.js 프론트엔드 소스

## 백엔드 실행 방법

```bash
./gradlew bootRun
```

### WebSocket 테스트
서버가 실행 중일 때 다음 스크립트를 브라우저 콘솔에서 실행하면 에코 메시지를 확인할 수 있습니다.

```javascript
const sock = new SockJS('http://localhost:8080/ws');
const client = Stomp.over(sock);
client.connect({}, () => {
  client.subscribe('/topic/echo', msg => console.log(msg.body));
  client.send('/app/echo', {}, 'hello');
});
```

테스트 실행:

```bash
./gradlew test
```

## 프론트엔드 실행 방법

```bash
cd frontend
npm install
npm run serve
```

배포용 빌드:

```bash
npm run build
```
