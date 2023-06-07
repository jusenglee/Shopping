    $(document).ready(function() {
        loadItems(0, 10);

        function loadItems(page, size) {
            $.ajax({
                url: "/all",
                type: "GET",
                data: {
                    page: page,
                    size: size
                },
                success: function(response) {
                    displayItems(response.content);
                },
                error: function(xhr, status, error) {
                    alert("상품 정보를 가져오는데 실패했습니다.");
                }
            });
        }

        function displayItems(items) {
            let itemContainer = $(".row-cols-2");
            itemContainer.empty();

            items.forEach(function(item) {
                let itemHtml = `
                <div class="col mb-5">
                    <div class="card h-100">
                        <img class="card-img-top"  src="${window.location.origin + item.imgUrl}" th:alt="${item.itemNm}" alt="..." />
                        <div class="card-body p-4">
                            <div class="text-center">
                                <h5 class="fw-bolder">${item.itemNm}</h5>
                                <p>${item.price} 원</p>
                            </div>
                        </div>
                        <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">
                            <div class="text-center">
                                <a class="btn btn-outline-dark mt-auto" href="/item/view/${item.id}">상세보기</a>
                            </div>
                        </div>
                    </div>
                </div>`;

                itemContainer.append(itemHtml);
            });
        }
    });
