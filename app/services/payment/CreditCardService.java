package services.payment;

import com.avaje.ebean.Model;
import models.payment.CreditCard;
import services.Service;

import java.util.List;

public class CreditCardService extends Service<CreditCard> {

    private static CreditCardService instance;

    private CreditCardService(Model.Finder<Long, CreditCard> finder) { super(finder); }

    public static CreditCardService getInstance() {
        if (instance == null) instance = new CreditCardService(new Model.Finder<>(CreditCard.class));
        return instance;
    }

    public List<CreditCard> getAllUserCreditCards(Long id) {
        return getFinder().where().eq("user_id", id).findList();
    }
}
