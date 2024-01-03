document.addEventListener("DOMContentLoaded", function () {
    $('.bxslider').bxSlider({
        mode: 'horizontal',// 가로 방향 수평 슬라이드
        speed: 500,        // 이동 속도를 설정
        pager: true,      // 현재 위치 페이징 표시 여부 설정
        moveSlides: 1,     // 슬라이드 이동시 개수
        touchEnabled: true, // 슬라이드 터치전환
        slideMargin: 5,    // 슬라이드간의 간격
        adaptiveHeight: true, //슬라이드 높이 자동조정
        responsive: true, // 슬라이드 넓이 자동조정
        auto: true,        // 자동 실행 여부
        autoHover: true,   // 마우스 호버시 정지 여부
        controls: false    // 이전 다음 버튼 노출 여부
    });
});
$(document).ready(function () {
    $.ajax({
        url: '/memberInfo',
        type: 'GET',
        success: function (data) {
            console.log(data);
            var mberNm = data.name;
            var role = data.role;
            var adminLinks = `
                        <li><a class="dropdown-item" href="/admin/mypage">판매자페이지</a></li>
                        <li><a class="dropdown-item" href="/admin/manage">상품관리</a></li>
                        <li><a class="dropdown-item" href="/admin/salelist">판매내역</a></li>
                        `;
            var userLinks = `
                         <li><a class="dropdown-item" href="#">마이페이지</a></li>
                         <li><a class="dropdown-item" href="#">장바구니</a></li>
                         <li><a class="dropdown-item" href="#">주문내역</a></li>
                        `;
            $('#mberName').text(data.mberNm);
            if(role == 'ADMIN') {
                $('#mberNemu').html(adminLinks);
            }else{
                $('#mberNemu').html(userLinks);
            }
        }
    });
});