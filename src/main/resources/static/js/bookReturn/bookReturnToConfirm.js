export function returnToConfirm() {
  $(function returnToConfirm() {
    $(document).on('click', '.rentalCheckButton', function (e) {
      const checks = document.getElementsByName('checks');
      var lendingIds = [];
      console.log('rentalCheckButton_Clicked');
      for (let i = 0; i < checks.length; i++) {
        if (checks[i].checked) {
          console.log('チェックした本のID：' + checks[i].value);
          lendingIds.push(checks[i].value);
        }
      }
      var url = '/bookReturnConfirm';
      var inputs = '';
      // CSRF（クロスサイトリクエストフォージェリ）保護が有効になっているため、
      // Spring Securityを使用している場合、フォームからのPOSTリクエストにCSRFトークンを含める必要がある
      var csrfToken = document.querySelector('input[name="_csrf"]').value;
      inputs += '<input type="hidden" name="_csrf" value="' + csrfToken + '" />';

      for (let lendingId of lendingIds) {
        inputs += '<input type="hidden" name="checks" value="' + lendingId + '" />';
      }
      // POST遷移
      $('body').append('<form action="' + url + '" method="post" id="post">' + inputs + '</form>');
      $('#post').submit();
      e.preventDefault();
    });
  });
}
