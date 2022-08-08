package com.example.training;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class GetMessageHandler implements Handler<RoutingContext> {
  @Override
  public void handle(RoutingContext routingContext) {

    routingContext.response()
      .putHeader("content-type", "text/plain")
      .putHeader("my-header","my-value").
      end("Hello from Vert.x trying to fetch postgres database!");
  }
}
