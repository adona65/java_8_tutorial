package tutorial_014.variousChanges;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class _04_FilesTest {
	public static void main(String[] args) {
		/*
		 * The utility class Files was first introduced in Java 7 as part of Java NIO. The JDK 8 API adds a couple of additional
		 *  methods which enables us to use functional streams with files.
		 *  
		 *  Listing files : The method Files.list streams all paths for a given directory, so we can use stream operations like filter 
		 *  and sorted upon the contents of the file system.
		 */
		try (Stream<Path> stream = Files.list(Paths.get(""))) {
		    String joined = stream
		        .map(String::valueOf)
		        .filter(path -> !path.startsWith("."))
		        .sorted()
		        .collect(Collectors.joining("; ")) ;
		    System.out.println("List: " + joined);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * The above example lists all files for the current working directory, then maps each path to it's string representation. The result 
		 * is then filtered, sorted and finally joined into a string. The creation of the stream is wrapped into a try with resource statement 
		 * because Streams implement AutoCloseable and in this case we really have to close the stream explicitly since it's backed by IO operations.
		 * 
		 * " The returned stream encapsulates a DirectoryStream. If timely disposal of file system resources is required, the try-with-resources 
		 * construct should be used to ensure that the stream's close method is invoked after the stream operations are completed."
		 */
		
		System.out.println("=====================================");
		
		/*
		 * Finding files : The next example demonstrates how to find files in a directory or it's sub-directories.
		 */
		Path start = Paths.get("");
		int maxDepth = 5;
		try (Stream<Path> stream = Files.find(start, maxDepth, (path, attr) -> String.valueOf(path).endsWith(".java"))) {
		    String joined = stream
		        .sorted()
		        .map(String::valueOf)
		        .collect(Collectors.joining("; "));
		    System.out.println("Found: " + joined);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * The method find() accepts three arguments : The directory path start is the initial starting point and maxDepth defines the maximum 
		 * folder depth to be searched. The third argument is a matching predicate and defines the search logic. In the above example we search 
		 * for all Java files (filename ends with ".java").
		 * 
		 * We can achieve the same behavior by utilizing the method Files.walk. Instead of passing a search predicate this method just walks over 
		 * any file. In the following example we use the stream operation filter to achieve the same behavior as in the previous example.
		 */
		try (Stream<Path> stream = Files.walk(start, maxDepth)) {
		    String joined = stream
		        .map(String::valueOf)
		        .filter(path -> path.endsWith(".java"))
		        .sorted()
		        .collect(Collectors.joining("; "));
		    System.out.println("walk(): " + joined);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("=====================================");
		
		/*
		 * Reading and writing files : Reading text files into memory and writing strings into a text file in Java 8 is finally a simple task. 
		 * No messing around with readers and writers. The method Files.readAllLines reads all lines of a given file into a list of strings. 
		 * You can simply modify this list and write the lines into another file via Files.write:
		 */
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(_04_FilesTest.class.getResource("test.txt").toURI()));
			lines.add("print('foobar');");
			Files.write(Paths.get(new URI((_04_FilesTest.class.getResource("") + "modified_test.txt").replace(" ", "%20"))), lines);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		/*
		 * Please keep in mind that those methods are not very memory-efficient because the whole file will be read into memory. The larger the file,
		 * the more heap-size will be used. As an memory-efficient alternative, you could use the method Files.lines. Instead of reading all lines into 
		 * memory at once, this method reads and streams each line one by one via functional streams.
		 */
		try (Stream<String> stream = Files.lines(Paths.get(_04_FilesTest.class.getResource("test.txt").toURI()))) {
		    stream
		        .filter(line -> line.contains("toto"))
		        .map(String::trim)
		        .forEach(System.out::println); // "toto like ice-cream"
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("=====================================");
		
		/*
		 * If you need more fine-grained control you can instead construct a new buffered reader:
		 */
		Path path;
		try {
			path = Paths.get(_04_FilesTest.class.getResource("test.txt").toURI());
			
			try (BufferedReader reader = Files.newBufferedReader(path)) {
			    System.out.println(reader.readLine()); // "first line"
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		/*
		 * Or in case you want to write to a file, simply construct a buffered writer instead :
		 */
		try {
			Path path2 = Paths.get(new URI((_04_FilesTest.class.getResource("") + "modified_test2.txt").replace(" ", "%20")));
			
			try (BufferedWriter writer = Files.newBufferedWriter(path2)) {
			    writer.write("print('Hello World');");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		System.out.println("=====================================");
		
		/*
		 * Buffered readers also have access to functional streams. The method lines construct a functional stream upon all lines denoted by the 
		 * buffered reader :
		 */
		try {
			Path path3 = Paths.get(_04_FilesTest.class.getResource("test.txt").toURI());
			
			try (BufferedReader reader = Files.newBufferedReader(path3)) {
			    long countPrints = reader
			        .lines()
			        .filter(line -> line.contains("i"))
			        .count(); // 3
			    System.out.println(countPrints);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		/*
		 * Don't forget for all this examples : you have to close functional file streams explicitly with try-with-resources statements. I would have expected
		 * that functional streams auto-close when calling a terminal operation like count or collect since you cannot call terminal operations twice on the same 
		 * stream anyway.
		 */
	}
}
