package io.github.paulushcgcj.mentorship.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.paulushcgcj.mentorship.exceptions.EntryNotFoundException;
import io.github.paulushcgcj.mentorship.models.IdentifiableEntry;
import io.github.paulushcgcj.mentorship.utils.StubbingUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class GenericFileRepository<T extends IdentifiableEntry<T>> {

  @Setter
  protected ObjectMapper mapper;

  private List<T> stubbedData;

  public void setUp(Path stubFile, Class<T> tClass) {
    log.info("Started to load stubbed data");

    Function<Object, T> singleParse = entry -> {
      try {
        T parsedEntry = mapper.readValue(mapper.writeValueAsString(entry), tClass);
        if (StringUtils.isBlank(parsedEntry.getId()))
          return parsedEntry.withId(StubbingUtils.idGen());
        return parsedEntry;
      } catch (Exception e) {
        log.error("Error while parsing {}", entry, e);
      }
      return null;
    };

    if (stubFile.normalize().toFile().exists()) {
      try {
        Object[] readMe = mapper.readValue(stubFile.normalize().toFile(), Object[].class);

        stubbedData =
          Stream
            .of(readMe)
            .map(singleParse)
            .collect(Collectors.toList());

      } catch (Exception e) {
        log.error("Error while loading stub file {}", stubFile, e);
        stubbedData = new ArrayList<>();
      }
    } else {
      log.error("No stub file found with path {}, starting empty", stubFile);
      stubbedData = new ArrayList<>();
    }

    log.info("Loaded {} stubbed entries", stubbedData.size());
  }

  public Mono<List<T>> listEntries(
    final int page,
    final int size,
    Comparator<T> sorter
  ) {
    return
      Mono.just(
        stubbedData
          .stream()
          .sorted(sorter)
          .skip((long) page * size)
          .limit(size)
          .collect(Collectors.toList())
      );
  }

  public Mono<T> findBy(Predicate<T> filter, UnaryOperator<T> process) {
    return
      stubbedData
        .stream()
        .filter(filter)
        .findFirst()
        .map(process)
        .map(Mono::just)
        .orElse(Mono.empty());
  }

  public Mono<T> findById(String id) {
    return
      Mono.justOrEmpty(
        stubbedData
          .stream()
          .filter(stubbedValue -> stubbedValue.getId().equals(id))
          .findFirst()
      ).switchIfEmpty(Mono.error(new EntryNotFoundException(id)));
  }

  public Mono<T> save(T value) {
    if (StringUtils.isNotBlank(value.getId())) {
      stubbedData.removeIf(stubbedValue -> stubbedValue.getId().equals(value.getId()));
      stubbedData.add(value);
      return Mono.just(value);
    } else {
      stubbedData.add(value.withId(StubbingUtils.idGen()));
      return Mono.just(value.withId(StubbingUtils.idGen()));
    }
  }

  public Mono<Void> remove(String id) {
    if (!stubbedData.removeIf(stubbedValue -> stubbedValue.getId().equals(id)))
      return Mono.error(new EntryNotFoundException(id));
    return Mono.empty();
  }

}
