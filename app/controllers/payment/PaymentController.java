package controllers.payment;

import com.google.inject.Inject;
import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.payment.CreditCard;
import models.payment.Payment;
import models.user.FreeUser;
import models.user.PremiumUser;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Result;
import services.payment.CreditCardService;
import services.payment.PaymentService;

import java.util.List;
import java.util.Optional;

public class PaymentController extends BaseController {

    private static Form<Payment> paymentBookForm;

    @Inject
    public PaymentController(FormFactory formFactory) {
        paymentBookForm =  formFactory.form(Payment.class);
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result create(Long creditCardId) {
        final Payment payment = paymentBookForm.bindFromRequest().get();
        return CreditCardService.getInstance().get(creditCardId).map(creditCard -> {
            payment.setCreditCard(creditCard);
            payment.save();
            return ok(Json.toJson(payment));
        }).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result update(Long id) {
        final Payment newPayment = paymentBookForm.bindFromRequest().get();
        final Optional<Payment> paymentOptional = PaymentService.getInstance().get(id);
        return paymentOptional.map(payment -> {
            payment.setCreditCard(newPayment.getCreditCard());
            payment.setAmount(newPayment.getAmount());
            payment.setDate(newPayment.getDate());
            payment.setDescription(newPayment.getDescription());
            return ok(Json.toJson(payment));
        }).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result get(Long id) {
        final Optional<Payment> paymentOptional = PaymentService.getInstance().get(id);
        return paymentOptional.map(p -> ok(Json.toJson(p))).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result getUserPayments() {
        final List<CreditCard> creditCards = CreditCardService.getInstance().getFinder().query()
                .where()
                .eq("user", getRequester())
                .findList();
        final List<Payment> payments = PaymentService.getInstance().getFinder().query()
                .where()
                .in("creditCard", creditCards)
                .findList();
        return ok(Json.toJson(payments));
    }

    public Result delete(Long id) {
        final Optional<Payment> paymentOptional = PaymentService.getInstance().get(id);
        return paymentOptional.map(p -> {
            p.delete();
            return ok();
        }).orElse(notFound());
    }
}
