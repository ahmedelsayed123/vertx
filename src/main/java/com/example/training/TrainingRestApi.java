package com.example.training;

import io.vertx.ext.web.Router;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;

import java.util.ArrayList;
import java.util.List;

public class TrainingRestApi {
  static void attach(Router parent){
    parent.get("/message").handler(new GetMessageHandler());
  }

  public static void getCars(Router parent, SqlClient client) {
    List<Car> cars = new ArrayList<>();
    client
      .query("SELECT * FROM cars")
      .execute(ar -> {
        if (ar.succeeded()) {
         RowSet<Row> result = ar.result();
         System.out.println("Got " + result.size() + " rows ");
          for (Row row : result) {
            Car car = new Car();
car.setId(row.getInteger("id"));
car.setMadein(row.getString("madein"));
car.setModel(row.getString("model"));
car.setName(row.getString("name"));
            System.out.println("res " + row.getString("name") + " " + row.getString("model")
            +" "+ row.getString("madein")+ " "+ row.getInteger("id"));

          cars.add(car);
          }
        } else {
          System.out.println("Failure: " + ar.cause().getMessage());
        }

        // Now close the pool
        client.close();
      });

    parent.get("/cars").handler(new GetCarsHandler(cars));
  }

  public static void addCar(Router parent, SqlClient client) {
  }
}
