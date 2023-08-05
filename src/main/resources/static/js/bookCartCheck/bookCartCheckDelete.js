import { spinnerFadeIn } from '../book/overlay.js';
import { spinnerFadeOut } from '../book/overlay.js';
import { calcReturnDueDate } from './returnDueDate.js';

export function cartOut() {
  $(function cartOut() {
    $(document).on('click', '.book_cartDelete_', function (e) {
      spinnerFadeIn();
      console.log('book_cartDelete_');
      e.preventDefault();
      var bookId = $(this).attr('name');
      $.ajax({
        url: '/bookCartCheck_deleteLending',
        type: 'GET',
        dataType: 'html',
        timeout: 10000, // タイムアウト時間の指定
        data: {
          bookId: bookId,
          _csrf: $('*[name=_csrf]').val(), // CSRFトークンを送信
        },
      })
        .done(function (data) {
          console.log('削除成功');
          $('#ajaxReload').html(data);
          calcReturnDueDate();
          spinnerFadeOut();
        })
        .fail(function () {
          alert('book_cartCheckDelete error!');
          spinnerFadeOut();
        });
    });
  });
}
