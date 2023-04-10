import {spinnerFadeIn} from './overlay.js';
import {spinnerFadeOut} from './overlay.js';

export function bookSerach() {
  $(function bookSerach() {
    spinnerFadeIn();

    $('.bookSearch').click(function(e) {
      console.log('bookSerach');
      e.preventDefault();
      var searchStr = $('#bookSearch').val();
      $.ajax({
         url: '/bookIndex_setSearch',
         type: 'GET',
         dataType: 'html',
         timeout: 10000,  // タイムアウト時間の指定
         data: {
           searchStr: searchStr,
           _csrf: $('*[name=_csrf]').val()  // CSRFトークンを送信
         }
       })
          .done(function(data) {
            // console.log('data : ' + data);
            console.log('searchStr : ' + searchStr);
            $('#ajaxReload').html(data);
            spinnerFadeOut();
          })
          .fail(function(data) {
            alert('book_search error!' + data);
            // console.log('data : ' + data);
            console.log('searchStr : ' + searchStr);
            spinnerFadeOut();
          })
    });
  });
}