/**
 * =============================================================================
 *
 * This file is part of GenericDisplay.
 *
 * org.dc.GenericDisplay is free software: you can redistribute it and modify
 * it under the terms of the GNU General Public License (Version 3)
 * as published by the Free Software Foundation.
 *
 * org.dc.GenericDisplay is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with org.dc.GenericDisplay. If not, see <http://www.gnu.org/licenses/>.
 *
 * =============================================================================
 */
package org.dc.display;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.lang.builder.HashCodeBuilder;
import static org.dc.display.DisplayRemoteSend.handleProperties;
import org.dc.display.jp.JPBroadcastDisplayScope;
import org.dc.display.jp.JPDisplayScope;
import org.dc.display.jp.JPImageUrl;
import org.dc.display.jp.JPMessage;
import org.dc.display.jp.JPMessageType;
import org.dc.display.jp.JPOutput;
import org.dc.display.jp.JPTabAmount;
import org.dc.display.jp.JPUrl;
import org.dc.display.jp.JPVisible;
import org.dc.jps.core.JPService;
import org.dc.jps.exception.JPServiceException;
import org.dc.jul.exception.CouldNotPerformException;
import org.dc.jul.exception.InitializationException;
import org.dc.jul.exception.InstantiationException;
import org.dc.jul.exception.NotAvailableException;
import org.dc.jul.exception.printer.ExceptionPrinter;
import org.slf4j.LoggerFactory;

/**
 *
 * @author * @author <a href="mailto:DivineThreepwood@gmail.com">Divine Threepwood</a>
 */
public class DisplayView extends Application implements Display {

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(DisplayView.class);

    public static final int AMOUNT_OF_TAB_FALLBACK = 10;

    private DisplayServer broadcastServer, displayServer;
    private Stage primaryStage;
    private final int maxTabAmount;

    private final HashMap<Integer, WebTab> webTabMap;
    private final ConcurrentLinkedQueue<WebTab> webTabUsageQueue;
    private final HTMLLoader htmlLoader;
    private final Pane cardsPane;

    public DisplayView() throws InstantiationException {
        try {
            this.webTabMap = new HashMap<>();
            this.webTabUsageQueue = new ConcurrentLinkedQueue<>();
            this.htmlLoader = new HTMLLoader();
            this.cardsPane = new StackPane();

            int tmpMaxTabAmount;
            try {
                tmpMaxTabAmount = JPService.getProperty(JPTabAmount.class).getValue();
            } catch (JPServiceException ex) {
                tmpMaxTabAmount = AMOUNT_OF_TAB_FALLBACK;
            }
            this.maxTabAmount = tmpMaxTabAmount;
        } catch (CouldNotPerformException ex) {
            throw new InstantiationException(this, ex);
        }
    }

    private void init(final Stage primaryStage) throws InterruptedException, InitializationException {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Platform.exit();
            }
        });

        try {
            // platform configuration
            Platform.setImplicitExit(false);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            this.primaryStage = primaryStage;

            
            final Group card1 = new Group(new Text(25, 25, "Card 1"));
            final Group card2 = new Group(new Text(25, 25, "Card 2"));

            cardsPane.getChildren().clear();
            cardsPane.getChildren().add(card1);

            primaryStage.setScene(new Scene(cardsPane));

            try {
                broadcastServer = new DisplayServer(this);
                broadcastServer.init(JPService.getProperty(JPBroadcastDisplayScope.class).getValue());
                broadcastServer.activate();
            } catch (JPServiceException | CouldNotPerformException ex) {
                throw new CouldNotPerformException("Could not load display server!", ex);
            }

            try {
                displayServer = new DisplayServer(this);
                displayServer.init(JPService.getProperty(JPDisplayScope.class).getValue());
                displayServer.activate();
            } catch (JPServiceException | CouldNotPerformException ex) {
                throw new CouldNotPerformException("Could not load display server!", ex);
            }
            this.htmlLoader.init(getScreen());
        } catch (CouldNotPerformException ex) {
            throw new InitializationException(this, ex);
        }
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        try {
            init(primaryStage);
            showText(" ");

            if (JPService.getProperty(JPVisible.class).getValue()) {
                setVisible(true);
            }

        } catch (CouldNotPerformException ex) {
            throw ExceptionPrinter.printHistoryAndReturnThrowable(new CouldNotPerformException("Could not start gui!", ex), logger);
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    handleProperties(false);
                } catch (CouldNotPerformException | InterruptedException ex) {
                    ExceptionPrinter.printHistory(ex, logger);
                }
            }
        }.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        displayServer.shutdown();
        broadcastServer.shutdown();
        webTabMap.values().forEach(tab -> {
            tab.shutdown();
        });
    }

    private int getHash(final String context) {
        return new HashCodeBuilder().append(context).toHashCode();
    }

    private WebTab loadWebEngine(final String context) {
        assert getHash(context) == getHash(context);
        int contextHash = getHash(context);
        WebTab webTab;

        if (!webTabMap.containsKey(contextHash)) {
            if (webTabMap.size() >= maxTabAmount) {

                webTab = webTabUsageQueue.poll();
                // update context hash
                webTabMap.remove(webTab.getContentHash());
                webTab.updateContextHash(contextHash);

                webTabMap.put(contextHash, webTab);
            } else {
                webTabMap.put(contextHash, new WebTab(contextHash, cardsPane));
            }
        }

        // add tab to queue tail and return web engine.
        webTab = webTabMap.get(contextHash);
        if (webTabUsageQueue.contains(webTab)) {
            webTabUsageQueue.remove(webTab);
        }
        webTabUsageQueue.offer(webTab);
        return webTab;
    }

    private Future<Void> displayHTML(final String html) throws CouldNotPerformException {
        return runTask(() -> {
            loadWebEngine(html).loadContent(html);
            return null;
        });
    }

    private Future<Void> displayURL(final String url) throws CouldNotPerformException {
        return runTask(() -> {
            loadWebEngine(url).load(url);
            return null;
        });
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showURL(final String url) throws CouldNotPerformException {
        return displayURL(url);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showHTMLContent(final String content) throws CouldNotPerformException {
        return displayHTML(content);
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showInfoText(final String presetId) throws CouldNotPerformException {
        return displayHTML(htmlLoader.loadTextView(presetId, Color.FORESTGREEN.darker()));
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showWarnText(final String presetId) throws CouldNotPerformException {
        return displayHTML(htmlLoader.loadTextView(presetId, Color.ORANGE));
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showErrorText(final String presetId) throws CouldNotPerformException {
        return displayHTML(htmlLoader.loadTextView(presetId, Color.RED.darker()));
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showText(final String presetId) throws CouldNotPerformException {
        return displayHTML(htmlLoader.loadTextView(presetId, Color.BLACK));
    }

    /**
     * {@inheritDoc}
     *
     * @param image
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showImage(final String image) throws CouldNotPerformException {
        return displayHTML(htmlLoader.loadImageView(image));
    }

    /**
     * {@inheritDoc}
     *
     * @param visible
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setVisible(final Boolean visible) throws CouldNotPerformException {
        return runTask(() -> {
            if (visible) {
                Screen screen = getScreen();
                Stage stage = getStage();
                stage.setX(screen.getVisualBounds().getMinX());
                stage.setY(screen.getVisualBounds().getMinY());
                stage.setHeight(screen.getVisualBounds().getHeight());
                stage.setWidth(screen.getVisualBounds().getWidth());
                getStage().setFullScreen(false);
                getStage().setAlwaysOnTop(true);
                getStage().setFullScreen(true);
                getStage().show();

            } else {
                getStage().hide();
                getStage().setFullScreen(false);
            }
            return null;
        });
    }

    private Screen getScreen() {
        try {

            // setup display property
            JPOutput.Display display;
            try {
                display = JPService.getProperty(JPOutput.class).getValue();
            } catch (JPServiceException ex) {
                ExceptionPrinter.printHistory(new CouldNotPerformException("Could not detect output property! Try to use default display.", ex), logger);
                display = JPOutput.Display.PRIMARY;
            }

            // select screen
            switch (display) {
            case PRIMARY:
                return Screen.getPrimary();
            case SECONDARY:
                for (Screen screen : Screen.getScreens()) {
                    if (!screen.equals(Screen.getPrimary())) {
                        return screen;
                    }
                }
                throw new NotAvailableException(Screen.class, JPOutput.Display.SECONDARY.name());
            default:
                return Screen.getScreens().get(display.getId());
            }
        } catch (Exception ex) {
            ExceptionPrinter.printHistory(new CouldNotPerformException("Could not detect display! Use display 0 instead.", ex), logger);
            return Screen.getScreens().get(0);
        }
    }

    private Stage getStage() throws NotAvailableException {
        if (primaryStage == null) {
            throw new NotAvailableException(Stage.class);
        }
        return primaryStage;
    }

    private <V> Future<V> runTask(final Callable<V> callable) throws CouldNotPerformException {
        try {

            if (Platform.isFxApplicationThread()) {
                try {
                    CompletableFuture.completedFuture(callable.call());
                } catch (Exception ex) {
                    ExceptionPrinter.printHistory(new CouldNotPerformException("Could not perform task!", ex), logger);
                }
            }

            FutureTask<V> future = new FutureTask(() -> {
                try {
                    return callable.call();
                } catch (Exception ex) {
                    throw ExceptionPrinter.printHistoryAndReturnThrowable(ex, logger);
                }
            });
            Platform.runLater(future);
            return future;
        } catch (Exception ex) {
            throw new CouldNotPerformException("Could not perform task!", ex);
        }
    }

    public static void main(String[] args) {

        // Configure and parse command line properties
        JPService.setApplicationName("generic-display");
        JPService.registerProperty(JPBroadcastDisplayScope.class);
        JPService.registerProperty(JPDisplayScope.class);
        JPService.registerProperty(JPOutput.class);
        JPService.registerProperty(JPMessage.class);
        JPService.registerProperty(JPTabAmount.class);
        JPService.registerProperty(JPUrl.class);
        JPService.registerProperty(JPImageUrl.class);
        JPService.registerProperty(JPVisible.class);
        JPService.registerProperty(JPMessageType.class);
        JPService.parseAndExitOnError(args);

        // launch user interface
        launch(args);
    }
}
