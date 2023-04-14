package main.java;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class App {

    private static final String PREFSKEY = "comp585Project2";

    /**
     * Entry point for our application, handles the following:
     *  1) creating our UserPrefs object
     *  2) doing some simple OS checking
     *  3) setting up our custom UI theme
     *  4) adding our shutDownHook
     *
     * @param args  user provided args, as String array
     */
    public static void main(String[] args) {
        // first, load our saved preferences before we do anything
        UserPrefs prefs = new UserPrefs(PREFSKEY);
        // now check for macOS to set system light/dark mode
        String os = System.getProperty("os.name");
        if (os.contains("Mac OS X")) {
            System.setProperty("apple.awt.application.appearance", "system");
        }
        /*
         * We set up our custom theme here, to ensure that the theme
         * propagates/applies to all elements in the UI...
         */
        FlatMacDarkLaf.setup();
        // ...then start the actual app code/logic...
        MainView app = new MainView(prefs);
        // ...and add our shutdown hook to save prefs on exit...
        Runtime.getRuntime().addShutdownHook(new Thread(prefs::savePrefsToDisk));
        // ...finally, display our application on screen!
        app.display();
    }
}
