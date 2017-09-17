package server.crontask;

import akka.actor.ActorSystem;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class ExpirationDateTask {

    private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;

    @Inject
    public ExpirationDateTask(ActorSystem actorSystem, ExecutionContext executionContext) {
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;

        this.initialize();
    }

    private void initialize() {
        this.actorSystem.scheduler().schedule(
                Duration.create(0, TimeUnit.SECONDS), // initialDelay
                Duration.create(3, TimeUnit.SECONDS), // interval
                () -> System.out.println("Running block of code"),
                this.executionContext
        );
    }
}
