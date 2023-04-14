package main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {

    public static boolean isNullOrEmpty(String input) {
        return (input == null || input.isEmpty());
    }

    public static boolean notNullOrEmpty(String input) {
        return !isNullOrEmpty(input);
    }

    /**
     * Checks if all strings in a given array are both not null,
     * and not empty/blank.
     *
     * @param strings   the array of strings to check
     * @return          true if no strings in the array are
     *                  empty/blank, or null; otherwise false
     */
    public static boolean allNotNullOrEmpty(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            if (isNullOrEmpty(strings[i])) {
                // if any array element is null or empty, we can stop
                return false;
            }
        }
        // if we get here, the loop completed, so no element was null/empty
        return true;
    }

    public static boolean isDsStoreFile(Path path) {
        String file = path.getFileName().toString();
        return file.toLowerCase().contains(".ds_store");
    }


    public static boolean notDsStoreFile(Path path) {
        return !isDsStoreFile(path);
    }

    /**
     * Checks to see if a given file is a binary file
     *
     * @param path  the file to check, as java.nio.file.Path object
     * @return      true if the file is likely binary,
     *              otherwise false.
     */
    public static boolean isBinaryFile(Path path) {
        try {
            String type = Files.probeContentType(path);
            if (type == null) {
                //type couldn't be determined, assume binary
                return true;
            } else if (type.startsWith("text")) {
                return false;
            } else {
                //type isn't text
                return true;
            }
        } catch (IOException e) {
            return true;
        }
    }

    public static boolean isTextFile(Path path) {
        return !isBinaryFile(path);
    }

    /**
     * Checks if a File both exists on disk, and is writable by java
     *
     * @param path  the file to check, as java.nio.file.Path object
     * @return      true if the file exists, and is writable,
     *              otherwise false
     */
    public static boolean existsAndWritable(Path path) {
        return Files.exists(path) && Files.isWritable(path);
    }

    /**
     * Checks a given path for all the following:
     *      1) exists on disk
     *      2) is a directory and not a regular file
     *      3) is writable by our application/JVM
     *
     * @param path  the path to check
     * @return      boolean: true if all 3 are true
     *              otherwise false.
     */
    public static boolean dirExistsAndWritable(Path path) {
        return Files.isDirectory(path) && existsAndWritable(path);
    }

    public static boolean matchString(String input, String search) {
        return input.toLowerCase().contains(search.toLowerCase());
    }

    public static boolean matchWord(String input, String search) {
        return input.toLowerCase().matches(".*\\b" + search.toLowerCase() + "\\b.*");
    }

    public static boolean matchCase(String input, String search) {
        return input.contains(search);
    }

    public static boolean matchWordAndCase(String input, String search) {
        return matchWord(input, search) && matchCase(input, search);
    }
}
