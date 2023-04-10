import {cartIn} from './bookCartIn.js';
import {spinnerFadeIn} from './overlay.js';
import {spinnerFadeOut} from './overlay.js';

export function cartOut() {
  $(function cartOut() {
    spinnerFadeIn();

    $('.book_cartDelete_').click(function(e) {
      console.log('book_cartDelete_');
      e.preventDefault();
      var bookId = $(this).attr('name');
      var searchStr = $('#searchStr').attr('name');
      $.ajax({
         url: '/bookIndex_deleteLending',
         type: 'GET',
         dataType: 'html',
         timeout: 10000,  // タイムアウト時間の指定
         data: {
           bookId: bookId,
           searchStr: searchStr,
           _csrf: $('*[name=_csrf]').val()  // CSRFトークンを送信
         }
       })
          .done(function(data) {
            console.log('削除成功');
            // console.log('data : ' + bookId);
            $('#ajaxReload').html(data);
            cartIn();
            cartOut();
            spinnerFadeOut();
          })
          .fail(function() {
            alert('book_cartDelete error!');
            spinnerFadeOut();
          })
    });
  });
}