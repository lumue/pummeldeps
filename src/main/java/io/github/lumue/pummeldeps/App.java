package io.github.lumue.pummeldeps;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

public class App {

	public static void main(String[] args) throws IOException {

		final String jdepsFile = "/home/lm/code/osp/arteria/backend/backend-application/doc/deps/dependencies.txt";
		JDepsOutputLoader.JDepsOutputFileAdapter jDepsOutputFileAdapter = new JDepsOutputLoader.JDepsOutputFileAdapter(
				jdepsFile);

		final DependencyModel dependencies = JdepsOutputParser.parseJdepsLines(jDepsOutputFileAdapter.loadJdepsLines());

		final List<String> output = PumlGenerator.createPumlLines(dependencies);

		final Path pumlFile = Paths.get("/home/lm/code/github/pummeldeps/tmp/dependencies.puml");
		Files.write(pumlFile, output, UTF_8);

	}




}
