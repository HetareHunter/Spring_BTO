export function bookSearchAnotherPage() {
  $(function bookSearchAnotherPage() {
    $('.bookSearch').click(function (e) {
      console.log('bookSerach');
      e.preventDefault();
      var searchStr = $('#bookSearch').val();
      $.ajax({
        // url: '/bookIndex_setSearchAnotherPage',
        type: 'GET',
        dataType: 'html',
        timeout: 10000, // タイムアウト時間の指定
        data: {
          searchStr: searchStr,
          _csrf: $('*[name=_csrf]').val(), // CSRFトークンを送信
        },
      })
        .done(function (data) {
          console.log('data : ' + data);
          console.log('searchStr : ' + searchStr);
          window.location.href = '/bookIndex_setSearchAnotherPage?searchStr=' + searchStr;
        })
        .fail(function (data) {
          alert('book_search error!' + data);
          console.log('data : ' + data);
          console.log('param : ' + searchStr);
        });
    });
  });
}
