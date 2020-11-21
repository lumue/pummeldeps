package io.github.lumue.pummeldeps;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

public class JdepsOutputParser {

  public static DependencyModel parseJdepsLines(Collection<String> jdepsLines) {
    return new DependencyModel("com.ottogroup.arteria.backend.",
        jdepsLines.stream()
        .map(JdepsOutputParser::parseLine)
        .flatMap(Optional::stream)
        .filter(not(DependencyModel.Dependency::isSelfReference))
        .collect(toList())
    );
  }



  private static Optional<DependencyModel.Dependency> parseLine(String line) {
    if (!line.contains(" -> "))
      return Optional.empty();

    final String[] split = line.split(" -> ");

    if(!(split.length>1))
      return Optional.empty();

    String from = split[0].strip();
    String to = split[1].replace(" main","").strip();

    if(from.isBlank()|| to.isBlank())
      return Optional.empty();

    if(from.equals("main")  && to.contains("/"))
      return Optional.empty();

    return Optional.of(new DependencyModel.Dependency(from, to));

  }
}
