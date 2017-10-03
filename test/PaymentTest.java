import models.payment.CreditCard;
import models.user.FreeUser;
import org.junit.Test;
import play.test.WithApplication;
import services.payment.CreditCardService;

import java.util.Date;
import java.util.List;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class PaymentTest extends WithApplication {

    @Test
    public void testPayment() {
        running(fakeApplication(inMemoryDatabase()), () -> {
            FreeUser f1 = new FreeUser("nozxmbre", "apelzxzxlido", "mailxcx@mail.com", "");
            f1.save();

            FreeUser f2 = new FreeUser("nombre", "apellido", "mail@mail.com", "");
            f2.save();


            CreditCard c1 = new CreditCard();
            c1.setCode("123");
            c1.setCreditCardType("visa");
            c1.setExpirationDate(new Date());
            c1.setNumber("123556");
            c1.setOwnerName("PONCIO");
            c1.setUser(f1);
            c1.save();

            CreditCard c2 = new CreditCard();
            c2.setCode("897");
            c2.setCreditCardType("masterd");
            c2.setExpirationDate(new Date());
            c2.setNumber("7895646213");
            c2.setOwnerName("PONCIO");
            c2.setUser(f1);
            c2.save();

            final List<CreditCard> creditCardIds = CreditCardService.getInstance().getFinder().query()
                    .select("number")
                    .where()
                    .eq("user", f2)
                    .findList();
            creditCardIds.forEach(c -> System.out.println(c.getCode()));
        });
    }
}
