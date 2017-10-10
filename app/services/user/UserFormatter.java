package services.user;

import models.user.User;

public class UserFormatter {

    public static void format(User u) {
        formatName(u);
        formatLastName(u);
    }

    private static void formatName(User u) {
        if (u.getName() == null) return;
        u.setName(capitalizeFirstCharacter(u.getName()).trim());
    }

    private static void formatLastName(User u) {
        if (u.getLastName() == null) return;
        u.setName(capitalizeFirstCharacter(u.getLastName()).trim());
    }

    private static String capitalizeFirstCharacter(String text) {
        if (text.length() < 2) return "" + Character.toUpperCase(text.charAt(0));
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
