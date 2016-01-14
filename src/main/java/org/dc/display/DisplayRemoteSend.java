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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.dc.display.jp.JPBroadcastDisplayScope;
import org.dc.display.jp.JPDisplayScope;
import org.dc.display.jp.JPImageUrl;
import org.dc.display.jp.JPMessage;
import org.dc.display.jp.JPMessageType;
import org.dc.display.jp.JPOutput;
import org.dc.display.jp.JPUrl;
import org.dc.display.jp.JPVisible;
import org.dc.jps.core.JPService;
import org.dc.jps.exception.JPServiceException;
import org.dc.jps.preset.JPHelp;
import org.dc.jul.exception.CouldNotPerformException;
import org.dc.jul.exception.printer.ExceptionPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a simple commandline remote to display text on a configured generic display.
 *
 * @author * @author <a href="mailto:DivineThreepwood@gmail.com">Divine Threepwood</a>
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
     * Method handles parsed jps properties.
     *
     * @param printWarning if the an error will be printed if no properties are parsed.
     * @throws CouldNotPerformException
     * @throws InterruptedException
     */
    public static void handleProperties(final boolean printWarning) throws CouldNotPerformException, InterruptedException {
        try {
            // Init remote instance
            DisplayRemote remote = new DisplayRemote();
            remote.init();
            remote.activate();

            if (JPService.getProperty(JPMessage.class).isParsed()) {

                // Display given Message
                String message = JPService.getProperty(JPMessage.class).getValue();

                switch (JPService.getProperty(JPMessageType.class).getValue()) {
                    case ERROR:
                        remote.showErrorText(message).get(TIMEOUT, TimeUnit.SECONDS);
                        break;
                    case WARNING:
                        remote.showWarnText(message).get(TIMEOUT, TimeUnit.SECONDS);
                        break;
                    case INFO:
                        remote.showInfoText(message).get(TIMEOUT, TimeUnit.SECONDS);
                        break;
                    default:
                        remote.showText(message).get(TIMEOUT, TimeUnit.SECONDS);
                        break;
                }
            } else if (JPService.getProperty(JPUrl.class).isParsed()) {
                remote.showURL(JPService.getProperty(JPUrl.class).getValue()).get(TIMEOUT, TimeUnit.SECONDS);;
            } else if (JPService.getProperty(JPVisible.class).isParsed()) {
                remote.setVisible(JPService.getProperty(JPVisible.class).getValue()).get(TIMEOUT, TimeUnit.SECONDS);;
            } else if (JPService.getProperty(JPImageUrl.class).isParsed()) {
                remote.showImage(JPService.getProperty(JPImageUrl.class).getValue()).get(TIMEOUT, TimeUnit.SECONDS);;
            } else if (printWarning) {
                logger.warn("No arguments given!");
                logger.info("Please type \"" + JPService.getApplicationName() + " " + JPHelp.COMMAND_IDENTIFIERS[0] + "\" to get more informations.");
            }
        } catch (CouldNotPerformException | JPServiceException | ExecutionException | TimeoutException ex) {
            throw new CouldNotPerformException("Could not handle properties!", ex);
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        // Configure and parse command line properties
        JPService.setApplicationName("generic-display-send");
        JPService.registerProperty(JPDisplayScope.class);
        JPService.registerProperty(JPBroadcastDisplayScope.class);
        JPService.registerProperty(JPMessage.class);
        JPService.registerProperty(JPUrl.class);
        JPService.registerProperty(JPImageUrl.class);
        JPService.registerProperty(JPVisible.class);
        JPService.registerProperty(JPMessageType.class);
        JPService.registerProperty(JPOutput.class);
        JPService.parseAndExitOnError(args);

        try {
            handleProperties(true);
        } catch (CouldNotPerformException ex) {
            ExceptionPrinter.printHistory(ex, logger);
            System.exit(1);
        }
        System.exit(0);
    }
}
