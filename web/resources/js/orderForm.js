
$(document).ready(function () {
    $('.input-search-document').on('keypress', function (e) {
        if (e.keyCode === 13) {
            e.preventDefault();
            $('.btn-search-document').click();
        }
    });
     $('.input-search-product').on('keypress', function (e) {
        if (e.keyCode === 13) {
            e.preventDefault();
            $('.btn-search-product').click();
        }
    });
});
        