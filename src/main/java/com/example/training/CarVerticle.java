package com.example.training;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

import static com.example.training.MainVerticle.ADDRESS;

public class CarVerticle extends AbstractVerticle {
private CarRepo carRepo;
  @Override
  public void start(){
    carRepo =new CarRepo(getPgPool());
    System.out.println("car verticle ");
    vertx.eventBus().consumer(ADDRESS,this::handler);  }

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
  private  void handler(Message<JsonObject> message) {
    JsonObject msg = message.body();
    System.out.println("Received message  : "+msg);
    carRepo.persistMsg(msg).onSuccess(event->{
      System.out.println("Insertion Done Successfully ");
    }).onFailure(event ->{
      System.out.println("Error : "+event);
    });
  }
}
