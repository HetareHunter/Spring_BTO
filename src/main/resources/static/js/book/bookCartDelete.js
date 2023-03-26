import {cartIn} from './bookCartIn.js';
import {spinnerFadeIn} from './overlay.js';
import {spinnerFadeOut} from './overlay.js';

export function cartOut() {
  $(function cartOut() {
    spinnerFadeIn();

    $('.book_cartDelete_').click(function(e) {
      console.log('book_cartDelete_');
      e.preventDefault();
      var param = $(this).attr('name');
      $.ajax({
         url: '/bookIndex_deleteLending',
         type: 'GET',
         dataType: 'html',
         timeout: 10000,  // タイムアウト時間の指定
         data: {
           param: param,
           _csrf: $('*[name=_csrf]').val()  // CSRFトークンを送信
         }
       })
          .done(function(data) {
            console.log('削除成功');
            console.log('data : ' + param);
            $('#ajaxReload').html(data);
            spinnerFadeOut();
            cartIn();
            cartOut();
          })
          .fail(function() {
            alert('book_cartDelete error!');
          })
    });
  });
}