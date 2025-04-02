import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class TodoFinder {

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage:  java TodoFinder <directory-path>");
            return;
        }

        Path path = Paths.get(args[0]);
        List<String> todos = new ArrayList<>();

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if(Files.isRegularFile(file) && (file.toString().endsWith(".java") || file.toString().endsWith(".cpp") ||
                            file.toString().endsWith(".h") || file.toString().endsWith(".pde"))){
                        List<String> lines = Files.readAllLines(file);
                        for(String line : lines) {
                            line = line.strip();
                            if(line.contains("//") && line.contains("TODO:")) {
                                todos.add(file + ": " + line);
                            } else if(line.contains("/*") && line.contains("TODO:")) {
                                todos.add(file + ": " + line);
                            }
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            Path outputPath = path.resolve("Yet to Do.txt");
            Files.write(outputPath, todos);

            System.out.println("Yet to Do.txt created successfully with " + todos.size() + " items.");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
