export function bookRental() {
  $(function bookRental() {
    $('.cartCheckButton').click(function (e) {
      console.log('bookRental');
      var cartBookNum = document.getElementById('cartLendingListSize').textContent;
      e.preventDefault();
      $.ajax({
        url: '/bookRentalCompleteAjax',
        type: 'GET',
        dataType: 'text',
        timeout: 10000, // タイムアウト時間の指定
        data: {
          _csrf: $('*[name=_csrf]').val(), // CSRFトークンを送信
        },
      })
        .done(function () {
          console.log('bookRental成功');
          document.getElementById('sumahoBookRentalNum').textContent = cartBookNum + '冊の本を借りました';
          document.getElementById('bookRentalNum').textContent = cartBookNum + '冊の本を借りました';
          document.getElementById('sumahoRentalTerm').textContent = '返却期限';
          document.getElementById('rentalTerm').textContent = '返却期限';
          document.getElementById('cartLendingList').textContent = '0';
          document.getElementById('topbarRentalBookNum').textContent = cartBookNum;
          document.getElementById('sumahoCartCheckButton').disabled = true;
          document.getElementById('cartCheckButton').disabled = true;
        })
        .fail(function () {
          console.log('bookRental失敗');
        });
    });
  });
}