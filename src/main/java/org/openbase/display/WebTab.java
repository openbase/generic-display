package org.openbase.display;

/*
 * #%L
 * GenericDisplay
 * %%
 * Copyright (C) 2015 - 2021 openbase.org
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import org.apache.commons.io.FileUtils;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.InvalidStateException;
import org.openbase.jul.exception.NotAvailableException;
import org.openbase.jul.exception.printer.ExceptionPrinter;
import org.openbase.jul.schedule.GlobalCachedExecutorService;
import org.openbase.jul.schedule.SyncObject;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Future;

import static org.openbase.display.DisplayView.logger;

/**
 * @author <a href="mailto:divine@openbase.org">Divine Threepwood</a>
 */
public class WebTab {

    private final static SyncObject displayTaskLock = new SyncObject("displayTaskLock");
    private static Future<Void> displayTask;
    private final WebView webView;
    private final StackPane mainStackPane;
    private final File userDirectory;
    private final SyncObject contentLoaderLock = new SyncObject("ContentLoaderLock");
    private Worker.State contentLoadersState = State.READY;
    private String content;
    private int contentHash;

    public WebTab(int contentHash, final StackPane mainStackPane) {
        this.contentHash = contentHash;
        this.mainStackPane = mainStackPane;
        this.webView = newWebView();
        this.userDirectory = new File(new File(FileUtils.getTempDirectory(), "generic-display"), UUID.randomUUID().toString());
        this.webView.getEngine().setUserDataDirectory(userDirectory);
        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            synchronized (contentLoaderLock) {
                contentLoadersState = newValue;
                switch (newValue) {
                    case SUCCEEDED:
                    case READY:
                        contentLoaderLock.notifyAll();
                    default:
                        // do nothing
                }
            }
        });
    }

    private static WebView newWebView() {
        logger.info("init new WebView...");
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.setOnAlert((WebEvent<String> event) -> {
            ExceptionPrinter.printHistory(new InvalidStateException("Webengine alert detected!", new CouldNotPerformException(event.toString())), logger);
        });
        webEngine.setOnError((WebErrorEvent event) -> {
            ExceptionPrinter.printHistory(new InvalidStateException("Webengine error detected!", new CouldNotPerformException(event.toString())), logger);
        });
        webEngine.getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newState) -> {
            Throwable exception = webEngine.getLoadWorker().getException();
            if (exception != null && newState == Worker.State.FAILED) {
                ExceptionPrinter.printHistory(new InvalidStateException("Webengine exception detected!", exception), logger);
            }
        });

        CookieManager.getDefault();
        return webView;
    }

    public WebView getWebView() {
        return webView;
    }

    public void updateContextHash(final int content) {
        this.contentHash = content;
    }

    public int getContentHash() {
        return contentHash;
    }

    public WebEngine getEngine() {
        return webView.getEngine();
    }

    public void shutdown() {
        try {
            FileUtils.deleteDirectory(userDirectory);
        } catch (IOException ex) {
            ExceptionPrinter.printHistory(new CouldNotPerformException("Could not cleanup user dir!", ex), logger);
        }
    }

    /**
     * Loads a Web page into this engine. This method starts asynchronous
     * loading and returns immediately.
     *
     * @param url    URL of the web page to load
     * @param reload forces to reload the tab
     */
    public void load(final String url, final boolean reload) {
        if (reload || !url.equals(this.content)) {
            webView.getEngine().load(url);
            this.content = url;
        }
        displayTab();
    }

    /**
     * Loads the given HTML content directly. This method is useful when you have an HTML
     * String composed in memory, or loaded from some system which cannot be reached via
     * a URL (for example, the HTML text may have come from a database). As with
     * {@link #load(String, boolean)}, this method is asynchronous.
     *
     * @param content the html content to display
     * @param reload  forces to reload the tab
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    public void loadContent(final String content, final boolean reload) throws CouldNotPerformException {
        try {
            if (content == null) {
                throw new NotAvailableException("Content");
            }

            if (reload || !content.equals(this.content)) {
                // load new content and display after loading content.
                webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                        if (newValue != Worker.State.SUCCEEDED) {
                            return;
                        }
                        displayTab();
                    }
                });
                webView.getEngine().loadContent(content);
                this.content = content;
            } else {
                displayTab();
            }

        } catch (CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not load web content!", ex);
        }
    }

    /**
     * Loads the given content directly. This method is useful when you have content
     * composed in memory, or loaded from some system which cannot be reached via
     * a URL (for example, the SVG text may have come from a database). As with
     * {@link #load(String, boolean)}, this method is asynchronous. This method also allows you to
     * specify the content type of the string being loaded, and so may optionally support
     * other types besides just HTML.
     *
     * @param content     the html content to display
     * @param contentType
     * @param reload      forces to reload the tab
     */
    public void loadContent(final String content, final String contentType, final boolean reload) {
        if (reload || !content.equals(this.content)) {
            webView.getEngine().loadContent(content, contentType);
            this.content = content;
        }
        displayTab();
    }

    public void displayTab() {
        synchronized (displayTaskLock) {
            // cancel loading tabs
            if (displayTask != null && !displayTask.isDone()) {
                displayTask.cancel(true);
            }

            displayTask = GlobalCachedExecutorService.submit(() -> {
                try {
                    waitForContent();
                    Platform.runLater(() -> {

                        // skip if already shown
                        if (mainStackPane.getChildren().contains(webView)) {
                            return;
                        }

                        // display
                        mainStackPane.getChildren().add(webView);
                        webView.toFront();

                        // remove other background views to increase performance
                        for (final Node node : new ArrayList<>(mainStackPane.getChildren())) {
                            if (node == webView) {
                                continue;
                            }
                            mainStackPane.getChildren().remove(node);
                        }
                    });
                    return null;
                } catch (CouldNotPerformException ex) {
                    throw new CouldNotPerformException("Could not display content!", ex);
                }
            });
        }
    }

    private void waitForContent() throws CouldNotPerformException {
        synchronized (contentLoaderLock) {
            if (contentLoadersState == State.READY || contentLoadersState == State.SUCCEEDED) {
                return;
            }
            try {
                contentLoaderLock.wait(10000);
            } catch (InterruptedException ex) {
                throw new CouldNotPerformException("Could not wait for content!", ex);
            }
        }
    }
}
