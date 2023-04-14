package main.java;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.*;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class MainView extends JFrame {

    private final int FRAME_WIDTH_PIXELS = 800;
    private final int FRAME_HEIGHT_PIXELS = 600;
    private ResultsTableModel model;
    private final UserPrefs prefs;
    private final Popups popup;
    private final FileWorker worker;
    private final HashMap<JComboBox<String>, LinkedHashSet<String>> historyMap;
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Educational license - Jocelyn Mallon
    private JMenuBar menuBar;
    private JMenu appMenu;
    private JMenuItem resetPrefsItem;
    private JMenuItem quitMenuItem;
    private JMenu helpMenu;
    private JMenuItem aboutMenuItem;
    private JPanel basePanel;
    private JComboBox<String> pathBox;
    private JButton browseButton;
    private JComboBox<String> findBox;
    private JButton findButton;
    private JComboBox<String> replaceBox;
    private JButton replaceButton;
    private JPanel optionsPanel;
    private JRadioButton fileModeButton;
    private JRadioButton folderModeButton;
    private JCheckBox matchCase;
    private JCheckBox wholeWord;
    private JComboBox<String> filterBox;
    private JScrollPane resultsPane;
    private JTable resultsTable;
    private JButton cancelButton;
    private JButton backupButton;
    private JButton confirmButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    public MainView(UserPrefs prefs) {
        // let JFD build the bulk of our UI components...
        initComponents();
        // create and configure our custom JTable model, etc.
        initTable();
        // now load and populate our saved prefs
        this.historyMap = new HashMap<>();
        this.prefs = prefs;
        this.popup = new Popups(this.basePanel);
        this.loadSavedPrefs();
        // now init our actual worker class
        worker = new FileWorker(model);
        // now attach all of our listeners
        this.setupListeners();
    }

    /**
     * Handles the final steps to actually display our app on screen
     */
    public void display() {
        setTitle("Find & Replace");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH_PIXELS, FRAME_HEIGHT_PIXELS);
        this.setVisible(true);
        this.setFocusable(true);
        this.requestFocus();
    }

    /**
     * Initializes the various swing components used in
     * building our main application GUI.
     *
     * This is the generated code from our MainView.jfd form
     */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Educational license - Jocelyn Mallon
        menuBar = new JMenuBar();
        appMenu = new JMenu();
        resetPrefsItem = new JMenuItem();
        quitMenuItem = new JMenuItem();
        helpMenu = new JMenu();
        aboutMenuItem = new JMenuItem();
        basePanel = new JPanel();
        var pathLabel = new JLabel();
        pathBox = new JComboBox<>();
        browseButton = new JButton();
        var findLabel = new JLabel();
        findBox = new JComboBox<>();
        findButton = new JButton();
        var replaceLabel = new JLabel();
        replaceBox = new JComboBox<>();
        replaceButton = new JButton();
        optionsPanel = new JPanel();
        var scopeLabel = new JLabel();
        fileModeButton = new JRadioButton();
        folderModeButton = new JRadioButton();
        var hSpacer8 = new JPanel(null);
        matchCase = new JCheckBox();
        wholeWord = new JCheckBox();
        var hSpacer10 = new JPanel(null);
        var filterLabel = new JLabel();
        filterBox = new JComboBox<>();
        resultsPane = new JScrollPane();
        resultsTable = new JTable();
        var confirmBar = new JPanel();
        cancelButton = new JButton();
        var hSpacer1 = new JPanel(null);
        backupButton = new JButton();
        var hSpacer2 = new JPanel(null);
        confirmButton = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
        ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0};
        ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
        ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

        //======== menuBar ========
        {

            //======== appMenu ========
            {
                appMenu.setText("App");

                //---- resetPrefsItem ----
                resetPrefsItem.setText("Reset preferences");
                appMenu.add(resetPrefsItem);
                appMenu.addSeparator();

                //---- quitMenuItem ----
                quitMenuItem.setText("Quit");
                appMenu.add(quitMenuItem);
            }
            menuBar.add(appMenu);

            //======== helpMenu ========
            {
                helpMenu.setText("Help");

                //---- aboutMenuItem ----
                aboutMenuItem.setText("About");
                helpMenu.add(aboutMenuItem);
            }
            menuBar.add(helpMenu);
        }
        setJMenuBar(menuBar);

        //======== basePanel ========
        {
            basePanel.setLayout(new GridBagLayout());
            ((GridBagLayout)basePanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
            ((GridBagLayout)basePanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
            ((GridBagLayout)basePanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout)basePanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

            //---- pathLabel ----
            pathLabel.setText("search path:");
            basePanel.add(pathLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 5, 10), 0, 0));

            //---- pathBox ----
            pathBox.setEditable(true);
            basePanel.add(pathBox, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 0, 5, 5), 0, 0));

            //---- browseButton ----
            browseButton.setText("Browse");
            basePanel.add(browseButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 0, 5, 5), 0, 0));

            //---- findLabel ----
            findLabel.setText("string to find:");
            basePanel.add(findLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 5, 5, 10), 0, 0));

            //---- findBox ----
            findBox.setEditable(true);
            basePanel.add(findBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

            //---- findButton ----
            findButton.setText("Find all");
            basePanel.add(findButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

            //---- replaceLabel ----
            replaceLabel.setText("replace with:");
            basePanel.add(replaceLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 5, 5, 10), 0, 0));

            //---- replaceBox ----
            replaceBox.setEditable(true);
            basePanel.add(replaceBox, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

            //---- replaceButton ----
            replaceButton.setText("Replace all");
            basePanel.add(replaceButton, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

            //======== optionsPanel ========
            {
                optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));

                //---- scopeLabel ----
                scopeLabel.setText("scope:  ");
                optionsPanel.add(scopeLabel);

                //---- fileModeButton ----
                fileModeButton.setText("single file");
                fileModeButton.setSelected(true);
                optionsPanel.add(fileModeButton);

                //---- folderModeButton ----
                folderModeButton.setText("all files in folder");
                optionsPanel.add(folderModeButton);
                optionsPanel.add(hSpacer8);

                //---- matchCase ----
                matchCase.setText("case sensitive");
                optionsPanel.add(matchCase);

                //---- wholeWord ----
                wholeWord.setText("match whole word");
                optionsPanel.add(wholeWord);
                optionsPanel.add(hSpacer10);

                //---- filterLabel ----
                filterLabel.setText("file filter:  ");
                optionsPanel.add(filterLabel);

                //---- filterBox ----
                filterBox.setEditable(true);
                filterBox.setToolTipText("e.g. *.txt, *.*, *.java, etc.");
                optionsPanel.add(filterBox);
            }
            basePanel.add(optionsPanel, new GridBagConstraints(0, 3, 3, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));

            //======== resultsPane ========
            {

                //---- resultsTable ----
                resultsTable.setModel(new DefaultTableModel());
                resultsPane.setViewportView(resultsTable);
            }
            basePanel.add(resultsPane, new GridBagConstraints(0, 4, 3, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 5, 5, 5), 0, 0));

            //======== confirmBar ========
            {
                confirmBar.setLayout(new BoxLayout(confirmBar, BoxLayout.X_AXIS));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                confirmBar.add(cancelButton);
                confirmBar.add(hSpacer1);

                //---- backupButton ----
                backupButton.setText("backup files");
                confirmBar.add(backupButton);
                confirmBar.add(hSpacer2);

                //---- confirmButton ----
                confirmButton.setText("Confirm");
                confirmBar.add(confirmButton);
            }
            basePanel.add(confirmBar, new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        }
        contentPane.add(basePanel, new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());

        //---- modeButtons ----
        var modeButtons = new ButtonGroup();
        modeButtons.add(fileModeButton);
        modeButtons.add(folderModeButton);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    /**
     * Initializes our custom table model for the results JTable
     * and also sets our preferred column widths, etc.
     */
    private void initTable() {
        model = new ResultsTableModel();
        resultsTable.setModel(model);
        resultsTable.getColumnModel().getColumn(0).setMinWidth(60);
        resultsTable.getColumnModel().getColumn(0).setPreferredWidth(75);
        resultsTable.getColumnModel().getColumn(0).setMaxWidth(100);
        resultsTable.getColumnModel().getColumn(1).setMinWidth(200);
        resultsTable.getColumnModel().getColumn(1).setPreferredWidth(240);
        resultsTable.getColumnModel().getColumn(2).setMinWidth(200);
        resultsTable.getColumnModel().getColumn(2).setPreferredWidth(240);
        resultsTable.getColumnModel().getColumn(3).setMinWidth(200);
        resultsTable.getColumnModel().getColumn(3).setPreferredWidth(240);
    }

    /**
     * Gets the user preferences from the UserPrefs object
     * and sets the relevant options/fields in the UI
     * accordingly
     */
    private void loadSavedPrefs() {
        fileModeButton.setSelected(prefs.isFileMode());
        folderModeButton.setSelected(!prefs.isFileMode());
        matchCase.setSelected(prefs.isMatchCase());
        wholeWord.setSelected(prefs.isWholeWord());
        populateHistBoxes(prefs.getFilterHistory(), filterBox);
        populateHistBoxes(prefs.getFindHistory(), findBox);
        populateHistBoxes(prefs.getPathHistory(), pathBox);
        populateHistBoxes(prefs.getReplaceHistory(), replaceBox);
    }

    /**
     * Helper function to populate the items of a JComboBox, from a given LinkedHashSet
     *
     * @param items the set of values to add to the combo box
     * @param box   the JCombo box to populate with 'items'
     */
    private void populateHistBoxes(LinkedHashSet<String> items, JComboBox<String> box) {
        // first clear out the existing box items...
        box.removeAllItems();
        // now add all of our saved history entries
        for (String entry : items) {
            box.addItem(entry);
        }
        // now add our combobox and set to our history map
        historyMap.put(box, items);
    }

    /**
     * Handles updating the single file vs all files in folder
     * mode flags whenever the user selects either radio box
     */
    private void addModeListeners() {
        JRadioButton[] buttons = {fileModeButton, folderModeButton};
        for (JRadioButton btn : buttons) {
            btn.addActionListener(e -> {
                if (btn.equals(fileModeButton)) {
                    prefs.setFileMode(fileModeButton.isSelected());
                } else {
                    prefs.setFileMode(!folderModeButton.isSelected());
                }
            });
        }
    }

    private void addBrowseListener() {
        browseButton.addActionListener(e -> {
            // create our file chooser object
            JFileChooser j = new JFileChooser(System.getProperty("user.home"), FileSystemView.getFileSystemView());
            // now set the selection mode based on file/folder settings
            if (prefs.isFileMode()) {
                j.setFileSelectionMode(JFileChooser.FILES_ONLY);
            } else {
                j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            }
            // finally, show the browse/open dialog
            int retVal = j.showOpenDialog(null);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                pathBox.setSelectedItem(j.getSelectedFile().getAbsolutePath());
            }
        });
    }

    private void addComboListeners() {
        for (JComboBox<String> box : historyMap.keySet()) {
            box.addActionListener(e -> {
                LinkedHashSet<String> list = historyMap.get(box);
                list.add((String) box.getSelectedItem());
            });
        }
    }

    private void addResetListener() {
        resetPrefsItem.addActionListener(e -> {
            int retVal = popup.showReset();
            if (retVal == JOptionPane.YES_OPTION) {
                // first we need to tell our UserPrefs object to 'reset' everything
                prefs.resetSavedPrefs();
                // then re-load our 'saved' prefs which should now all be defaults
                this.loadSavedPrefs();
            }
        });
    }

    /**
     * Initializes the various required fields and options
     * that we need before we can run our FileWorker class
     *
     * @param op    the WorkerOps ENUM value for the
     *              operation we need to configure
     *              FileWorker for
     * @return      boolean, true if config checks
     *              successful, otherwise false
     */
    private boolean initFileWorker(WorkerOps op) {
        String[] stringsToCheck;
        String path = (String) pathBox.getSelectedItem();
        String filter = (String) filterBox.getSelectedItem();
        String findText = (String) findBox.getSelectedItem();
        String replaceText = (String) replaceBox.getSelectedItem();
        switch (op) {
            case REPLACE -> stringsToCheck = new String[]{path, findText, replaceText};
            case FIND -> stringsToCheck = new String[]{path, findText};
            default -> stringsToCheck = new String[]{path};
        }
        // now double check that nothing is null/empty
        if (Utils.allNotNullOrEmpty(stringsToCheck)) {
            worker.setPath(path);
            worker.setFilter(filter);
            worker.setFindText(findText);
            worker.setReplaceText(replaceText);
            worker.setCurrentOp(op);
            worker.setFileMode(prefs.isFileMode());
            worker.setMatchCase(prefs.isMatchCase());
            worker.setWholeWord(prefs.isWholeWord());
            return true;
        } else {
            popup.showHelp();
            return false;
        }
    }

    /**
     * Handles the actual find/replace action, when the user
     * clicks on either the 'find all' or 'replace all' buttons
     *
     * @param op  WorkerOps enum value:
     *              FIND if the user clicked
     *              the 'find all' button
     *              REPLACE if the user clicked
     *              the 'replace all' button
     */
    private void workerWrapper(WorkerOps op) {
        if (initFileWorker(op)) {
            try {
                worker.doInBackground();
            } catch (Exception e) {
                popup.showError(e);
            }
        }
    }

    private void confirmHelper() {
        if (!model.isEmpty()) {
            int retVal = popup.showConfirm();
            if (retVal == JOptionPane.YES_OPTION) {
                workerWrapper(WorkerOps.COMMIT);
            }
        } else {
            popup.showHelp();
        }
    }

    private void cancelHelper() {
        if (!model.isEmpty()) {
            int retVal = popup.showCancel();
            if (retVal == JOptionPane.YES_OPTION) {
                model.reset();
            }
        }
    }

    private void backupHelper() {
        if (!model.isEmpty()) {
            int retVal = popup.showBackup();
            if (retVal == JOptionPane.YES_OPTION) {
                workerWrapper(WorkerOps.BACKUP);
            }
        }
    }

    private void addTableClickListener() {
        resultsTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    ResultRow item = model.getRow(row);
                    try {
                        Desktop.getDesktop().open(item.getFilePath().toFile());
                    } catch (IOException ex) {
                        popup.showError(ex);
                    }
                }
            }
        });
    }

    private void setupListeners() {
        // first, add our radio button listener to both mode buttons
        addModeListeners();
        // now add the listener for match case and whole word checkboxes
        matchCase.addActionListener(e -> prefs.setMatchCase(matchCase.isSelected()));
        wholeWord.addActionListener(e -> prefs.setWholeWord(wholeWord.isSelected()));
        // next, add our listener for the 'browse' button
        addBrowseListener();
        // now hook up our combobox listeners to always sync new entries to saved prefs
        addComboListeners();
        // don't forget our menu item listeners...
        addResetListener();
        aboutMenuItem.addActionListener(e -> popup.showAbout());
        quitMenuItem.addActionListener(e -> {
            dispose();
            System.exit(0);
        });
        // and the double click listener for the JTable
        addTableClickListener();
        // last but not least, hook up our find/replace/cancel/confirm buttons
        findButton.addActionListener(e -> workerWrapper(WorkerOps.FIND));
        replaceButton.addActionListener(e -> workerWrapper(WorkerOps.REPLACE));
        confirmButton.addActionListener(e -> confirmHelper());
        cancelButton.addActionListener(e -> cancelHelper());
        backupButton.addActionListener(e -> backupHelper());
    }
}
