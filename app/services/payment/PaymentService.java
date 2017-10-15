package services.payment;

import com.avaje.ebean.Model;
import models.payment.Payment;
import services.Service;

import java.util.List;

public class PaymentService extends Service<Payment> {

    private static PaymentService instance;

    private PaymentService(Model.Finder<Long, Payment> finder) {
        super(finder);
    }

    public static PaymentService getInstance() {
        if (instance == null) instance = new PaymentService(new Model.Finder<>(Payment.class));
        return instance;
    }

    public List<Payment> getAllPaymentsWithCreditCard(Long id) {
        return getFinder().where().eq("credit_card_id", id).findList();
    }
}
