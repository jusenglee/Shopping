function loadSelectBox(url, selectId) {
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            var select = $('select[id="' + selectId + '"]');
            select.empty();
            $.each(data, function(key, value) {
                select.append('<option value="' + value.id + '">' + (value.kateName || value.name) + '</option>');
            });
        }
    });
}
$(document).ready(function() {
    loadSelectBox('/categories', 'category');
    loadSelectBox('/brands', 'brand');

    $('form').on('submit', function(event) {
        event.preventDefault();

        var form = $(this);
        var formData = new FormData(this);

        var imageFiles = $('.custom-file-input')[0].files;
        for (var i = 0; i < imageFiles.length; i++) {
            var file = imageFiles[i];
            formData.append('itemImgFile' + i, file);
        }
      $.ajax({
          type: 'POST',
          url: form.attr('action'),
          data: formData,
          contentType: false,
          processData: false,
          success: function(data) {
              console.log('Item has been saved successfully!');
              window.location.href ="/main"
          },
          error: function(e) {
              if (e.responseJSON) {
                  var errors = e.responseJSON;
                  for (var fieldName in errors) {
                      var errorMessage = errors[fieldName];
                      // Show error message to user
                      alert(fieldName + ': ' + errorMessage);
                  }
              }
          }
      });
    });
});