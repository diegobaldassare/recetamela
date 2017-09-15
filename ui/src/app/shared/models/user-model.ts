import {CreditCard} from "./credit-card";

export class User {

  public email: string;
  public facebookId: string;
  public id: string;
  public lastName: string;
  public name: string;
  public profilePic: string;
  public type: string;
  public authToken: string;
  public creditCards: CreditCard[]=[];

  public toJson() : any {
    return {
      email: this.email,
      facebookId: this.facebookId,
      id: this.id,
      lastName: this.lastName,
      name: this.lastName,
      profilePic: this.profilePic,
      type: this.type
    }
  }
}
