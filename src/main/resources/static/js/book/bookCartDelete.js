$(function() {
  $('.book_cartDelete_').click(function(e) {
    console.log('book_cartDelete_ ');
    e.preventDefault();
    var param = $(this).attr('name');
    $.ajax({
       url: '/bookIndex_deleteLending',
       type: 'GET',
       timeout: 10000,  // タイムアウト時間の指定
       data: {
         param: param,
         _csrf: $('*[name=_csrf]').val()  // CSRFトークンを送信
       }
     })
        .done(function() {
          console.log('削除成功');
          console.log('data : ' + param);
          $('#cartButtonSet_' + param).show();
          $('#cartButtonDelete_' + param).hide();
        })
        .fail(function() {
          alert('book_cartDelete error!');
        })
  });
});