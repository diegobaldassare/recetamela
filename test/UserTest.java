import com.avaje.ebean.Ebean;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import models.FreeUser;
import models.Media;
import models.PremiumUser;
import models.User;
import org.junit.Test;
import play.test.WithApplication;
import services.FreeUserService;
import services.PremiumUserService;

import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

/**
 * Created by Matias Cicilia on 30-Aug-17.
 */
public class UserTest extends WithApplication {

    private final Long ID = 52L;

    @Test
    public void testUpgradeFreeUser() {
        test(() -> {
            FreeUser freeUser = new FreeUser("nombre", "apellido", "mail@mail.com", new Media());
            freeUser.setId(ID);
            freeUser.save();
            PremiumUser premiumUser;
            try {
                FreeUser aux = (FreeUser) updateFreeUser();
                aux.save();
                premiumUser = new PremiumUser(aux.getName(), aux.getLastName(), aux.getEmail(), aux.getProfilePic());
                premiumUser.setId(aux.getId());
                premiumUser.setFacebookId(aux.getFacebookId());
                premiumUser.setAuthToken(aux.getAuthToken());
                FreeUserService.getInstance().get(ID).ifPresent(a -> assertEquals("newNombre", a.getName()));
                Ebean.execute(() -> {
                    aux.delete();
                    premiumUser.save();
                });

            } catch (NoSuchObjectException err) {
                fail();
            }
            System.out.println("FreeUser esta presente?" + FreeUserService.getInstance().get(ID).isPresent());
            System.out.println("PremiumUser esta presente?" + PremiumUserService.getInstance().get(ID).isPresent());
            PremiumUserService.getInstance().get(ID).ifPresent(a -> assertEquals("newNombre", a.getName()));
            PremiumUserService.getInstance().get(ID).ifPresent(a -> assertEquals("PremiumUser", a.getType()));
        });
    }

    @Test
    public void testUpdateFreeUser() {
        test(() -> {
            FreeUser freeUser = new FreeUser("nombre", "apellido", "mail@mail.com", new Media());
            freeUser.setId(ID);
            freeUser.save();
            Optional<FreeUser> optFreeUser = FreeUserService.getInstance().get(ID);
            if(optFreeUser.isPresent()) {
                assertEquals(ID, optFreeUser.get().getId());
            } else fail();
            FreeUser newFreeUser;
            try {
                newFreeUser = (FreeUser) updateFreeUser();
                newFreeUser.save();
            } catch (NoSuchObjectException err) {
                fail();
            }
            FreeUserService.getInstance().get(freeUser.getId()).ifPresent(a -> assertEquals("newNombre", a.getName()));
        });
    }

    @Test
    public void testUserType() {
        running(fakeApplication(inMemoryDatabase()), () -> {
            FreeUser sent = new FreeUser("nombre", "apellido", "mail@mail.com", new Media());
            sent.setId(1L);
            sent.save();
            FreeUserService.getInstance().get(1L).ifPresent(a -> System.out.println(a.getType()));
            FreeUserService.getInstance().get(1L).ifPresent(a -> assertEquals("FreeUser", a.getType()));
        });
    }

    @Test
    public void findById() {
        running(fakeApplication(inMemoryDatabase()), () -> {
            FreeUser sent = new FreeUser("nombre", "apellido", "mail@mail.com", new Media());
            sent.setId(1L);
            sent.save();
            FreeUserService.getInstance().get(sent.getId()).ifPresent(a -> assertEquals(sent, a));
        });
    }

    private void test(Runnable runnable) {
        running(fakeApplication(inMemoryDatabase()),runnable);
    }

    private User updateFreeUser() throws NoSuchObjectException {
        FreeUser newFreeUser = new FreeUser("newNombre", "newApellido", "newMail@mail.com", new Media());
        Optional<FreeUser> optionalFreeUser = FreeUserService.getInstance().get(ID);
        FreeUser oldFreeUser;
        if(optionalFreeUser.isPresent()) oldFreeUser = optionalFreeUser.get();
        else throw new NoSuchObjectException("The user was not found");
        if (newFreeUser.getName() != null) oldFreeUser.setName(newFreeUser.getName());
        if (newFreeUser.getLastName() != null) oldFreeUser.setLastName(newFreeUser.getLastName());
        if (newFreeUser.getEmail() != null) oldFreeUser.setEmail(newFreeUser.getEmail());
        if (newFreeUser.getProfilePic() != null) oldFreeUser.setProfilePic(newFreeUser.getProfilePic());
        if (newFreeUser.getAuthToken() != null) oldFreeUser.setAuthToken(newFreeUser.getAuthToken());
        oldFreeUser.setFacebookId(newFreeUser.getFacebookId());
        return oldFreeUser;
    }
}
