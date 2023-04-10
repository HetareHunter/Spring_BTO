import {spinnerFadeIn} from './overlay.js';
import {spinnerFadeOut} from './overlay.js';

export function cartOut() {
  $(function cartOut() {
    spinnerFadeIn();
    $(document).on('click', '.book_cartDelete_', function(e) {
      console.log('book_cartDelete_');
      e.preventDefault();
      var bookId = $(this).attr('name');
      var searchStr = $('#searchTableStr').attr('name');
      console.log('cartOut searchTableStr : ' + searchStr);
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
            console.log('cartOut後searchTableStr : ' + searchStr);
            // cartIn();
            // cartOut();
            spinnerFadeOut();
          })
          .fail(function() {
            alert('book_cartDelete error!');
            spinnerFadeOut();
          })
    });
  });
}