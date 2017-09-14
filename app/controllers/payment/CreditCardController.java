package controllers.payment;

import com.google.inject.Inject;
import models.payment.CreditCard;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.payment.CreditCardService;

import java.util.Optional;

public class CreditCardController extends Controller {

    private static Form<CreditCard> creditCardForm;

    @Inject
    public CreditCardController(FormFactory formFactory) {
        creditCardForm = formFactory.form(CreditCard.class);
    }

    public Result create() {
        CreditCard creditCard = creditCardForm.bindFromRequest().get();
        creditCard.save();
        return ok(Json.toJson(creditCard));
    }

    public Result update() {
        CreditCard newCreditCard = creditCardForm.bindFromRequest().get();
        Optional<CreditCard> creditCardOptional = CreditCardService.getInstance().get(newCreditCard.getId());
        if (!creditCardOptional.isPresent()) return notFound();
        CreditCard oldCreditCard = creditCardOptional.get();
        oldCreditCard.setNumber(newCreditCard.getNumber());
        oldCreditCard.setCreditCardType(newCreditCard.getCreditCardType());
        oldCreditCard.update();
        return ok(Json.toJson(oldCreditCard));
    }

    public Result delete(Long id) {
        Optional<CreditCard> creditCardOptional = CreditCardService.getInstance().get(id);
        if (creditCardOptional.isPresent()) {
            creditCardOptional.get().delete();
            return ok();
        }
        return notFound();
    }

    public Result get(Long id) {
        Optional<CreditCard> creditCardOptional = CreditCardService.getInstance().get(id);
        return creditCardOptional.map(c -> ok(Json.toJson(c))).orElseGet(Results::notFound);
    }
}
