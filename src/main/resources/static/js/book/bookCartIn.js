$(function() {
  $('.cartButtonSet_').click(function(e) {
    console.log('book_cart_ ');
    e.preventDefault();
    var param = $(this).attr('name');
    console.log(param);
    // var target = $(e.target);
    // var parentRow = target.closest('tr');
    // var bookId = target.attr("name");
    $.ajax({
       url: '/bookIndex_setLending',
       type: 'GET',
       timeout: 10000,  // タイムアウト時間の指定
       data: {
         param: param,
         _csrf: $('*[name=_csrf]').val()  // CSRFトークンを送信
       }
     })
        .done(function(data) {
          console.log('クリック成功' + data);
          console.log('data : ' + param);
          $('#cartButtonSet_' + param).hide();
          $('#cartButtonDelete_' + param).show();
        })
        .fail(function(data) {
          alert('book_cart error!' + data);
        })
  });
});
