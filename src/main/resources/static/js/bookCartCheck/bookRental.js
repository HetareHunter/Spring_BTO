import { spinnerFadeIn } from '../book/overlay.js';
import { spinnerFadeOut } from '../book/overlay.js';
import { calcReturnDueDate } from './returnDueDate.js';

export function bookRental() {
  $(function bookRental() {
    $('.cartCheckButton').click(function (e) {
      spinnerFadeIn();
      console.log('bookRental');
      // javascriptを外部ファイルにすると/*[[${変数名}]]*/の形でjavaの変数にアクセスできないので
      // htmlファイルにhiddenにしているdivタグを用意してそこから値を取得するようにしている
      var cartBookNum = document.getElementById('cartLendingListSize').textContent;
      e.preventDefault();
      $.ajax({
        url: '/bookRentalCompleteAjax',
        type: 'GET',
        dataType: 'html',
        timeout: 10000, // タイムアウト時間の指定
        data: {
          _csrf: $('*[name=_csrf]').val(), // CSRFトークンを送信
        },
      })
        .done(function (data) {
          console.log('bookRental成功');
          $('#ajaxReload').html(data);
          calcReturnDueDate();
          spinnerFadeOut();
          document.getElementById('sumahoBookRentalNum').textContent = cartBookNum + '冊の本を借りました';
          document.getElementById('bookRentalNum').textContent = cartBookNum + '冊の本を借りました';
          document.getElementById('sumahoRentalTerm').textContent = '返却期限';
          document.getElementById('rentalTerm').textContent = '返却期限';
          // ナビバーのカートの数0に修正する。
          // 借りている本の数はtopbarのインナーjavascriptをbookCartCheckTable.htmlから呼び出して変化させる
          document.getElementById('cartLendingList').textContent = '0';
          document.getElementById('sumahoCartCheckButton').disabled = true;
          document.getElementById('cartCheckButton').disabled = true;
        })
        .fail(function () {
          console.log('bookRental失敗');
        });
    });
  });
}
