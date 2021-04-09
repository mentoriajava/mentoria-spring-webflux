package io.github.paulushcgcj.mentorship.utils;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StubbingUtils {

  public static final String[] ROLE_READER = {"READER"};
  public static final String[] ROLE_WRITER = ArrayUtils.add(ROLE_READER, "WRITER");
  public static final String[] ROLE_MANAGER = ArrayUtils.add(ROLE_WRITER, "MANAGER");
  public static final String[] ROLE_SUPER = ArrayUtils.add(ROLE_MANAGER, "SUPER");

  public static String idGen() {
    return UUID.randomUUID().toString().replace("-", "").substring(0, 24);
  }

  public static String[] loadMyRoles(String roleName) {
    Map<String, String[]> roleMap =
      Map.of(
        "SUPER", ROLE_SUPER,
        "MANAGER", ROLE_MANAGER,
        "WRITER", ROLE_WRITER,
        "READER", ROLE_READER
      );
    return roleMap.get(roleName);
  }

  public static String[] loadMyAuthorities(String roleName) {
    return
      Stream
        .of(loadMyRoles(roleName))
        .map(role -> "ROLE_" + role)
        .toArray(String[]::new);
  }

}
