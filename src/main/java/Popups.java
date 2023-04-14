package main.java;

import javax.swing.*;
import java.util.Arrays;

public class Popups {

    private final JPanel basePanel;

    /**
     * Constructor, creates our popups helper class
     * using the provided JPanel as the parent
     * JPanel for all dialogs/popups we display.
     *
     * @param panel the JPanel to use as our
     *              parentComponent for all dialogs
     */
    public Popups(JPanel panel) {
        basePanel = panel;
    }

    public int showReset() {
        String title = "Reset saved preferences?";
        String resetText = """
                Are you sure you want to reset all saved preferences?

                This will erase the following history fields as well:
                1) path/file history
                2) search/find text history
                3) replacement text history
                4) file filter history""";
        return JOptionPane.showConfirmDialog(basePanel, resetText, title, JOptionPane.YES_NO_OPTION);
    }

    public void showAbout() {
        String about = """
                Find (and/or replace) a given string,
                in a single file, or all files in a given folder.

                by Jocelyn Mallon
                for Comp 585, Spring 2023.""";
        JOptionPane.showMessageDialog(basePanel, about);
    }

    public void showHelp() {
        String helpMsg = """
                One or more required fields are empty.

                for 'find' mode, both 'search path' and\s
                'string to find' are required.

                for 'replace' mode, 'replace with' is
                also required.""";
        showError(helpMsg);
    }

    public int showBackup() {
        String title = "Backup original files?";
        String msg = """
                Make a backup copy of all files with
                matches?
                
                e.g. somefile.txt -> somefile.txt.bak
                """;
        return showConfirm(title, msg);
    }

    public int showCancel() {
        String title = "Cancel/Reset matches?";
        String cancelMsg = """
                Are you sure you want to cancel the current
                find/replace operation?
                
                This will reset all results/matches.
                """;
        return showConfirm(title, cancelMsg);
    }

    public int showConfirm() {
        String title = "Write changes to disk?";
        String infoMsg = """
                Are you sure you want to write all changes to disk?
                
                This operation cannot be undone.
                """;
        return showConfirm(title, infoMsg);
    }

    private int showConfirm(String title, String msg) {
        return JOptionPane.showConfirmDialog(basePanel, msg, title, JOptionPane.YES_NO_OPTION);
    }

    // simple error message popup methods from Heslacher @ stack overflow
    public void showError(String errorMessage) {
        showError(errorMessage, "Error!");
    }

    public void showError(Exception exceptionError) {
        String errorMessage = "Message: " + exceptionError.getMessage()
                + "\nStackTrace: " + Arrays.toString(exceptionError.getStackTrace());
        String title = exceptionError.getClass().getName();
        showError(errorMessage, title);
    }

    public void showError(String errorMessage, String title) {
        JOptionPane.showMessageDialog(basePanel, errorMessage, title, JOptionPane.ERROR_MESSAGE);
    }
}
