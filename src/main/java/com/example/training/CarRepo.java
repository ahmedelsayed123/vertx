package com.example.training;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import lombok.AllArgsConstructor;


import java.time.OffsetDateTime;
import java.util.List;

@AllArgsConstructor

public class CarRepo {
  private Pool pgPool;
  public Future<RowSet<Row>> persistMsg(JsonObject msg) {
    return pgPool.preparedQuery("INSERT INTO cars (name, model, madein,id) VALUES ($1, $2, $3, $4)")
      .execute(Tuple.tuple(List.of(msg.getString("name"), msg.getString("model"),
        msg.getString("madein"),msg.getInteger("id"))));
  }

}
