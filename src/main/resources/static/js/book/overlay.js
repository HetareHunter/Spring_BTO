export function overlay() {
  $(function cartInFade() {
    $('.cartButtonSet_').click(function() {
      $('#overlay').fadeIn(300);
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

export function spinnerFadeIn() {
  $(function spinnerFadeIn() {
    $(document).ajaxSend(function() {
      $('#overlay').fadeIn(300);
    });
  })
}

export function spinnerFadeOut() {
  $(function spinnerFadeOut() {
    setTimeout(function() {
      $('#overlay').fadeOut(300);
    }, 400);
  });
}