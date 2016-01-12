/**
 * This file is part of GenericDisplay.
 *
 * GenericDisplay is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GenericDisplay is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GenericDisplay.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dc.display;

import org.dc.jps.core.JPService;
import org.dc.jps.exception.JPServiceException;
import org.dc.jul.exception.CouldNotPerformException;
import org.dc.jul.exception.InitializationException;
import org.dc.jul.exception.printer.ExceptionPrinter;
import org.dc.display.jp.JPGenericDisplayScope;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

/**
 *
 * @author * @author <a href="mailto:DivineThreepwood@gmail.com">Divine Threepwood</a>
 */
public class DisplayView extends Application implements DisplayInterface {

    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(DisplayView.class);

    public static final String DEFAULT_URL = "http://www.google.de";

    private DisplayServer server;
    private WebEngine webEngine;

    private void init(Stage primaryStage) throws InterruptedException, InitializationException {

        try {
            WebView webView = new WebView();
            primaryStage.setScene(new Scene(webView));
            primaryStage.setFullScreen(true);
            primaryStage.setAlwaysOnTop(true);

            webEngine = webView.getEngine();

            try {
                showText("Starting server instance...");
            } catch (CouldNotPerformException ex) {
                Logger.getLogger(DisplayView.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                this.server = new DisplayServer(this);
                server.init(JPService.getProperty(JPGenericDisplayScope.class).getValue());
                server.activate();
            } catch (JPServiceException | CouldNotPerformException ex) {
                throw new CouldNotPerformException("Could not load display server!", ex);
            }
        } catch (CouldNotPerformException ex) {
            throw new InitializationException(this, ex);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            init(primaryStage);
            primaryStage.show();
        } catch (CouldNotPerformException ex) {
            throw ExceptionPrinter.printHistoryAndReturnThrowable(new CouldNotPerformException("Could not start gui!", ex), logger);
        }
    }

    private Future<Void> displayHTML(final String html) {
        Platform.runLater(() -> {
            webEngine.loadContent(html);
        });
        return CompletableFuture.completedFuture(null);
    }

    private Future<Void> displayURL(final String url) {
        Platform.runLater(() -> {
            webEngine.load(url);
        });
        return CompletableFuture.completedFuture(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> showURL(final String url) {
        return displayURL(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> showHTMLContent(final String content) {
        return displayHTML(content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> showPreset(String presetId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showInfoText(String presetId) throws CouldNotPerformException {
        return displayHTML(TextBoxHTMLGenerator.generate(presetId, Color.FORESTGREEN.darker()));
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showWarnText(String presetId) throws CouldNotPerformException {
        return displayHTML(TextBoxHTMLGenerator.generate(presetId, Color.ORANGE));
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showErrorText(String presetId) throws CouldNotPerformException {
        return displayHTML(TextBoxHTMLGenerator.generate(presetId, Color.RED.darker()));
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showText(String presetId) throws CouldNotPerformException {
        return displayHTML(TextBoxHTMLGenerator.generate(presetId, Color.BLACK));
    }

    public static void main(String[] args) {

        // Configure and parse command line properties
        JPService.setApplicationName("generic-display-server");
        JPService.registerProperty(JPGenericDisplayScope.class);
        JPService.parseAndExitOnError(args);

        // launch user interface
        launch(args);
    }
}
