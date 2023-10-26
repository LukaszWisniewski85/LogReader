package org.example;
import jakarta.validation.constraints.NotNull;

import java.io.*;
import java.util.*;
import static org.example.ProcessFile.*;

public class App {
    public static void main(String[] args){
        File directory = new File("/Users/lukaszwisniewski/my-app/logs");
        File[] files = directory.listFiles();
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        for (File file: files) {
            System.out.println(file);
        }
        for (File file : files) {
            boolean log = !file.getName().endsWith("log");
            if (log)
                continue;
            processFile(file);
            logCounts();
            logRangeCount();
            uniqueLibrariesCount();
        }
    }
}



