document.addEventListener("DOMContentLoaded", function () {
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