$('.slider').slick({
  autoplay: true, //自動的に動き出すか。初期値はfalse。
  autoplaySpeed: 5000, //自動で動き出す秒数(ms)
  infinite: true, //スライドをループさせるかどうか。初期値はtrue。
  slidesToShow: 2, //スライドを画面にn枚見せる
  slidesToScroll: 2, //1回のスクロールでn枚の写真を移動して見せる
  prevArrow: '<div class="slick-prev"></div>', //矢印部分PreviewのHTMLを変更
  nextArrow: '<div class="slick-next"></div>', //矢印部分NextのHTMLを変更
  dots: true, //下部ドットナビゲーションの表示
  responsive: [
    {
      breakpoint: 800, //モニターの横幅がnpx以下の見せ方
      settings: {
        slidesToShow: 1, //スライドを画面に2枚見せる
        slidesToScroll: 1, //1回のスクロールで2枚の写真を移動して見せる
      },
    },
    {
      breakpoint: 426, //モニターの横幅がnpx以下の見せ方
      settings: {
        slidesToShow: 1, //スライドを画面に1枚見せる
        slidesToScroll: 1, //1回のスクロールで1枚の写真を移動して見せる
      },
    },
  ],
});
