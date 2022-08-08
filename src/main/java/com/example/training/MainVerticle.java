package com.example.training;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
//import io.vertx.ext.sql.SQLClient;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;

import java.time.Instant;


public class MainVerticle extends AbstractVerticle {
static final String ADDRESS = "my.request.address";
//private CarRepo carRepo;
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
    vertx.deployVerticle(new CarVerticle());

  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    SqlClient client=dbConfig();
    System.out.println("hello");
    final Router restApi = Router.router(vertx);

    TrainingRestApi.attach(restApi);
    TrainingRestApi.getCars(restApi,client);
    restApi.post("/addCar").handler(BodyHandler.create());
    restApi.post("/addCar").handler(this::addCar);
//    TrainingRestApi.addCar(restApi,client);


//    carRepo =new CarRepo(getPgPool());
//vertx.eventBus().consumer(ADDRESS,this::handler);


    vertx.createHttpServer().requestHandler(
//      req -> {
//      req.response()
//        .putHeader("content-type", "text/plain")
//        .end("Hello from Vert.x!");
//    }
      restApi
    ).exceptionHandler(error -> System.out.println("failed with error : "+error))

      .listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  private void addCar(RoutingContext routingContext) {
    JsonObject reqJo = routingContext.getBodyAsJson();
    System.out.println("routing context : "+routingContext.getBodyAsJson());

  vertx.eventBus().publish("ADDRESS",reqJo);
  }
//  public JsonObject getData() {
//    return new JsonObject()
//      .put("uuid", uuid)
//      .put("temperature", temperature)
//      .put("timestamp", Instant.now().toString());
//
//  }
//  private  void handler(Message<JsonObject> message) {
//    JsonObject msg = message.body();
//    System.out.println("Received message  : "+msg);
//carRepo.persistMsg(msg).onSuccess(event->{
//  System.out.println("Insertion Done Successfully ");
//}).onFailure(event ->{
//  System.out.println("Error : "+event);
//});
//  }

  public PgPool getPgPool(){
    PgConnectOptions connectOptions = new PgConnectOptions()
      .setPort(5432)
      .setHost("localhost")
      .setDatabase("postgres")
      .setUser("postgres")
      .setPassword("user123");

// Pool options
    PoolOptions poolOptions = new PoolOptions()
      .setMaxSize(5);

// Create the client pool
    return PgPool.pool(vertx,connectOptions, poolOptions);
  }
  public SqlClient dbConfig(){
    PgConnectOptions connectOptions = new PgConnectOptions()
      .setPort(5432)
      .setHost("localhost")
      .setDatabase("postgres")
      .setUser("postgres")
      .setPassword("user123");

// Pool options
    PoolOptions poolOptions = new PoolOptions()
      .setMaxSize(5);

// Create the client pool
    return PgPool.client(vertx,connectOptions, poolOptions);

// A simple query
//    client
//      .query("SELECT * FROM cars")
//      .execute(ar -> {
//        if (ar.succeeded()) {
//          RowSet<Row> result = ar.result();
//          System.out.println("Got " + result.size() + " rows ");
//          for (Row row : result) {
//
//            System.out.println("res " + row.getString("name") + " " + row.getString("model")
//            +" "+ row.getString("madein")+ " "+ row.getInteger("id"));
//          }
//        } else {
//          System.out.println("Failure: " + ar.cause().getMessage());
//        }
//
//        // Now close the pool
//        client.close();
//      });
  }
}
