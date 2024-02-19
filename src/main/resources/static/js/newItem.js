function loadSelectBox(url, selectId) {
    $.ajax({
        url: url, type: 'GET', success: function (data) {
            var select = $('select[id="' + selectId + '"]');
            select.empty();
            $.each(data, function (key, value) {
                select.append('<option value="' + value.id + '">' + (value.kateName || value.name) + '</option>');
            });
        }
    });
}

$(document).ready(function () {
    loadSelectBox('/categories', 'category');
    loadSelectBox('/brands', 'brand');
    $('#formBtn').click(function (event) {
        event.preventDefault();
        console.log("버튼 클릭됨"); // 여기를 추가
        ajaxSubmitForm();
    });
});

function ajaxSubmitForm() {
    var form = $('#form');
    var formData = new FormData(form[0]);

    var imageFiles = $('.custom-file-input')[0].files;
    for (var i = 0; i < imageFiles.length; i++) {
        formData.append('itemImgFile' + i, imageFiles[i]);
    }

    $.ajax({
        type: 'POST',
        url: form.attr('action'),
        data: formData,
        contentType: false,
        processData: false,
        success: function (data) {
            alert('상품이 등록 되었습니다.');
            window.location.href = "/main";
        },
        error: function (response) {
            var errors = response.responseJSON;
            var errorsHtml = '<div class="alert alert-danger"><ul>';

            $.each(errors, function (key, value) {
                errorsHtml += '<li>' + value + '</li>';
                $('#' + key).addClass('is-invalid'); // 오류가 있는 필드를 강조합니다.
            });

            errorsHtml += '</ul></div>';
            $('#formErrors').html(errorsHtml); // 오류 메시지를 DOM에 추가합니다.
        }
    });
}
