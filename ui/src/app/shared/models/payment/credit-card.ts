import {User} from "../user-model";

export class CreditCard {
  public id: string;
  public user: User;
  public ownerName: string = '';
  public number: string = '';
  public code: string = '';
  public expirationDate: Date;
  public creditCardType: string = '';


  constructor(number: string, creditcardtype: string, owner: string, code: string, expirationDate: Date) {
    this.ownerName = owner;
    this.number = number;
    this.code = code;
    this.expirationDate = expirationDate;
    this.creditCardType = creditcardtype;
  }
}
