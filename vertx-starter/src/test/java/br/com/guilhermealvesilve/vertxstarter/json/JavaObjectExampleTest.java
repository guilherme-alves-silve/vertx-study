package br.com.guilhermealvesilve.vertxstarter.json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class JavaObjectExampleTest {

  @Test
  void shouldEncodeJsonObject() {
    final var jsonObj = new JsonObject()
      .put("id", 1)
      .put("name", "Alice")
      .put("loves_vertx", true);

    assertAll(
      () -> assertEquals("{\"id\":1,\"name\":\"Alice\",\"loves_vertx\":true}", jsonObj.encode()),
      () -> assertEquals(jsonObj, new JsonObject(jsonObj.encode()))
    );
  }

  @Test
  void shouldDecodeJsonObject() {
    var jsonInput = "{\"id\":1,\"name\":\"Alice\",\"loves_vertx\":true}";
    final var jsonObj = new JsonObject(jsonInput);

    assertAll(
      () -> assertEquals(1, jsonObj.getInteger("id")),
      () -> assertEquals("Alice", jsonObj.getString("name")),
      () -> assertTrue(jsonObj.getBoolean("loves_vertx"))
    );
  }

  @Test
  void shouldCreateJsonObjectFromMap() {
    final var map = new HashMap<String, Object>();
    map.put("id", 1);
    map.put("name", "Alice");
    map.put("loves_vertx", true);
    final var jsonObj = new JsonObject(map);
    assertAll(
      () -> assertEquals(1, jsonObj.getInteger("id")),
      () -> assertEquals("Alice", jsonObj.getString("name")),
      () -> assertTrue(jsonObj.getBoolean("loves_vertx")),
      () -> assertEquals(map, jsonObj.getMap())
    );
  }

  @Test
  void shouldMapJsonArray() {
    final var jsonArray = new JsonArray()
      .add(new JsonObject().put("id", 1))
      .add(new JsonObject().put("id", 2))
      .add(new JsonObject().put("id", 3))
      .add("randomValue")
      .add(1);
    assertEquals("[{\"id\":1},{\"id\":2},{\"id\":3},\"randomValue\",1]", jsonArray.encode());
  }

  @Test
  void shouldConvertPersonToJson() {
    var person = new Person(1, "Alice", true);
    var alice = JsonObject.mapFrom(person);
    assertAll(
      () -> assertEquals(person.getId(), alice.getInteger("id")),
      () -> assertEquals(person.getName(), alice.getString("name")),
      () -> assertEquals(person.isLovesVertx(), alice.getBoolean("lovesVertx"))
    );
  }

  @Test
  void shouldJsonToPerson() {
    final var alice = new JsonObject("{\"id\":1,\"name\":\"Alice\",\"lovesVertx\":true}");
    final var person = alice.mapTo(Person.class);
    assertAll(
      () -> assertEquals(person.getId(), alice.getInteger("id")),
      () -> assertEquals(person.getName(), alice.getString("name")),
      () -> assertEquals(person.isLovesVertx(), alice.getBoolean("lovesVertx"))
    );
  }
}
