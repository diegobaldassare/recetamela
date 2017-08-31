package services;

import com.avaje.ebean.Model;
import models.BaseModel;

import java.util.Optional;

/**
 * Abstract Service with basic ABM features.
 * Every model service will extend this class.
 */
public abstract class Service<T extends BaseModel> {

    protected Model.Finder<Long, T> finder;

    protected Service(Model.Finder<Long, T> finder) {
        this.finder = finder;
    }

    public Optional<T> getById(long id) {
        return Optional.ofNullable(finder.select("id").where().eq("id", id).findUnique());
    }
}
