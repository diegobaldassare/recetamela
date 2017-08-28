package models;

import com.avaje.ebean.Model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseModel<T> extends Model {
    @Id
    public Long id;

    public Finder<Long, T> finder;
}
