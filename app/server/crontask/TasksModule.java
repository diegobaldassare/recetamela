package server.crontask;

import com.google.inject.AbstractModule;

public class TasksModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ExpirationDateTask.class).asEagerSingleton();
    }
}
