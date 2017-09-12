import models.FreeUser;
import models.User;
import models.Media;
import org.junit.Test;
import play.test.WithApplication;
import services.FreeUserService;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

/**
 * Created by Matias Cicilia on 30-Aug-17.
 */
public class UserTest extends WithApplication {


    @Test
    public void findById() {
        running(fakeApplication(inMemoryDatabase()), () -> {
            User sent = new FreeUser("nombre", "apellido", "mail@mail.com", new Media());
            sent.setId(1L);
            sent.save();
            FreeUserService.getInstance().get(sent.getId()).ifPresent(a -> assertEquals(sent, a));
        });
    }
}
