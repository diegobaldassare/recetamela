export class CreditCard {
  id:string = '';
  number: string = '';
  creditCardType: string = '';

  constructor(number: string, creditcardtype: string) {
    this.number = number;
    this.creditCardType = creditcardtype;
  }
}
