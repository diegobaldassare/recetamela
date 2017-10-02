package services.recipe;

import models.recipe.Recipe;
import org.apache.commons.lang3.StringUtils;
import server.error.RequestError;
import server.exception.BadRequestException;

import java.util.List;

public class RecipeValidator {

    public static void validateAllFields(Recipe r) throws BadRequestException {
        validateName(r);
        validateDescription(r);
        validateCategories(r);
        validateIngredients(r);
        validateImages(r);
        validateDifficulty(r);
        validateSteps(r);
        validateVideoUrl(r);
        validateServings(r);
        validateDuration(r);
    }

    public static void validateNotNullFields(Recipe r) throws BadRequestException {
        if (r.getName() != null) validateName(r);
        if (r.getDescription() != null) validateDescription(r);
        if (r.getCategories() != null) validateCategories(r);
        if (r.getIngredients() != null) validateIngredients(r);
        if (r.getImages() != null) validateImages(r);
        if (r.getDifficulty() != 0) validateDifficulty(r);
        if (r.getSteps() != null) validateSteps(r);
        if (r.getVideoUrl() != null) validateVideoUrl(r);
        if (r.getServings() != 0) validateServings(r);
        if (r.getDuration() != 0) validateDuration(r);
    }

    private static void validateName(Recipe r) throws BadRequestException {
        if (r.getName() == null || !isAlphaNumericSpaceNotEmpty(r.getName()))
            throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateDescription(Recipe r) throws BadRequestException {
        if (r.getDescription() == null || r.getDescription().length() == 0)
            throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateCategories(Recipe r) throws BadRequestException {
        if (nullOrEmpty(r.getCategories()))
            throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateIngredients(Recipe r) throws BadRequestException {
        if (nullOrEmpty(r.getIngredients()))
            throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateImages(Recipe r) throws BadRequestException {
        if (nullOrEmpty(r.getImages()))
            throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateDifficulty(Recipe r) throws BadRequestException {
        if (r.getDifficulty() < 1 || r.getDifficulty() > 5)
            throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateSteps(Recipe r) throws BadRequestException {
        if (nullOrEmpty(r.getSteps()))
            throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateVideoUrl(Recipe r) throws BadRequestException {
        if (r.getVideoUrl() == null || r.getVideoUrl().matches("^(https?://(www\\.)?)?youtube\\.com/watch\\?v=[a-zA-Z0-9_-]+$")) return;
        throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateServings(Recipe r) throws BadRequestException {
        if (r.getServings() < 1) throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static void validateDuration(Recipe r) throws BadRequestException {
        if (r.getDuration() < 2) throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    private static boolean isAlphaNumericSpaceNotEmpty(String s) {
        return s.length() != 0 && StringUtils.isAlphanumericSpace(s);
    }

    private static boolean nullOrEmpty(List l) {
        return l == null || l.isEmpty();
    }
}
