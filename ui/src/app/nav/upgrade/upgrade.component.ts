import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {SharedService} from '../../shared/services/shared.service';
import {CreditCard} from "../../shared/models/payment/credit-card";
import {ToasterService} from "angular2-toaster";
import {UserService} from "../../shared/services/user.service";
import {CreditCardService} from "../../shared/services/credit-card.service";
import {User} from "../../shared/models/user-model";
import {Router} from "@angular/router";
import {PaymentService} from "../../shared/services/payment.service";
import {Payment} from "../../shared/models/payment/payment";

@Component({
  selector: 'app-upgrade',
  templateUrl: './upgrade.component.html',
  styleUrls: ['./upgrade.component.css']
})
export class UpgradeComponent implements OnInit {
  private creditCard: CreditCard;
  private creditCardForm: FormGroup;
  private isPassword = "password";
  public active: boolean;
  private expired: boolean;

  constructor(private sharedService: SharedService,
              private cdRef: ChangeDetectorRef,
              private toaster: ToasterService,
              private _userService: UserService,
              private _creditCardService: CreditCardService,
              private _paymentService: PaymentService,
              private router: Router,
              private fb: FormBuilder) {
    this.sharedService.notifyObservable$.subscribe(res => {
      if (res.hasOwnProperty('upgradeForm') && res.upgradeForm) this.activeUpgrade(res.upgradeForm);
      if (res.hasOwnProperty('expired')) this.notifyExpiredAccount(res.expired);
    });

    this.creditCardForm = fb.group({
      'cardName': new FormControl(null, [Validators.required]),
      'cardNumber': new FormControl(null, [Validators.required, isValidNumber]),
      'cardCode': new FormControl(null, [Validators.required]),
      'cardDate': new FormControl(null, [Validators.required, isValidDate]),
    // }, {validator: isValidCode('cardCode','cardNumber')     //No me esta funcionando bien
    });
  }

  ngOnInit() {}

  private notifyExpiredAccount(value: boolean) : void {
    this.expired = value;
    this.cdRef.detectChanges();
  }

  public activeUpgrade(value: boolean): void {
    this.active = value;
    //this.cdRef.detectChanges();
  }

  public close(): void {
    this.activeUpgrade(false);
  }

  showPassword() {
    if (this.isPassword == "text") {
      this.isPassword = "password";
    }
    else {
      this.isPassword = "text";
    }
  }

  upgradeToPremium() {
    this.createCreditCardForm();
    this._creditCardService.createCreditCard(this.creditCard).then( result => {
        this._paymentService.create(this.createPayment(99.99, "Premium User Upgrade"), result.id).then(() => {
          this._userService.upgradeFreeUser((JSON.parse(localStorage.getItem("user")) as User).id).then( result => {
            localStorage.setItem("user", JSON.stringify(result));
            this.toaster.pop("success", "Pago procesado");
            this.close();
            this.sharedService.notifyOther({isPremium: true});
            this.router.navigate(['/home']);
            this.sharedService.notifyOther({loggedIn: true});
          }, () => { this.toaster.pop("error", "Error de pago"); });
        }, () => { this.toaster.pop("error", "Error de guardado de pago"); });
      }, () => { this.toaster.pop("error", "Error de guardado de tarjeta"); }
    );
  }

  private createCreditCardForm() {
    const cardName = this.creditCardForm.value.cardName;
    const cardNumber = this.creditCardForm.value.cardNumber;
    const cardCode = this.creditCardForm.value.cardCode;
    const cardDate = this.creditCardForm.value.cardDate;

    const datePart = cardDate.split('-');
    this.creditCard = new CreditCard(cardNumber, this.cardType(cardNumber), cardName, cardCode, new Date(Date.UTC(Number(datePart[0]), Number(datePart[1])-1)));
  }

  private cardType(num: number): string {
    if (num.toString().charAt(0) == '4') return "visa";
    if (num.toString().startsWith("5")) return "mastercard";
    if (num.toString().startsWith("34") || num.toString().startsWith("37")) return "amex";
    else return null;
  }

  private createPayment(amount: number, description: string) : Payment {
    const payment = new Payment();
    payment.amount = amount;
    payment.date = new Date();
    payment.description = description;
    return payment;
  }

}
//  No me esta funcionando bien
// function isValidCode(ccCode:string, ccNum: string){
//   return (group: FormGroup): { [key: string]: any }  => {
//     let flag = false;
//     if (ccCode.length == 3 && (ccNum.startsWith('4',0)||ccNum.startsWith('5',0))){
//       flag = true;
//     }
//     if (ccCode.length == 4 && (ccNum.startsWith('34',0)||ccNum.startsWith('37',0))){
//       flag = true;
//     }
//
//     if(flag == false){
//       return {notValidCode : true};
//     }
//   }
// }

function isValidDate(input: FormControl) {
  const expDate = Date.parse(input.value);
  const today = new Date().getTime();

  if(expDate>today) return null;
  else return {notValidDate: true};
}

function isValidNumber(input: FormControl) {
  const num = input.value;
  if (num != null) {
    let ccNum = num.toString();
    let sum = 0;
    let alternate = false;
    for (let i = ccNum.length - 1; i >= 0; i--) {
      let n = +(ccNum.substring(i, i + 1));
      if (alternate) {
        n *= 2;
        if (n > 9) {
          n = (n % 10) + 1;
        }
      }
      sum += n;
      alternate = !alternate;
    }
    if (ccNum.startsWith('4', 0) ||
      ccNum.toString().startsWith('5', 0) ||
      ccNum.toString().startsWith('34', 0) ||
      ccNum.toString().startsWith('37', 0)) {
      return (sum % 10 == 0) ? null : {notValidNumber: true};
    }
    return {notValidNumber: true};
  }
  return null;
}
