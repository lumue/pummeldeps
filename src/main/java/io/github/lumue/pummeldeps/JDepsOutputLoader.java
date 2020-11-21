package io.github.lumue.pummeldeps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.spi.ToolProvider;

public interface JDepsOutputLoader {

  public List<String> loadJdepsLines();



  public static class JDepsToolAdapter implements JDepsOutputLoader{

    private String inputLocation;
    private List<String> options;

    public List<String> loadJdepsLines() {
      ToolProvider jdeps = ToolProvider.findFirst("jdeps").get();

      return Collections.emptyList();
    }

  }

  public static class JDepsOutputFileAdapter implements JDepsOutputLoader{

    private final String inputLocation;

    public JDepsOutputFileAdapter(final String jdepsFile) {
      inputLocation=jdepsFile;
    }

    public List<String> loadJdepsLines() {
      try {
        return Files.readAllLines(Paths.get(inputLocation));
      } catch (IOException e) {
        throw new RuntimeException("error loading jdeps output from "+inputLocation);
      }
    }

    public static Optional<DependencyModel.Dependency> ofLine(String line) {
      if (!line.contains(" -> "))
        return Optional.empty();

      final String[] split = line.split(" -> ");

      if(!(split.length>1))
        return Optional.empty();

      String from = split[0].strip();
      String to = split[1].replace(" main ","").strip();

      if(from.isBlank()|| to.isBlank())
        return Optional.empty();

      if(from.equals("main")  && to.contains("/"))
        return Optional.empty();

      return Optional.of(new DependencyModel.Dependency(from, to));

    }
  }
}