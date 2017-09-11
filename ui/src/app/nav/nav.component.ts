import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import { CreditCardValidator } from 'ngx-credit-cards';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

  creditCardForm: FormGroup;
  isPassword = "password";

  constructor() { }

  ngOnInit() {
    this.creditCardForm = new FormGroup({
      'cardName': new FormControl(null, [Validators.required]),
      //'cardNumber': new FormControl(null, [CreditCardValidator.validateCar]),
      'cardNumber': new FormControl(null, [Validators.required, Validators.minLength(3), Validators.maxLength(4)]),
      'cardCode': new FormControl(null, [Validators.required, Validators.minLength(3), Validators.maxLength(4)]),
      'cardDate': new FormControl(null, [Validators.required, Validators.minLength(4), Validators.maxLength(4)]),
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
    //return new Card(cardName, cardNumber, cardCode);
  }
}
