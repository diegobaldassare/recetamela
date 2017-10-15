package services;

import com.avaje.ebean.Model;
import models.chefrequest.ChefRequest;

public class ChefRequestService extends Service<ChefRequest> {

    private static ChefRequestService instance;

    private ChefRequestService(Model.Finder<Long, ChefRequest> finder) {
        super(finder);
    }

    public static ChefRequestService getInstance() {
        if (instance == null) instance = new ChefRequestService(new Model.Finder<>(ChefRequest.class));
        return instance;
    }
}
