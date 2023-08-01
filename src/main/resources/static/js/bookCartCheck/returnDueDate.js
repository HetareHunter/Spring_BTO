export function calcReturnDueDate() {
  $(function calcReturnDueDate() {
    var dueDateReturn = new Date();
    dueDateReturn.setDate(dueDateReturn.getDate() + 14);
    var yearNum = dueDateReturn.getFullYear();
    var monthNum = dueDateReturn.getMonth() + 1;
    var dayNum = dueDateReturn.getDate();
    var jpDateStr = String(yearNum) + '年 ' + String(monthNum) + '月 ' + String(dayNum) + '日';
    // スマホ版の画面に返却期限を表示する
    document.getElementById('dueDateReturn_sumaho').textContent = `${jpDateStr}`;
    // webタブレット版の画面に返却期限を表示する
    document.getElementById('dueDateReturn').textContent = `${jpDateStr}`;
  });
}
