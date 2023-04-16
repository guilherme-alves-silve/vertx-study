package br.com.guilhermealvesilve.vertxstarter.json;

public class Person {
  private Integer id;
  private String name;
  private boolean lovesVertx;

  public Person() {
    //constructor for deserialization of json
  }

  public Person(Integer id, String name, boolean lovesVertx) {
    this.id = id;
    this.name = name;
    this.lovesVertx = lovesVertx;
  }

  public Integer getId() {
    return id;
  }

  public Person setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Person setName(String name) {
    this.name = name;
    return this;
  }

  public boolean isLovesVertx() {
    return lovesVertx;
  }

  public Person setLovesVertx(boolean lovesVertx) {
    this.lovesVertx = lovesVertx;
    return this;
  }
}
