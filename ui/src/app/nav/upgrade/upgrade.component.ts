import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-upgrade',
  templateUrl: './upgrade.component.html',
  styleUrls: ['./upgrade.component.css']
})
export class UpgradeComponent implements OnInit {

  creditCardForm: FormGroup;
  isPassword = "password";

  constructor() { }

  ngOnInit() {
    this.creditCardForm = new FormGroup({
      'cardName': new FormControl(null, [Validators.required]),
      'cardNumber': new FormControl(null, [Validators.required, Validators.minLength(15), Validators.maxLength(16), isValidNumber]),
      'cardCode': new FormControl(null, [Validators.required, Validators.minLength(3), Validators.maxLength(4), isValidCode(5)]),     //En vez de 5 hay que pasar el numero de la tarjeta
      'cardDate': new FormControl(null, [Validators.required, Validators.minLength(4), Validators.maxLength(4), isValidDate]),
    });
  }

  showPassword(){
    if(this.isPassword == "text"){
      this.isPassword = "password";
    }
    else{
      this.isPassword = "text";
    }
  }

  upgradeToPremium(){
    this.createCreditCardForm();
    /*this.cardService.create(this.createCreditCardForm());*/
  }

  private createCreditCardForm(){
    const cardName = this.creditCardForm.value.cardName;
    const cardNumber = this.creditCardForm.value.cardNumber;
    const cardCode = this.creditCardForm.value.cardCode;
    const cardDate = this.creditCardForm.value.cardDate;

    console.log(cardName);
    console.log(cardNumber);
    console.log(cardCode);
    console.log(cardDate);

    // console.log(this.isValidNumber(cardNumber));
    console.log(this.cardType(cardNumber));
    // console.log(this.isCodeValid(cardCode,this.cardType(cardNumber)));
    // console.log(this.isDateValid(cardDate));
    // //return new Card(cardName, cardNumber, cardCode);
  }


  private cardType(num: number): string{
    if(num.toString().charAt(0)=='4') return "visa";
    if(num.toString().startsWith("5")) return "mastercard";
    if(num.toString().startsWith("34") || num.toString().startsWith("37")) return "amex";
    else return null;
  }

}

function isValidCode(ccNum: number){
  return (c: FormControl) => {
    if(c.value != null){
      if (c.value.toString().length == 3 && (ccNum.toString().startsWith('4',0)||ccNum.toString().startsWith('5',0))){
        return null;
      }
      if (c.value.toString().length == 4 && (ccNum.toString().startsWith('34',0)||ccNum.toString().startsWith('37',0))){
        return null;
      }
      return {notValidCode : true}
    }
    return null;
  }
}

function isValidDate(input: FormControl){
  const date =  input.value;
  if(date!=null){
    const month = +(date.toString().charAt(0));
    const month2 = +(date.toString().charAt(1));
    const year = +(date.toString().charAt(2));

    if(month > 1) return {notValidDate : true};
    if(month == 1){
      if(month2 > 2) return {notValidDate : true};
    }
    if(year < 2) return {notValidDate : true};

    return null;
  }
  return null;
}

function isValidNumber(input: FormControl){
  const num = input.value;
  // console.log(num);
  if(num != null){
    let ccNum = num.toString();
    let sum = 0;
    let alternate = false;
    for (let i = ccNum.length - 1; i >= 0; i--)
    {
      let n = +(ccNum.substring(i, i + 1));
      if (alternate)
      {
        n *= 2;
        if (n > 9)
        {
          n = (n % 10) + 1;
        }
      }
      sum += n;
      alternate = !alternate;
    }
    if(ccNum.startsWith('4',0) ||
      ccNum.toString().startsWith('5',0) ||
      ccNum.toString().startsWith('34',0) ||
      ccNum.toString().startsWith('37',0)){
      return (sum % 10 == 0) ? null : {notValidNumber : true};
    }
    return {notValidNumber : true};
  }
  return null;
}
