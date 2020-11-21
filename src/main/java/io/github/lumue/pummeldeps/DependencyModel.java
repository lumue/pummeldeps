package io.github.lumue.pummeldeps;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

public class DependencyModel {

  private final Set<Dependency> dependencies = new HashSet<>();
  private final String rootPackage;

  public DependencyModel(String rootPackage, Collection<Dependency> dependencies) {
    this.rootPackage = rootPackage;
    this.dependencies.addAll(dependencies.stream()
        .filter(dependency -> dependency.from().startsWith(rootPackage))
        .filter(dependency -> dependency.to().startsWith(rootPackage))
        .map(dependency -> new Dependency(dependency.from.replace(rootPackage, ""),
            dependency.to.replace(rootPackage, "")))
        .collect(Collectors.toSet())
    );
  }

  public Collection<Dependency> getDependencies() {
    return dependencies;
  }

  @Nonnull
  public Set<Dependency> findTransitiveTopLevelDependencies() {
    Set<Dependency> ret = new HashSet<>();
    return dependencies.stream()
        .map(Dependency::stripSubpackages)
        .filter(not(Dependency::isSelfReference))
        .collect(Collectors.toSet());
  }





  @Nonnull
  public Set<Dependency> findTransitiveTopLevelDependencyCycles(){
    Set<Dependency> ret=new HashSet<>();
    Set<Dependency> transitiveTopLevelDependencies = findTransitiveTopLevelDependencies();
    return transitiveTopLevelDependencies.stream()
        .filter(d->transitiveTopLevelDependencies.contains(Dependency.reverse(d)))
        .collect(Collectors.toSet());
  }



  public static class Dependency {

    private final String from;
    private final String to;

    public Dependency(String from, String to) {
      this.from = from;
      this.to = to;
    }


    public String from() {
      return from;
    }

    public String to() {
      return to;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      final Dependency that = (Dependency) o;
      return Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
      return Objects.hash(from, to);
    }

    @Override
    public String toString() {
      return String.format("[%s] --> [%s]", from, to);
    }

    static boolean isSelfReference(final Dependency dep) {
      return Objects.equals(dep.from(), dep.to());
    }

    @Nonnull
    static Dependency stripSubpackages(final Dependency dep) {
      String from = stripSubpackagesFromName(dep.from());
      String to = stripSubpackagesFromName(dep.to());
      Dependency resultDep = new Dependency(
          from,
          to
      );
      return resultDep;
    }

    @Nonnull
    private static String stripSubpackagesFromName(final String packageName) {
      if(packageName.contains("."))
        return packageName.substring(0,packageName.indexOf('.'));
      return packageName;
    }

    private static Dependency reverse(final Dependency dep) {
      return new Dependency(
          dep.to,
          dep.from
      );
    }
  }
}

