import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

  creditCardForm: FormGroup;

  constructor() { }

  ngOnInit() {
    this.creditCardForm = new FormGroup({
      'cardName': new FormControl(null, [Validators.required]),
      'cardNumber': new FormControl(null, [Validators.required]),
      'cardCode': new FormControl(null, [Validators.required]),
    });
  }

  upgradeToPremium(){
    this.createCreditCardForm();
    /*this.cardService.create(this.createCreditCardForm());*/
  }

  private createCreditCardForm(){
    const cardName = this.creditCardForm.value.cardName;
    const cardNumber = this.creditCardForm.value.cardNumber;
    const cardCode = this.creditCardForm.value.cardCode;

    console.log(cardName);
    console.log(cardNumber);
    console.log(cardCode);
    //return new Card(cardName, cardNumber, cardCode);
  }
}
