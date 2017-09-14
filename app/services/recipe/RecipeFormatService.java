package services.recipe;

import models.recipe.RecipeInput;
import models.recipe.RecipeStep;
import org.apache.commons.lang3.StringUtils;
import server.error.RequestError;
import server.exception.BadRequestException;

import java.util.Arrays;
import java.util.List;

public class RecipeFormatService {

    public static void formatInput(final RecipeInput input) throws BadRequestException {
        assertNotNull(input);
        input.name = formatName(input.name);
        input.description = formatDescription(input.description);
        input.difficulty = formatDifficulty(input.difficulty);
        input.videoUrl = formatVideoUrl(input.videoUrl);
        input.duration = formatDuration(input.duration);
        input.serves = formatServes(input.serves);
        input.steps = formatSteps(input.steps);
        input.categoryNames = formatCategoryOrIngredientNames(input.categoryNames);
        input.ingredientNames = formatCategoryOrIngredientNames(input.ingredientNames);
    }

    private static void assertNotNull(final RecipeInput input) throws BadRequestException {
        if (input.name == null ||
                input.description == null ||
                input.duration == null ||
                input.serves == null ||
                input.steps == null ||
                input.categoryNames == null ||
                input.ingredientNames == null ||
                input.imageIds == null
        ) throw new BadRequestException(RequestError.BAD_FORMAT);
    }

    public static long[] formatImageIds(long[] imageIds) throws BadRequestException {
        if (imageIds.length == 0 || imageIds.length > 10) throw new BadRequestException(RequestError.BAD_FORMAT);
        return imageIds;
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

    public static List<RecipeStep> formatSteps(List<RecipeStep> steps) throws BadRequestException {
        for (RecipeStep s : steps) {
            s.setDescription(capitalizeFirstCharacter(s.getDescription().trim()));
            if (s.getDescription().length() == 0) steps.remove(s);
        }
        if (steps.isEmpty()) throw new BadRequestException(RequestError.BAD_FORMAT);
        return steps;
    }

    public static int formatDifficulty(int d) throws BadRequestException {
        if (d < 1 || d > 5) throw new BadRequestException(RequestError.BAD_FORMAT);
        return d;
    }

    public static String formatVideoUrl(String url) throws BadRequestException {
        if (url == null) return null;
        if (url.length() == 0) return null;
        if (url.matches("^(https?://(www\\.)?)?youtube\\.com/watch\\?v=[a-zA-Z0-9_]+$")) return url;
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


    public static String formatDuration(String duration) throws BadRequestException {
        duration = capitalizeFirstCharacter(duration).trim();
        if(duration.length() == 0) throw new BadRequestException(RequestError.BAD_FORMAT);
        return duration;
    }

    public static String formatServes(String serves) throws BadRequestException {
        serves = capitalizeFirstCharacter(serves).trim();
        if(serves.length() == 0) throw new BadRequestException(RequestError.BAD_FORMAT);
        return serves;
    }

    private static boolean alphaNumSpaceNotEmpty(String s) {
        return s.length() != 0 && StringUtils.isAlphanumericSpace(s);
    }

    private static String capitalizeFirstCharacter(String text) {
        if (text.length() < 2) return "" + Character.toUpperCase(text.charAt(0));
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
