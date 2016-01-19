package io.github.btkelly.gandalf.example;

import android.app.Application;

import io.github.btkelly.gandalf.Gandalf;

/**
 * TODO: Add a class header comment!
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new Gandalf.Installer()
                .setBootstrapUrl("Test")
                .install();

    }
}
