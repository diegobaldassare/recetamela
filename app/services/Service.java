package services;

import com.avaje.ebean.Model;
import models.BaseModel;

/**
 * Abstract Service with basic ABM features.
 * Every model service will extend this class.
 */
public abstract class Service<T extends BaseModel> {

    protected Model.Finder<Long, T> finder;

    protected Service(Model.Finder<Long, T> finder) {
        this.finder = finder;
    }

    public T getById(long id) {
        return finder.byId(id);
    }
}
