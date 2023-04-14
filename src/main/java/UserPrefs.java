package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class UserPrefs {

    private final String[] HISTORYKEYS = {"findHistory", "replaceHistory", "filterHistory", "pathHistory"};
    private final Preferences PREFS;
    private HashMap<String, LinkedHashSet<String>> listMap;
    private LinkedHashSet<String> findHistory;
    private LinkedHashSet<String> replaceHistory;
    private LinkedHashSet<String> filterHistory;
    private LinkedHashSet<String> pathHistory;
    private boolean fileMode;
    private boolean matchCase;
    private boolean wholeWord;
    //------------------------------------------------------
    // Constructors begin here
    public UserPrefs() {
        this("comp585Project2");
    }

    public UserPrefs(String PREFSKEY) {
        this.PREFS = Preferences.userRoot().node(PREFSKEY);
        this.loadSavedPrefs();
        this.initListMap();
    }
    // Constructors end here
    //------------------------------------------------------
    // Getters/Accessors begin here
    public LinkedHashSet<String> getFindHistory() {
        return findHistory;
    }

    public LinkedHashSet<String> getReplaceHistory() {
        return replaceHistory;
    }

    public LinkedHashSet<String> getFilterHistory() {
        return filterHistory;
    }

    public LinkedHashSet<String> getPathHistory() {
        return pathHistory;
    }

    public boolean isFileMode() {
        return fileMode;
    }

    public boolean isMatchCase() {
        return matchCase;
    }

    public boolean isWholeWord() {
        return wholeWord;
    }
    // Getters/Accessors end here
    //------------------------------------------------------
    // Setters/Mutators begin here
    public void setFileMode(boolean val) {
        this.fileMode = val;
    }

    public void setMatchCase(boolean val) {
        this.matchCase = val;
    }

    public void setWholeWord(boolean val) {
        this.wholeWord = val;
    }
    // Setters/Mutators end here
    //------------------------------------------------------
    // class logic begins here

    /**
     *
     */
    private void loadSavedPrefs() {
        // get the last operating mode, single file or whole folder...
        fileMode = PREFS.getBoolean("fileMode", true);
        // now check our case and word settings...
        matchCase = PREFS.getBoolean("matchCase", false);
        wholeWord = PREFS.getBoolean("wholeWord", false);
        // lastly, initialize any previous history fields...
        loadHistoryLists();
    }

    /**
     * Populates the hashmap we use for mapping the various history
     * preference keys (String) to their respective LinkedHashSet
     * objects, so we can easily look up the correct set
     */
    private void initListMap() {
        this.listMap = new HashMap<>();
        for (String key : HISTORYKEYS) {
            switch (key) {
                case "findHistory" -> listMap.put(key, findHistory);
                case "replaceHistory" -> listMap.put(key, replaceHistory);
                case "filterHistory" -> listMap.put(key, filterHistory);
                case "pathHistory" -> listMap.put(key, pathHistory);
            }
        }
    }

    /**
     * Initializes previously run search, replace, filter, and file/path history
     *
     * Uses the java preferences api to retrieve the relevant key/value pairs,
     * then parses the values into lists of strings, splitting on '|' (pipe)
     *
     * Then we explicitly use a copy of the parsed set, to ensure we don't run into
     * any issues with duplicate object references, i.e. so no two combo boxes
     * can accidentally be assigned the same history list/set
     */
    private void loadHistoryLists() {
        for (String key : HISTORYKEYS) {
            String rawHistory = PREFS.get(key, "");
            LinkedHashSet<String> splitHist = new LinkedHashSet<>(new ArrayList<>(List.of(rawHistory.split("\\|"))));
            switch (key) {
                case "findHistory" -> findHistory = new LinkedHashSet<>(splitHist);
                case "replaceHistory" -> replaceHistory = new LinkedHashSet<>(splitHist);
                case "filterHistory" -> filterHistory = new LinkedHashSet<>(splitHist);
                case "pathHistory" -> pathHistory = new LinkedHashSet<>(splitHist);
            }
        }
    }

    /**
     * Saves all the current settings and history entries to disk
     * using the Java Preferences API.
     */
    public void savePrefsToDisk() {
        // save our boolean/toggle options
        PREFS.putBoolean("fileMode", fileMode);
        PREFS.putBoolean("matchCase", matchCase);
        PREFS.putBoolean("wholeWord", wholeWord);
        // now save all of our various history fields/lists
        for (String key : HISTORYKEYS) {
            saveHistoryList(key, listMap.get(key));
        }
    }

    /**
     * Saves the elements of a LinkedHashSet, of type String, to
     * a single String value in our Preferences object.
     * concatenates the items using '|' (pipe) as the delimeter
     * @param key   the preference key to save to
     * @param list  the list of items to concat and save
     */
    private void saveHistoryList(String key, LinkedHashSet<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String item : list) {
            if (Utils.notNullOrEmpty(item)) {
                builder.append(item);
                builder.append("|");
            }
        }
        // now save the history string to disk
        String val = builder.toString();
        PREFS.put(key, val);
    }

    /**
     * Handles resetting of all 'saved' preferences to
     * application default values, by first removing
     * all keys from the Preferences object, then
     * re-running our load method, which will force
     * all values to use the default.
     */
    public void resetSavedPrefs() {
        try {
            for (String key : PREFS.keys()) {
                PREFS.remove(key);
            }
            // now reset our local history sets
            findHistory.clear();
            replaceHistory.clear();
            filterHistory.clear();
            pathHistory.clear();
            // now reload our 'saved' prefs, which forces all to default values
            this.loadSavedPrefs();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }
}
