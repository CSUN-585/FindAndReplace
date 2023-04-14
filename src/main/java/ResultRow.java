package main.java;

import java.nio.file.Path;

public class ResultRow {

    private final Long lineNumber;
    private final String sourceText;
    private final String replaceText;
    private final Path filePath;

    /**
     * Constructor for a single result object
     *
     * @param line      the line number the match was found, as long
     * @param find      the entire source line of text, as String
     * @param replace   the source line, with all matches replaced
     *                  with users provided text (if any provided,
     *                  else will be identical to find)
     */
    public ResultRow(Long line, Path filePath, String find, String replace) {
        this.filePath = filePath;
        this.lineNumber = line;
        this.sourceText = find;
        this.replaceText = replace;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public String getSourceText() {
        return sourceText;
    }

    public String getReplaceText() {
        return replaceText;
    }

    public String getFileName() {
        return filePath.getFileName().toString();
    }

    public Path getFilePath() {
        return filePath;
    }

}
