export function checkBoxCount() {
  //ページが開かれたときの処理
  window.onload = function () {
    checkBoxCheck();
  };

  document.getElementById('bookRentalNum').textContent = '合計 0冊の返却';
  // 初期状態のボタンは無効
  $('#rentalCheckButton').prop('disabled', true);
  $('#sumahorentalCheckButton').prop('disabled', true);
  // チェックボックスの状態が変わったら（クリックされたら）
  $("input[type='checkbox']").on('change', function () {
    checkBoxCheck();
  });
}

function checkBoxCheck() {
  // チェックされているチェックボックスの数
  if ($('.checks:checked').length > 0) {
    // ボタン有効
    $('#rentalCheckButton').prop('disabled', false);
    $('#sumahorentalCheckButton').prop('disabled', false);
  } else {
    // ボタン無効
    $('#rentalCheckButton').prop('disabled', true);
    $('#sumahorentalCheckButton').prop('disabled', true);
  }
  //返却する本の数を数えてテキスト出力
  document.getElementById('bookRentalNum').textContent = '合計 ' + $('.checks:checked').length + '冊の返却';
  document.getElementById('sumahoBookRentalNum').textContent = '合計 ' + $('.checks:checked').length + '冊の返却';
}
