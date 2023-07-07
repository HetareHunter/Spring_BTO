window.addEventListener('DOMContentLoaded', () => {
  searchForm();
});
$(function searchForm() {
  // $(document).ready(function () {
  //   var windowWidth = $(window).width();
  //   $('#search-form').width(windowWidth);
  // });
  $(window).on('load resize', function () {
    var windowWidth = $(window).width();
    var coefficient = 800; // 元のウィンドウサイズに対する検索ボックスの大きさを調整する値
    var formWidth = windowWidth - coefficient; // ウィンドウサイズと等速で大きさが変わる
    console.log(windowWidth);
    var maxWidth = 600; // 最大幅
    var minWidth = 90; //最小幅

    if (formWidth > maxWidth) {
      formWidth = maxWidth;
    } else if (formWidth < minWidth) {
      formWidth = minWidth;
    }

    $('#search-form').css('width', formWidth + 'px');
  });
});
