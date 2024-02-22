$(document).ready(function () {
    $.ajax({
        url: '/admin/manage/sellerItemList', // 서버 측 URL
        type: 'GET', success: function (products) {
            // 성공 시, 상품 목록 처리
            console.log(products);
            // TODO: 상품 목록을 페이지에 표시하는 로직 구현
            // 상품 데이터를 테이블에 추가합니다.
            var tableBody = $('#productTable');
            products.forEach(function (product, index) {
                var badgeHtml = '';
                if (product.status === 'SELL') badgeHtml += '<span class="badge badge-sale">SELL</span> ';
                if (product.status === 'NOT_SALE') badgeHtml += '<span class="badge badge-new">NOT_SALE</span> ';
                if (product.status === 'SOLD_OUT') badgeHtml += '<span class="badge badge-hot">SOLD_OUT</span> ';
                if (product.status === 'STOPPED') badgeHtml += '<span class="badge badge-top">STOPPED</span> ';

                var rowHtml = `
                                   <tr>
                                       <td>${index + 1}</td>
                                       <td>
                                           ${product.itemNm}
                                           ${badgeHtml}
                                       </td>
                                       <td>${product.category.cateName}</td>
                                       <td>${product.price}</td>
                                       <td>${product.itemSellStatus}</td>
                                       <td>${product.stockNumber}</td>
                                       <td>${formatDate(product.createdAt)}</td>
                                       <td>${formatDate(product.updatedAt)}</td>
                                       <td>
                                           <button type="button" class="btn btn-primary btn-sm">수정</button>
                                           <button type="button" class="btn btn-danger btn-sm">삭제</button>
                                       </td>
                                   </tr>
                               `;
                console.log(rowHtml);
                tableBody.append(rowHtml);
            });
        }, error: function (request, status, error) {
            // 실패 시, 에러 처리
            console.error('AJAX Error: ', status, error);
        }
    })// 날짜 포맷을 'yyyy-MM-dd' 형태로 변환하는 함수
    function formatDate(dateString) {
        var date = new Date(dateString);
        var year = date.getFullYear();
        var month = ('0' + (date.getMonth() + 1)).slice(-2);
        var day = ('0' + date.getDate()).slice(-2);
        return year + '-' + month + '-' + day;
    }
})
