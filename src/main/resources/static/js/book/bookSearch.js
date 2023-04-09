import {spinnerFadeIn} from './overlay.js';
import {spinnerFadeOut} from './overlay.js';

export function bookSerach() {
  $(function bookSerach() {
    spinnerFadeIn();

    $('.bookSearch').click(function(e) {
      console.log('bookSerach');
      e.preventDefault();
      var param = $('#bookSearch').val();
      $.ajax({
         url: '/bookIndex_setSearch',
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
            $('#ajaxReload').html(data);
            spinnerFadeOut();
          })
          .fail(function(data) {
            alert('book_search error!' + data);
            console.log('data : ' + data);
            console.log('param : ' + param);
            spinnerFadeOut();
          })
    });
  });
}