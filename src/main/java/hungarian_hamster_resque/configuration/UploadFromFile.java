package hungarian_hamster_resque.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UploadFromFile {
//
//    public void readFromFile(Path path) {
//        try (BufferedReader br = Files.newBufferedReader(path)) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                splitter(line);
//            }
//        } catch (IOException ioe) {
//            throw new IllegalArgumentException("Can not read the file", ioe);
//        }
//    }
//
//    private void splitter(String line) {
//        String[] temp = line.split("-");
//        String[] tempMoviesAndDate = temp[1].split(";");
//        if (!shows.containsKey(temp[0])) {
//            shows.put(temp[0], new ArrayList<>());
//        }
//        shows.get(temp[0]).add(new Movie(tempMoviesAndDate[0], LocalTime.parse(tempMoviesAndDate[1])));
//        Collections.sort(shows.get(temp[0]), Comparator.comparing(Movie::getStartTime));
//    }




}
