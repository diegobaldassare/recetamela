package models.payment;

import models.BaseModel;

import javax.persistence.Entity;

@Entity
public class CreditCard extends BaseModel {

    private String number;

    private CreditCardType creditCardType;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public CreditCardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CreditCardType creditCardType) {
        this.creditCardType = creditCardType;
    }
}
