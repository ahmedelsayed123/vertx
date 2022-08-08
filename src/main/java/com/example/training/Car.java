package com.example.training;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Car {

  private Integer id;
  private String name;
  private String madein;
  private String model;
}
