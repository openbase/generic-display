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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.lang.builder.HashCodeBuilder;

import static org.openbase.display.DisplayRemoteSend.handleAction;

import org.openbase.display.HTMLLoader.Template;
import org.openbase.display.jp.*;
import org.openbase.jps.core.JPService;
import org.openbase.jps.exception.JPServiceException;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.InitializationException;
import org.openbase.jul.exception.InstantiationException;
import org.openbase.jul.exception.NotAvailableException;
import org.openbase.jul.exception.printer.ExceptionPrinter;
import org.openbase.jul.extension.type.processing.MetaConfigVariableProvider;
import org.openbase.jul.processing.StringProcessor;
import org.openbase.jul.schedule.FutureProcessor;
import org.openbase.jul.schedule.GlobalCachedExecutorService;
import org.slf4j.LoggerFactory;
import org.openbase.type.configuration.MetaConfigType.MetaConfig;

/**
 * @author <a href="mailto:divine@openbase.org">Divine Threepwood</a>
 */
public class DisplayView extends Application implements Display {

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(DisplayView.class);

    public static final int AMOUNT_OF_TAB_FALLBACK = 10;

    private final Object TAB_LOCK = new Object();

    private DisplayServer broadcastServer, displayServer;
    private Stage primaryStage;
    private final int maxTabAmount;

    private final HashMap<Integer, WebTab> webTabMap;
    private final ConcurrentLinkedQueue<WebTab> webTabUsageQueue;
    private final HTMLLoader htmlLoader;
    private final StackPane stackPane;

    public DisplayView() throws InstantiationException {
        try {
            this.webTabMap = new HashMap<>();
            this.webTabUsageQueue = new ConcurrentLinkedQueue<>();
            this.htmlLoader = new HTMLLoader();
            this.stackPane = new StackPane();

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

            Scene scene = new Scene(stackPane);

            // configure hide key combination
            final KeyCombination escapeKey = new KeyCodeCombination(KeyCode.ESCAPE);
            scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent event) {
                    if (escapeKey.match(event)) {
                        try {
                            setVisible(false).get();
                        } catch (InterruptedException ex) {
                            return;
                        } catch (ExecutionException ex) {
                            ExceptionPrinter.printHistory(new CouldNotPerformException("Could not execute key event!", ex), logger);
                        }
                    }
                }
            });

            primaryStage.setScene(scene);

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
        GlobalCachedExecutorService.submit(() -> {
            try {
                handleAction(this, false);
            } catch (CouldNotPerformException | InterruptedException ex) {
                ExceptionPrinter.printHistory(ex, logger);
            }
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        displayServer.shutdown();
        broadcastServer.shutdown();
        synchronized (TAB_LOCK) {
            webTabMap.values().forEach(tab -> {
                tab.shutdown();
            });
        }
    }

    private int getHash(final String context) {
        return new HashCodeBuilder().append(context).toHashCode();
    }

    private WebTab loadWebEngine(final String context) {
        synchronized (TAB_LOCK) {
            assert getHash(context) == getHash(context);
            int contextHash = getHash(context);
            WebTab webTab;

            // create new tab or load existing one if content is not already displayed.
            if (!webTabMap.containsKey(contextHash)) {

                // check if all tabs are in use.
                if (webTabMap.size() >= maxTabAmount) { // recover outdated tab
                    // get outdated tab
                    webTab = webTabUsageQueue.poll();
                    // update context hash
                    webTabMap.remove(webTab.getContentHash());
                    webTab.updateContextHash(contextHash);

                    webTabMap.put(contextHash, webTab);
                } else { // create new tab
                    webTabMap.put(contextHash, new WebTab(contextHash, stackPane));
                }
            }

            // restore tab
            webTab = webTabMap.get(contextHash);

            // add tab to queue tail so it will not be reused
            if (webTabUsageQueue.contains(webTab)) {
                webTabUsageQueue.remove(webTab);
            }
            webTabUsageQueue.offer(webTab);
            return webTab;
        }
    }

    private Future<Void> displayHTML(final String html, boolean show, final boolean reload) {
        return runTask(() -> {
            loadWebEngine(html).loadContent(html, reload);
            if (show) {
                setVisible(show);
            }
            return null;
        });
    }

    private Future<Void> displayURL(final String url, boolean show, final boolean reload) {
        return runTask(() -> {
            loadWebEngine(url).load(url, reload);
            if (show) {
                setVisible(show);
            }
            return null;
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param url {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showUrlAndReload(String url) {
        logger.info("show url and reload: " + url);
        return displayURL(url, true, true);
    }

    /**
     * {@inheritDoc}
     *
     * @param content {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showHtmlContentAndReload(String content) {
        logger.info("show html content and reload: " + toSingleLine(content));
        return displayHTML(content, true, true);
    }

    /**
     * {@inheritDoc}
     *
     * @param url {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showUrl(final String url) {
        logger.info("show url: " + url);
        return displayURL(url, true, false);
    }

    /**
     * {@inheritDoc}
     *
     * @param content {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showHtmlContent(final String content) {
        logger.info("show html content: " + toSingleLine(content));
        return displayHTML(content, true, false);
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showInfoText(final String presetId) {
        logger.info("show info text: " + presetId);
        try {
            return displayHTML(htmlLoader.loadTextView(presetId, Color.FORESTGREEN.darker()), true, false);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showWarnText(final String presetId) {
        logger.info("show warning text: " + presetId);
        try {
            return displayHTML(htmlLoader.loadTextView(presetId, Color.ORANGE), true, false);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showErrorText(final String presetId) {
        logger.info("show error text: " + presetId);
        try {
            return displayHTML(htmlLoader.loadTextView(presetId, Color.RED.darker()), true, false);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showText(final String presetId) {
        logger.info("show text: " + presetId);
        try {
            return displayHTML(htmlLoader.loadTextView(presetId, Color.BLACK), true, false);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param image {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showImage(final String image) {
        logger.info("show image: " + image);
        try {
            return displayHTML(htmlLoader.loadImageView(image), true, false);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param url {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setUrl(final String url) {
        logger.info("set url: " + url);
        return displayURL(url, false, false);
    }

    /**
     * {@inheritDoc}
     *
     * @param content {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setHtmlContent(final String content) {
        logger.info("set html content: " + toSingleLine(content));
        return displayHTML(content, false, false);
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setInfoText(final String presetId) {
        logger.info("set info text: " + presetId);
        try {
            return displayHTML(htmlLoader.loadTextView(presetId, Color.FORESTGREEN.darker()), false, false);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setWarnText(final String presetId) {
        logger.info("set warning text: " + presetId);
        try {
            return displayHTML(htmlLoader.loadTextView(presetId, Color.ORANGE), false, false);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setErrorText(final String presetId) {
        logger.info("set error text: " + presetId);
        try {
            return displayHTML(htmlLoader.loadTextView(presetId, Color.RED.darker()), false, false);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param presetId {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setText(final String presetId) {
        logger.info("set text: " + presetId);
        try {
            return displayHTML(htmlLoader.loadTextView(presetId, Color.BLACK), false, false);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param image {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setImage(final String image) {
        logger.info("set image:" + image);
        try {
            return displayHTML(htmlLoader.loadImageView(image), false, false);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param visible {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setVisible(final Boolean visible) {
        return runTask(() -> {
            if (visible) {
                logger.info("show display");
                Screen screen = getScreen();
                Stage stage = getStage();
                stage.setX(screen.getVisualBounds().getMinX());
                stage.setY(screen.getVisualBounds().getMinY());
                stage.setHeight(screen.getVisualBounds().getHeight());
                stage.setWidth(screen.getVisualBounds().getWidth());
                if (!getStage().isFocused() || !getStage().isShowing()) {
                    getStage().setFullScreen(false);
                    if (!getStage().isAlwaysOnTop()) {
                        getStage().setAlwaysOnTop(true);
                    }
                    getStage().setFullScreen(true);
                    getStage().show();
                }
            } else {
                logger.info("hide display");
                getStage().hide();
                getStage().setFullScreen(false);
            }
            return null;
        });
    }

    @Override
    public Future<Void> setTemplate(MetaConfig metaConfig) {
        try {
            final Template template;
            try {
                template = Template.valueOf(new MetaConfigVariableProvider("passed parameters", metaConfig).getValue(Template.KEY_TEMPLATE));
            } catch (IllegalArgumentException | CouldNotPerformException ex) {
                throw new CouldNotPerformException("Could not resolve template!", ex);
            }
            logger.info("set template:" + template.name());
            return displayHTML(htmlLoader.loadTemplateView(template, metaConfig, false), false, false);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    @Override
    public Future<Void> showTemplate(MetaConfig metaConfig) {
        try {
            final Template template;
            try {
                template = Template.valueOf(new MetaConfigVariableProvider("passed parameters", metaConfig).getValue(Template.KEY_TEMPLATE));
            } catch (IllegalArgumentException | CouldNotPerformException ex) {
                throw new CouldNotPerformException("Could not resolve template!", ex);
            }
            logger.info("set template:" + template.name());
            return displayHTML(htmlLoader.loadTemplateView(template, metaConfig, false), true, false);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> closeAll() {
        return runTask(() -> {
            logger.info("close all");
            setVisible(false).get();
            synchronized (TAB_LOCK) {
                for (WebTab webTab : new ArrayList<>(webTabMap.values())) {
                    webTab.getEngine().getLoadWorker().cancel();
                    webTab.getEngine().load(null);
                    webTabMap.remove(webTab.getContentHash());
                    webTab.updateContextHash(0);
                }
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

    private <V> Future<V> runTask(final Callable<V> callable) {
        try {

            if (Platform.isFxApplicationThread()) {
                try {
                    return CompletableFuture.completedFuture(callable.call());
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
            return FutureProcessor.canceledFuture(new CouldNotPerformException("Could not perform task!", ex));
        }
    }

    private String toSingleLine(String input) {
        return StringProcessor.removeDoubleWhiteSpaces(input.replace("\n", " "));
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
