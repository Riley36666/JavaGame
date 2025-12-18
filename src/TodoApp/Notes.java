package TodoApp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Notes {
    public static void open(int page) throws FileNotFoundException {
        try (BufferedReader br =
                     new BufferedReader(new FileReader("tasks/task" + page + ".txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                System.out.println(line); // placeholder
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
