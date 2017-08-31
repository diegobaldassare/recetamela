package services;

import com.avaje.ebean.Model;
import models.BaseModel;

/**
 * Abstract service for a model that must be extended by all services as singletons.
 * They are usually called by controllers.
 */
public abstract class Service<T extends BaseModel> {

    protected final Model.Finder<Long, T> finder;

    protected Service(Model.Finder<Long, T> finder) {
        this.finder = finder;
    }

    public T get(long id) {
        return finder.byId(id);
    }
}
