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
package de.dc.display;
import de.citec.jps.core.JPService;
import de.citec.jps.preset.JPVerbose;
import de.citec.jul.exception.CouldNotPerformException;
import de.citec.jul.exception.ExceptionPrinter;
import de.citec.jul.extension.rsb.scope.jp.JPScope;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.slf4j.LoggerFactory;
import rsb.Scope;

/**
 *
 * @author * @author <a href="mailto:DivineThreepwood@gmail.com">Divine Threepwood</a>
 */
public class DisplayView extends Application implements DisplayInterface {

    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(DisplayView.class);

    public static final String DEFAULT_URL = "http://www.google.de";

    private DisplayServer server;
    private WebEngine webEngine;

    private void init(Stage primaryStage) throws InterruptedException {
        WebView webView = new WebView();
        primaryStage.setScene(new Scene(webView));
        primaryStage.setFullScreen(true);
        primaryStage.setAlwaysOnTop(true);

        webEngine = webView.getEngine();
        webEngine.load(DEFAULT_URL);

        try {
            this.server = new DisplayServer(this);
            server.init("/home/display");
            server.activate();
        } catch (CouldNotPerformException ex) {
            ExceptionPrinter.printHistory(logger, ex);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        JPService.setApplicationName("display-view");
        JPService.registerProperty(JPScope.class, new Scope("/home/display"));
        JPService.registerProperty(JPVerbose.class, true);
        JPService.parseAndExitOnError(args);
        launch(args);
    }

    @Override
    public Future<Void> showURL(final String url) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                webEngine.load(url.startsWith("http://") ? url : "http://" + url);
            }
        });
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public Future<Void> showHTMLContent(final String content) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                webEngine.loadContent(content);
            }
        });
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public Future<Void> showPreset(String presetId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
