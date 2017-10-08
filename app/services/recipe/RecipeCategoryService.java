package services.recipe;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import models.recipe.RecipeCategory;
import models.user.User;
import services.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeCategoryService extends Service<RecipeCategory> {

    private static RecipeCategoryService instance;

    private RecipeCategoryService(Finder<Long, RecipeCategory> finder) {
        super(finder);
    }

    public static RecipeCategoryService getInstance() {
        if (instance == null) instance = new RecipeCategoryService(new Finder<>(RecipeCategory.class));
        return instance;
    }

    /**
     * Get category by name.
     * @param name Name of the category.
     * @return RecipeCategory if found. If there's more than one category with name an exception
     *         and null if category was not found.
     */
    public Optional<RecipeCategory> getByName(String name){
        return Optional.ofNullable(getFinder().where().eq("name", name).findUnique());
    }

    public List<RecipeCategory> search(String query) {
        return getFinder().where().like("name", query + "%").findList();
    }

    public List<RecipeCategory> getUnFollowed(String id) {
        String sql = "SELECT c.id FROM RECIPE_CATEGORY c JOIN USER_RECIPE_CATEGORY u ON u.id = r.user_id WHERE r.recipe_category_id = :id";
        RawSql rawSql = RawSqlBuilder.parse(sql).columnMapping("c.id", "id").create();
     return Ebean.find(RecipeCategory.class).setRawSql(rawSql).setParameter("id", id).findList();
    }

    public List<User> getFollowers(long id) {
        Optional<RecipeCategory> category = get(id);

        if (!category.isPresent()) return new ArrayList<>();
        return category.get().getFollowers();

        /*String sql = "SELECT u.id FROM USER u JOIN USER_RECIPE_CATEGORY r ON u.id = r.user_id WHERE r.recipe_category_id = :id";
        RawSql rawSql = RawSqlBuilder.parse(sql)
                .columnMapping("u.id", "id")
                .create();

        List<User> followerIds = Ebean.find(User.class)
                .setRawSql(rawSql)
                .setParameter("id", id)
                .findList();*/
    }
}
