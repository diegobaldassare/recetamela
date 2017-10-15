package services.user;

import models.user.User;
import org.apache.commons.lang3.StringUtils;
import server.error.RequestError;
import server.exception.BadRequestException;

import java.util.List;

public class UserValidator {

    public static void validateAllFields(User u) throws BadRequestException {
        validateName(u);
        validateLastName(u);
        validateEmail(u);
    }

    public static void validateNotNullFields(User u) throws BadRequestException {
        if (u.getName() != null) validateName(u);
        if (u.getLastName() != null) validateLastName(u);
        if (u.getEmail() != null) validateEmail(u);
    }

    private static void validateName(User u) throws BadRequestException {
        if (u.getName() == null || !isAlphaNumericSpaceNotEmpty(u.getName()))
            throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateLastName(User u) throws BadRequestException {
        if (u.getName() == null || !isAlphaNumericSpaceNotEmpty(u.getName()))
            throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateEmail(User u) throws BadRequestException {
        if (u.getEmail() == null || u.getEmail().length() < 3 || !u.getEmail().contains("@"))
            throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static boolean isAlphaNumericSpaceNotEmpty(String s) {
        return s.length() != 0 && StringUtils.isAlphanumericSpace(s);
    }

    private static boolean nullOrEmpty(List l) {
        return l == null || l.isEmpty();
    }
}
