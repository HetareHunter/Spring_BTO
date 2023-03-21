

$(function() {
  $('button[name]').on('click', function(e) {
    var id = $(this).attr('name');
    console.log(id);
    $('#book_cart_' + id).on('click', 'button', function(e) {
      e.preventDefault();
      var param = $(this).attr('name');
      alert(param);
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
          .done(function() {
            //$(".notes").append(`<div>${data}</div>`);
            //$("#bookId").val("");
            // var divId = document.getElementById("bookId").dataset.id;
            // alert(param);
          })
          .fail(function() {
            alert('book_cart error!');
          })
    });

    $('#book_cartDelete_' + id).on('click', 'button', function(e) {
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
            alert(param);
          })
          .fail(function() {
            alert('book_cartDelete error!');
          })
    });
  });
});
