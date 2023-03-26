import {cartOut} from './bookCartDelete.js';
import {overlay} from './overlay.js';

export function cartIn() {
  $(function cartIn() {
    $('.cartButtonSet_').click(function(e) {
      console.log('book_cart_ ');
      // e.preventDefault();  //有効にするとページの部分更新もできなくなる
      var param = $(this).attr('name');
      var lendable = $('#debug' + param).attr('name');
      $.ajax({
         url: '/bookIndex_setLending',
         type: 'GET',
         dataType: 'html',
         timeout: 10000,  // タイムアウト時間の指定
         data: {
           param: param,
           _csrf: $('*[name=_csrf]').val()  // CSRFトークンを送信
         }
       })
          .done(function(data) {
            console.log('data : ' + data);
            console.log('param : ' + param);
            console.log('lendable : ' + lendable);
            lendable = $('#debug' + param).attr('name');
            console.log('更新後lendable : ' + lendable);
            $('#ajaxReload').html(data);
            cartIn();
            cartOut();
            overlay();
          })
          .fail(function(data) {
            alert('book_cart error!' + data);
          })
    });
  });
}