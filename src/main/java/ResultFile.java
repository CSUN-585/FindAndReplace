package main.java;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class ResultFile {

    private final Path filePath;
    private HashSet<ResultRow> matchesSet;
    private HashMap<Long, ResultRow> lineMap;

    public ResultFile(Path path) {
        this.filePath = path;
        this.matchesSet = new LinkedHashSet<>();
        this.lineMap = new HashMap<>();
    }

    public Path getFilePath() {
        return filePath;
    }

    public HashSet<ResultRow> getMatches() {
        return matchesSet;
    }

    public void addMatch(ResultRow match) {
        lineMap.put(match.getLineNumber(), match);
        matchesSet.add(match);
    }

    public boolean hasMatches() {
        return !matchesSet.isEmpty();
    }

    public long getMatchCount() {
        return lineMap.size();
    }
}
