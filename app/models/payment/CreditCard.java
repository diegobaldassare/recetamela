package models.payment;

import models.BaseModel;

import javax.persistence.Entity;

@Entity
public class CreditCard extends BaseModel {

    private String number;

    private String creditCardType;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }
}
