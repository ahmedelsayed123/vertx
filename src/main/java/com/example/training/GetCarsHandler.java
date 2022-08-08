package com.example.training;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class GetCarsHandler implements Handler<RoutingContext> {

  private List<Car> cars;
  public GetCarsHandler(List<Car> cars) {
  this.cars=cars;
  }

  public List<Car> getCars() {
    return cars;
  }

  public void setCars(List<Car> cars) {
    this.cars = cars;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    JsonArray jsonArray = new JsonArray();
    for(int i =0 ; i <getCars().size();i++) {
       jsonArray.add(getCars().get(i));
    }

    routingContext.response()
      .putHeader("content-type", "text/plain")
      .putHeader("my-header","my-value").
      end(jsonArray.toString());
  }
}
