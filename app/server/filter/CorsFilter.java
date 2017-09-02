package server.filter;

import akka.stream.Materializer;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class CorsFilter extends Filter {

    @Inject
    public CorsFilter(Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(Function<Http.RequestHeader, CompletionStage<Result>> next, Http.RequestHeader rh) {
        return next.apply(rh).thenApply(result -> result.withHeader("Access-Control-Allow-Origin", "*"));
    }
}
