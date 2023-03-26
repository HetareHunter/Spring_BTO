export function overlay() {
  $(function cartInFade() {
    $('.cartButtonSet_').click(function() {
      $('#overlay').fadeIn();
      console.log('fadeIn!');
    });
    $('.book_cartDelete_').click(function() {
      $('#overlay').fadeIn();
      console.log('fadeIn!');
    });
    $('.close').click(function() {
      $('#overlay').fadeOut();
      console.log('fadeOut!');
    });
    // バブリングを停止
    $('.overlay-inner').click(function(event) {
      event.stopPropagation();
      console.log('stopPropagation!');
    });
  });
}