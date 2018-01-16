package org.openbase.display;

/*
 * #%L
 * GenericDisplay
 * %%
 * Copyright (C) 2015 - 2018 openbase.org
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
import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.util.UUID;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import org.apache.commons.io.FileUtils;
import static org.openbase.display.DisplayView.logger;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.InvalidStateException;
import org.openbase.jul.exception.NotAvailableException;
import org.openbase.jul.exception.printer.ExceptionPrinter;

/**
 *
 * @author <a href="mailto:divine@openbase.org">Divine Threepwood</a>
 */
public class WebTab {

    private final WebView webView;
    private String content;
    private int contentHash;
    private final Pane mainPane;
    private final File userDirectory;

    public WebTab(int contentHash, final Pane mainPane) {
        this.contentHash = contentHash;
        this.mainPane = mainPane;
        this.webView = newWebView();
        this.userDirectory = new File(new File(FileUtils.getTempDirectory(), "generic-display"), UUID.randomUUID().toString());
        this.webView.getEngine().setUserDataDirectory(userDirectory);
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
     * @param url URL of the web page to load
     */
    public void load(final String url) {
        if (!url.equals(this.content)) {
            webView.getEngine().load(url);
            this.content = url;
        }
        displayTab();
    }

    /**
     * Loads the given HTML content directly. This method is useful when you have an HTML
     * String composed in memory, or loaded from some system which cannot be reached via
     * a URL (for example, the HTML text may have come from a database). As with
     * {@link #load(String)}, this method is asynchronous.
     *
     * @param content
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    public void loadContent(final String content) throws CouldNotPerformException {
        try {
            if (content == null) {
                throw new NotAvailableException("Content");
            }

            if (!content.equals(this.content)) {
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
     * {@link #load(String)}, this method is asynchronous. This method also allows you to
     * specify the content type of the string being loaded, and so may optionally support
     * other types besides just HTML.
     *
     * @param content
     * @param contentType
     */
    public void loadContent(final String content, final String contentType) {
        if (!content.equals(this.content)) {
            webView.getEngine().loadContent(content, contentType);
            this.content = content;
        }
        displayTab();
    }

    public void displayTab() {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(webView);
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
}
