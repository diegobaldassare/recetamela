package controllers.payment;

import com.google.inject.Inject;
import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.user.FreeUser;
import models.user.PremiumUser;
import models.user.User;
import models.payment.CreditCard;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;
import services.payment.CreditCardService;
import services.user.UserService;

import java.util.Optional;

public class CreditCardController extends BaseController {

    private static Form<CreditCard> creditCardForm;

    @Inject
    public CreditCardController(FormFactory formFactory) {
        creditCardForm = formFactory.form(CreditCard.class);
    }

    @Authenticate({FreeUser.class, PremiumUser.class})
    public Result create() {
        User user = UserService.getInstance().getFinder().byId(getRequester().getId());
        CreditCard creditCard = creditCardForm.bindFromRequest().get();
//        user.getCreditCards().add(creditCard);
//        Logger.debug(user.getCreditCards().toString());
        user.update();
        return ok(Json.toJson(creditCard));
    }

    public Result update(Long id) {
        CreditCard newCreditCard = creditCardForm.bindFromRequest().get();
        Optional<CreditCard> creditCardOptional = CreditCardService.getInstance().get(id);
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
