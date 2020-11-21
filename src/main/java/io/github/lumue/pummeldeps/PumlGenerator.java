package io.github.lumue.pummeldeps;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PumlGenerator {
  @Nonnull
  static List<String> createPumlLines(final DependencyModel model) {
    final List<String> output = new ArrayList<>();
    output.add("@startuml");
    model.findTransitiveTopLevelDependencyCycles().forEach(dep-> output.add(createDependencyMarkup(dep)));
    output.add("@enduml");
    return output;
  }

  private static String createDependencyMarkup(DependencyModel.Dependency dep) {
    return String.format("[%s] --> [%s]", dep.from(), dep.to());
  }
}
