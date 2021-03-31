package io.github.paulushcgcj.mentorship.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class MentorshipTestUtils {

  public static final ObjectMapper mapper = getMapper();

  private static ObjectMapper getMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.findAndRegisterModules();
    return mapper;
  }

  @SneakyThrows
  public static String loadContent(String fileName){
    File content = loadFile(fileName);
    if(!content.exists())
      throw new FileNotFoundException("File " + fileName + " was not found");
    return String.join(StringUtils.EMPTY,Files.readAllLines(content.toPath()));
  }

  public static <T> T loadClass(String fileName, Class clazz) {
    return loadMock(fileName, null, clazz);
  }

  public static <T> List<T> loadList(String fileName, Class clazz) {
    return loadMock(fileName, List.class, clazz);
  }


  @SneakyThrows
  private static <T> T loadMock(String fileName, Class parentClass, Class childClass) {
    try {
      File loadedFile = loadFile(fileName);

      if(!loadedFile.exists())
        throw new FileNotFoundException("File " + fileName + " was not found");

      if (parentClass == null)
        return (T) mapper.readValue(loadedFile, childClass);
      return mapper.readValue(loadedFile, mapper.getTypeFactory().constructParametricType(parentClass, childClass));

    } catch (IOException e) {
      log.error("Error while trying to load {} with classes as {} ({})",fileName,childClass,parentClass,e);
    }
    return null;
  }

  private static File loadFile(String fileName) {
    return Paths
      .get("./mocks/__files/" + fileName)
      .normalize()
      .toFile();
  }
}
