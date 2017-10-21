package controllers.payment;

import com.google.inject.Inject;
import controllers.BaseController;
import controllers.authentication.Authenticate;
import models.user.*;
import models.payment.CreditCard;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Result;
import services.payment.CreditCardService;
import services.user.UserService;

import java.util.List;
import java.util.Optional;

public class CreditCardController extends BaseController {

    private static Form<CreditCard> creditCardForm;

    @Inject
    public CreditCardController(FormFactory formFactory) {
        creditCardForm = formFactory.form(CreditCard.class);
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result create() {
        final Optional<User> userOptional = UserService.getInstance().get(getRequester().getId());
        return userOptional.map(user -> {
            final CreditCard creditCard = creditCardForm.bindFromRequest().get();
            final String number = creditCard.getNumber();
            creditCard.setNumber(number.substring(number.length() - 4));
            creditCard.setUser(user);
            creditCard.save();
            return ok(Json.toJson(creditCard));
        }).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result update(Long id) {
        final CreditCard newCreditCard = creditCardForm.bindFromRequest().get();
        final String number = newCreditCard.getNumber();
        final Optional<CreditCard> creditCardOptional = CreditCardService.getInstance().get(id);
        return creditCardOptional.map(creditCard -> {
            creditCard.setNumber(number.substring(number.length() - 4));
            creditCard.setCreditCardType(newCreditCard.getCreditCardType());
            creditCard.setExpirationDate(newCreditCard.getExpirationDate().toDate());
            creditCard.setCode(newCreditCard.getCode());
            creditCard.setOwnerName(newCreditCard.getOwnerName());
            creditCard.update();
            return ok(Json.toJson(creditCard));
        }).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result delete(Long id) {
        final Optional<CreditCard> creditCardOptional = CreditCardService.getInstance().get(id);
        return creditCardOptional.map(creditCard -> {
            creditCard.delete();
            return ok();
        }).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result get(Long id) {
        final Optional<CreditCard> creditCardOptional = CreditCardService.getInstance().get(id);
        return creditCardOptional.map(c -> ok(Json.toJson(c))).orElse(notFound());
    }

    @Authenticate({FreeUser.class, PremiumUser.class, ChefUser.class, AdminUser.class})
    public Result getUserCreditCards() {
        final List<CreditCard> creditCards = CreditCardService.getInstance().getFinder().query()
                .where()
                .eq("user", getRequester())
                .findList();
        return ok(Json.toJson(creditCards));
    }
}
