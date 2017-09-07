package services.recipe;

import models.recipe.RecipeInput;
import org.apache.commons.lang3.StringUtils;
import server.error.RequestError;
import server.exception.BadRequestException;

import java.util.Arrays;
import java.util.List;

public class RecipeFormatService {

    public static void formatInput(final RecipeInput input) throws BadRequestException {
        assertNotNulls(input);
        input.name = formatName(input.name);
        input.description = formatDescription(input.description);
        input.difficulty = formatDifficulty(input.difficulty);
        input.videoUrl = formatVideoUrl(input.videoUrl);
        input.steps = formatSteps(input.steps);
        input.categoryNames = formatCategoryOrIngredientNames(input.categoryNames);
        input.ingredientNames = formatCategoryOrIngredientNames(input.categoryNames);
    }

    private static void assertNotNulls(final RecipeInput input) throws BadRequestException {
        if (input.name == null ||
            input.description == null ||
            input.steps == null ||
            input.categoryNames == null ||
            input.ingredientNames == null
        ) throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    public static String[] formatCategoryOrIngredientNames(String[] names) throws BadRequestException {
        final List<String> nameList = Arrays.asList(names);
        for (int i = 0; i < nameList.size(); i++) {
            nameList.set(i, nameList.get(i).trim().toLowerCase());
            if (alphaNumSpaceNotEmpty(nameList.get(i))) continue;
            nameList.remove(i);
        }
        if (nameList.isEmpty()) throw new BadRequestException(RequestError.BAD_FORMAT);
        return (String[]) nameList.toArray();
    }

    public static String[] formatSteps(String[] steps) throws BadRequestException {
        final List<String> stepList = Arrays.asList(steps);
        for (int i = 0; i < stepList.size(); i++) {
            stepList.set(i, capitalizeFirstCharacter(stepList.get(i)).trim());
            if (stepList.get(i).length() == 0) stepList.remove(i);
        }
        if (stepList.isEmpty()) throw new BadRequestException(RequestError.BAD_FORMAT);
        return (String[]) stepList.toArray();
    }

    public static int formatDifficulty(int d) throws BadRequestException {
        if (d < 1 || d > 5) throw new BadRequestException(RequestError.BAD_FORMAT);
        return d;
    }

    public static String formatVideoUrl(String url) throws BadRequestException {
        if (url == null) return null;
        if (url.length() == 0) return null;
        if (url.matches("^(https?://(www\\.)?)?youtube\\.com/watch\\?v=[a-zA-Z0-9]+$")) return url;
        throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    public static String formatName(String name) throws BadRequestException {
        name = capitalizeFirstCharacter(name).trim();
        if (alphaNumSpaceNotEmpty(name)) return name;
        throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    public static String formatDescription(String desc) throws BadRequestException {
        desc = capitalizeFirstCharacter(desc).trim();
        if (desc.length() == 0) throw new BadRequestException(RequestError.BAD_FORMAT);
        return desc;
    }

    private static boolean alphaNumSpaceNotEmpty(String s) {
        return s.length() != 0 && StringUtils.isAlphanumericSpace(s);
    }

    private static String capitalizeFirstCharacter(String text) {
        if (text.length() < 2) return "" + Character.toUpperCase(text.charAt(0));
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
