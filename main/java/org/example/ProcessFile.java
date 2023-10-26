package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessFile {
    public static HashSet<String> logAsStringTreeSet = new HashSet<>();
    public static void processFile(File file) {

        try {
            FileInputStream fstream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            Date lastMod = new Date(file.lastModified());

            LocalTime beforeReadingTime = LocalTime.now();
            /* odczytu logu do Hashset do późniejszej obróbki */
            while ((strLine = br.readLine()) != null) {
                logAsStringTreeSet.add(strLine);
            }
            fstream.close();
            logAsStringTreeSet.removeIf(String::isEmpty);

            // log czasu po odczycie pliku i zapisane różnicy
            LocalTime afterReadingTime = LocalTime.now();
            long readingTime = Duration.between(beforeReadingTime, afterReadingTime).toMillis();
            System.out.println("---------------------------");
            System.out.println("Plik: " + file.getName() + ", Date: " + lastMod + ".");
            System.out.println("Czas odczytu pliku: " + readingTime + " milisekund");
        } catch (Exception e) {
            System.err.println("Error: File not found or can not be read");
        }
    }
        //zliczanie ilości logu wg severity
        public static void logCounts() {
            int infoCount = 0;
            int warnCount = 0;
            int errorCount = 0;
            int debugCount = 0;
            int fatalCount = 0;
            int totalCount;
            for (String string : logAsStringTreeSet) {
                if (string.contains("INFO")) infoCount++;
                if (string.contains("WARN")) warnCount++;
                if (string.contains("ERROR")) errorCount++;
                if (string.contains("DEBUG")) debugCount++;
                if (string.contains("FATAL")) fatalCount++;
            }
            totalCount = warnCount + infoCount + errorCount + fatalCount + debugCount;
            System.out.println("Liczba logów INFO:  " + infoCount + " WARN: " + warnCount + " ERROR: " + errorCount + " DEBUG: " + debugCount + " FATAL: " + fatalCount);
            System.out.println("Total : " + totalCount + " ERROR Ratio: " + 100*errorCount/totalCount + " % " + " DEBUG: " + 100*debugCount/totalCount + " % " + " FATAL: " + 100*fatalCount/totalCount + " % ");

        }

        //zliczanie wystąpień unikalnych bibliotek
        public static void uniqueLibrariesCount() {
        TreeSet <String> uniqueLibraries = new TreeSet<>();
        for (String string : logAsStringTreeSet) {
            if (string.contains("[org.")) {
                string = string.substring(string.indexOf('['), string.indexOf(']') + 1);
                uniqueLibraries.add(string);
            }
        }
        System.out.println("Liczba bibliotek: " +  uniqueLibraries.size() + "\n" +
                    "biblioteki: " + uniqueLibraries);
}

            //zliczanie zakresu logów
    public static void logRangeCount() {
        TreeSet<LocalDateTime> dateTreeSet = new TreeSet<>();
        Pattern pattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})");

        for (String string : logAsStringTreeSet) {
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                string = string.substring(0, 23);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
                LocalDateTime date = LocalDateTime.parse(string, formatter);
                dateTreeSet.add(date);
            }

        }
        long duration = Duration.between(dateTreeSet.first(), dateTreeSet.last()).toSeconds();
        long diffMinutes = duration / 60 % 60;
        long diffHours = duration / (60 * 60) % 24;
        long diffDays = duration / (24 * 60 * 60);
        long diffSeconds = duration % 60;
        System.out.println("Zakres logu: " + diffDays + " dni " + diffHours + " godzin " + diffMinutes + " minut " +  diffSeconds + " sekund.");

    }
    }

