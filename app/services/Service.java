package services;

import com.avaje.ebean.Model;
import models.BaseModel;

/**
 * Abstract Service with basics ABM features.
 * Every model service will extend this class.
 */
public class Service<T extends BaseModel> {

    private Model.Finder<Long, T> finder;

    private Service(Model.Finder<Long, T> finder) {
        this.finder = finder;
    }

    public T getById(long id) {
        return finder.byId(id);
    }
}
