package io.github.paulushcgcj.mentorship.utils;

import java.util.UUID;

public class StubbingUtils {
  public static String idGen() {
    return UUID.randomUUID().toString().replace("-", "").substring(0, 24);
  }

  public static String getMethod(String value) {
    return
      "get"
        +
        value
          .substring(0, 1)
          .toUpperCase()
        +
        value
          .substring(1);
  }

}
