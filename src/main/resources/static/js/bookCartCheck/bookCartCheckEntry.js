import { calcReturnDueDate } from './returnDueDate.js';
import { bookRental } from './bookRental.js';
import { cartOut } from './bookCartCheckDelete.js';

$(function main() {
  calcReturnDueDate();
  bookRental();
  cartOut();
});
