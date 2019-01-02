package org.openbase.display;

/*
 * #%L
 * GenericDisplay
 * %%
 * Copyright (C) 2015 - 2019 openbase.org
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.openbase.display.jp.*;
import org.openbase.jps.core.JPService;
import org.openbase.jps.exception.JPServiceException;
import org.openbase.jps.preset.JPHelp;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.printer.ExceptionPrinter;
import org.openbase.jul.pattern.Remote.ConnectionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a simple commandline remote to display text on a configured generic display.
 *
 * @author <a href="mailto:divine@openbase.org">Divine Threepwood</a>
 */
public class DisplayRemoteSend {

    /**
     * Local class logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(DisplayRemoteSend.class);

    /**
     * Action timeout (TimeUnit.SECONDS).
     */
    public static final long TIMEOUT = 30;

    /**
     * Method handles the action defined by the parsed jps properties.
     *
     * @param printWarning if the an error will be printed if no properties are parsed.
     * @throws CouldNotPerformException
     * @throws InterruptedException
     */
    public static void handleAction(final boolean printWarning) throws CouldNotPerformException, InterruptedException {
        // Init remote instance
        DisplayRemote remote = new DisplayRemote();

        try {
            remote.init();
            remote.activate();
            remote.waitForConnectionState(ConnectionState.CONNECTED, 5000);
            handleAction(remote, printWarning);
        } catch (CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not handle action!", ex);
        } finally {
            remote.shutdown();
        }
    }

    /**
     * Method handles the action defined by the parsed jps properties.
     *
     * @param display display is used to perform the changes.
     * @param printWarning if the an error will be printed if no properties are parsed.
     * @throws CouldNotPerformException
     * @throws InterruptedException
     */
    public static void handleAction(final Display display, final boolean printWarning) throws CouldNotPerformException, InterruptedException {
        try {

            if (JPService.getProperty(JPCloseAll.class).getValue()) {
                display.closeAll().get(TIMEOUT, TimeUnit.SECONDS);
            }

            if (JPService.getProperty(JPMessage.class).isParsed()) {

                // Display given Message
                String message = JPService.getProperty(JPMessage.class).getValue();

                switch (JPService.getProperty(JPMessageType.class).getValue()) {
                    case ERROR:
                        display.showErrorText(message).get(TIMEOUT, TimeUnit.SECONDS);
                        break;
                    case WARNING:
                        display.showWarnText(message).get(TIMEOUT, TimeUnit.SECONDS);
                        break;
                    case INFO:
                        display.showInfoText(message).get(TIMEOUT, TimeUnit.SECONDS);
                        break;
                    default:
                        display.showText(message).get(TIMEOUT, TimeUnit.SECONDS);
                        break;
                }
            } else if (JPService.getProperty(JPUrl.class).isParsed()) {
                if(JPService.getProperty(JPReload.class).getValue()) {
                    display.showUrlAndReload(JPService.getProperty(JPUrl.class).getValue()).get(TIMEOUT, TimeUnit.SECONDS);
                } else {
                    display.showUrl(JPService.getProperty(JPUrl.class).getValue()).get(TIMEOUT, TimeUnit.SECONDS);
                }
            } else if (JPService.getProperty(JPVisible.class).isParsed()) {
                display.setVisible(JPService.getProperty(JPVisible.class).getValue()).get(TIMEOUT, TimeUnit.SECONDS);;
            } else if (JPService.getProperty(JPImageUrl.class).isParsed()) {
                display.showImage(JPService.getProperty(JPImageUrl.class).getValue()).get(TIMEOUT, TimeUnit.SECONDS);;
            } else if (!JPService.getProperty(JPCloseAll.class).getValue() && printWarning) {
                logger.warn("No arguments given!");
                logger.info("Please type \"" + JPService.getApplicationName() + " " + JPHelp.COMMAND_IDENTIFIERS[0] + "\" to get more informations.");
            }
        } catch (CouldNotPerformException | JPServiceException | ExecutionException | TimeoutException ex) {
            throw new CouldNotPerformException("Could not handle action!", ex);
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        // Configure and parse command line properties
        JPService.setApplicationName("generic-display-send");
        JPService.registerProperty(JPBroadcastDisplayScope.class);
        JPService.registerProperty(JPDisplayScope.class, JPService.getProperty(JPBroadcastDisplayScope.class).getValue());
        JPService.registerProperty(JPMessage.class);
        JPService.registerProperty(JPUrl.class);
        JPService.registerProperty(JPImageUrl.class);
        JPService.registerProperty(JPVisible.class);
        JPService.registerProperty(JPReload.class);
        JPService.registerProperty(JPMessageType.class);
        JPService.registerProperty(JPOutput.class);
        JPService.registerProperty(JPCloseAll.class);
        JPService.parseAndExitOnError(args);

        try {
            handleAction(true);
        } catch (CouldNotPerformException ex) {
            ExceptionPrinter.printHistory(ex, logger);
            System.exit(1);
        }
        System.exit(0);
    }
}
