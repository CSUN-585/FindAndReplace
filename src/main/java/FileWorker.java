package main.java;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileWorker extends SwingWorker<ResultsTableModel, Object> {

    private final ResultsTableModel results;
    private final HashMap<Path, ResultFile> resultsMap;
    private String pathString;
    private String filter;
    private String findText;
    private String replaceText;
    private Path searchPath;
    private boolean fileMode;
    private boolean matchCase;
    private boolean wholeWord;
    private WorkerOps currentOp;

    public FileWorker(ResultsTableModel results) {
        this.resultsMap = new HashMap<>();
        this.results = results;
    }

    //------------------------------------------------------
    // Setters/Mutators begin here...
    public void setPath(String rawPath) {
        this.pathString = rawPath;
        this.searchPath = Paths.get(rawPath);
    }

    public void setFilter(String input) {
        if (Utils.isNullOrEmpty(input)) {
            this.filter = "";
        } else {
            this.filter = input;
        }
    }

    public void setFindText(String input) {
        this.findText = input;
    }

    public void setReplaceText(String input) {
        this.replaceText = input;
    }

    public void setFileMode(boolean bool) {
        this.fileMode = bool;
    }

    public void setCurrentOp(WorkerOps op) {
        this.currentOp = op;
    }

    public void setMatchCase(boolean bool) {
        this.matchCase = bool;
    }

    public void setWholeWord(boolean bool) {
        this.wholeWord = bool;
    }
    // Setters/Mutators end here.
    //------------------------------------------------------
    // class logic begins here

    /**
     * Wrapper method to call our single findInFile
     * method on all files a given/provided folder.
     *
     */
    private void findInFolder() {
        try {
            Set<Path> filesToSearch = getListOfFiles(searchPath);
            for (Path file : filesToSearch) {
                findInFile(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Iterates over the lines in a file to search for
     * the user provided text string, and if found
     * adds a new entry to our result set
     *
     * @param p the file to search, as
     *          java.nio.file.Path object
     */
    private void findInFile(Path p) {
        // create our ResultFile object, and add it to our hashset
        resultsMap.put(p, new ResultFile(p));
        // now read the file line by line and look for our search text/string
        try (Stream<String> lines = Files.lines(p)) {
            AtomicLong counter = new AtomicLong();
            lines.forEach(line -> {
                // because line numbers don't zero-index, pre-increment counter
                long lineNum = counter.incrementAndGet();
                // now check the line for our search text
                if (checkLine(p,line, lineNum)) {
                    addResultItem(p, line, lineNum);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper function that decides how to compare the
     * contents of a given line in a file (line) with
     * the user provided search string.
     *
     * @param p         the path to the file, as
     *                  java.nio.file.Path object
     * @param line      the text of the line in
     *                  the file, as String
     * @param lineNum   the line number of the line
     *                  in the file, as long
     */
    private boolean checkLine(Path p, String line, long lineNum) {
        if (matchCase && wholeWord) {
            return Utils.matchWordAndCase(line, findText);
        } else if (matchCase) {
            return Utils.matchCase(line, findText);
        } else if (wholeWord) {
            return Utils.matchWord(line, findText);
        } else {
            return Utils.matchString(line, findText);
        }
    }

    /**
     * Builds the regex pattern used to match our search
     * string, according to the following rules:
     *      1) if neither matchCase, nor WholeWord
     *         are set, then we turn on case-insensitive
     *         mode (?i) and concat with our findText
     *      2) if only wholeWord is set, we combine (1)
     *         with explicitly adding \b (word boundaries)
     *         both before and after our find text string
     *      3) if only matchCase is set, then we
     *         explicitly turn OFF case-insensitive mode
     *         (?-i) before concatting with our findText
     *      3) if both matchCase and WholeWord are set,
     *         we combine (3) with (2)
     *
     * @return  the final regex pattern to use, as String
     */
    private String buildRegex() {
        if (matchCase && wholeWord) {
            return "(?-i)\b" + findText + "\b";
        } else if (matchCase) {
            return "(?-i)" + findText;
        } else if (wholeWord) {
            return "(?i)\b" + findText + "\b";
        } else {
            return "(?i)" + findText;
        }
    }

    /**
     * Adds a new entry to our results list
     *
     * @param p the path to the file, as Path object
     * @param l the line of text that matched our search string
     * @param n the line number of the matched line (param l)
     */
    private void addResultItem(Path p, String l, long n) {
        ResultRow item = null;
        switch (currentOp) {
            case FIND -> {
                item = new ResultRow(n, p, l, l);
            }
            case REPLACE -> {
                String regex = buildRegex();
                item = new ResultRow(n, p, l, l.replaceAll(regex, replaceText));
            }
        }
        if (Objects.nonNull(item)) {
            results.add(item);
            resultsMap.get(p).addMatch(item);
        }
    }

    private boolean matchFilter(Path file) {
        boolean checkFile = !Files.isDirectory(file) &&
                            Utils.notDsStoreFile(file) &&
                            Utils.isTextFile(file);
        if (filter.isEmpty()) {
            return checkFile;
        } else {
            return checkFile && file.getFileName().toString().toLowerCase().endsWith(filter);
        }
    }

    public Set<Path> getListOfFiles(Path dir) throws IOException {
        try (Stream<Path> stream = Files.list(dir)) {
            return stream.filter(this::matchFilter).collect(Collectors.toSet());
        }
    }

    private void updateAndSaveFile(ResultFile file) {
        try {
            Path fPath = file.getFilePath();
            List<String> content = new ArrayList<>(Files.readAllLines(fPath, StandardCharsets.UTF_8));
            for (ResultRow match : file.getMatches()) {
                // because we pre-incremented our line number counter when
                // reading the files originally, we have to subtract 1
                int index = Math.toIntExact(match.getLineNumber() - 1);
                String origLine = content.get(index);
                // sanity check to make sure we got the right line of text...
                if (origLine.equals(match.getSourceText())) {
                    content.set(index, match.getReplaceText());
                }
            }
            // now create a temp file to write the changes to disk
            Path temp = Files.createTempFile(fPath.getFileName().toString(), ".tmp");
            for (String line: content) {
                Files.writeString(temp, line + System.lineSeparator(), StandardOpenOption.APPEND);
            }
            // debug
            backupFile(fPath);
            Files.move(temp, fPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void backupFile(Path fPath) throws IOException {
        Path backup = fPath.getParent().resolve(fPath.getFileName() + ".bak");
        Files.copy(fPath, backup, StandardCopyOption.REPLACE_EXISTING);
    }

    private void backupFiles() throws IOException {
        for (ResultFile file : resultsMap.values()) {
            if (file.hasMatches()) {
                backupFile(file.getFilePath());
            }
        }
    }

    private void saveFiles() {
        for (ResultFile file : resultsMap.values()) {
            if (file.hasMatches()) {
                updateAndSaveFile(file);
            }
        }
    }

    private boolean searchPathOk() {
        if (fileMode) {
            return Utils.existsAndWritable(searchPath);
        } else {
            return Utils.dirExistsAndWritable(searchPath);
        }
    }

    private boolean canRun() {
        boolean haveSource = Utils.notNullOrEmpty(findText);
        boolean havePath = Utils.notNullOrEmpty(pathString) && searchPathOk();
        boolean haveOperation = currentOp != null;
        return haveSource && havePath && haveOperation;
    }

    @Override
    protected ResultsTableModel doInBackground() throws Exception {
        if (!canRun()) {
            /*
             * if we don't have all the necessary fields set
             * to properly execute the find/replace loop,
             * throw an exception, so our UI can show an
             * error message/popup
             */
            throw new Exception();
        }
        // if we get here, our startup/safety checks passed!
        switch (currentOp) {
            case COMMIT -> saveFiles();
            case BACKUP -> backupFiles();
            case FIND, REPLACE -> {
                if (!results.isEmpty()) {
                    results.reset();
                }
                if (fileMode) {
                    // we're doing a single file find/replace
                    findInFile(searchPath);
                } else {
                    // we're running find/replace on all files in a dir
                    findInFolder();
                }
            }
        }
        return results;
    }
}
