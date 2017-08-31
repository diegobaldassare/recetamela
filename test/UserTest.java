import controllers.UserController;
import models.User;
import models.media.Media;
import org.junit.Test;
import play.test.WithApplication;
import services.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
            User sent = new User("nombre", "apellido", "mail@mail.com", new Media());
            sent.setId(1L);
            sent.save();
            UserService.getInstance().getById(sent.getId()).ifPresent(a -> assertEquals(sent, a));
        });
    }
}
