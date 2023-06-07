
document.addEventListener("DOMContentLoaded", function () {
$('.bxslider').bxSlider({
      mode: 'horizontal',// 가로 방향 수평 슬라이드
            speed: 500,        // 이동 속도를 설정
            pager: true,      // 현재 위치 페이징 표시 여부 설정
            moveSlides: 1,     // 슬라이드 이동시 개수
            touchEnabled :true, // 슬라이드 터치전환
            slideMargin: 5,    // 슬라이드간의 간격
            adaptiveHeight:true, //슬라이드 높이 자동조정
            responsive:true, // 슬라이드 넓이 자동조정
            auto: true,        // 자동 실행 여부
            autoHover: true,   // 마우스 호버시 정지 여부
            controls: false    // 이전 다음 버튼 노출 여부
    });
    fetch('/members/username', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        credentials: 'same-origin',
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Not logged in');
            }
        })
        .then(member => {
            if (member.role === "USER") {
                handleUser(member);
            } else if (member.role === "ADMIN") {
                handleAdmin(member);
            }
        })
        .catch(error => {
            // 로그인되지 않은 사용자에 대한 처리를 여기서 수행
        });
});

function handleUser(user) {
    const userForm = document.getElementById('user-form');
    const dropdownButton = userForm.querySelector('#dropdownMenuButton');
    dropdownButton.textContent = user.name + " " + '사용자님';

    const myPageLink = userForm.querySelector('a:nth-child(1)');
    const cartLink = userForm.querySelector('a:nth-child(2)');
    const orderHistoryLink = userForm.querySelector('a:nth-child(3)');

    myPageLink.href = `/user/${user.id}`;
    cartLink.href = `/user/cart/${user.id}`;
    orderHistoryLink.href = `/user/orderHist/${user.id}`;
}

function handleAdmin(admin) {
    const adminForm = document.getElementById('admin-form');
    const dropdownButton = adminForm.querySelector('#dropdownMenuButton');
    dropdownButton.textContent = admin.name + " " + '판매자님';

    const myPageLink = adminForm.querySelector('a:nth-child(1)');
    const cartLink = adminForm.querySelector('a:nth-child(2)');
    const orderHistoryLink = adminForm.querySelector('a:nth-child(3)');

    myPageLink.href = `/admin/${admin.id}`;
    cartLink.href = `/admin/cart/${admin.id}`;
    orderHistoryLink.href = `/admin/orderHist/${admin.id}`;
}