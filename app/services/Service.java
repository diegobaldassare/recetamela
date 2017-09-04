package services;

import com.avaje.ebean.Model;
import models.BaseModel;

import java.util.Optional;

/**
 * Abstract service for a model that must be extended by all services as singletons.
 * They are usually called by controllers.
 */
public abstract class Service<T extends BaseModel> {

    private final Model.Finder<Long, T> finder;

    protected Service(Model.Finder<Long, T> finder) {
        this.finder = finder;
    }

    public Model.Finder<Long, T> getFinder() {
        return finder;
    }

    public Optional<T> get(long id) {
        return Optional.ofNullable(finder.byId(id));
    }
}
