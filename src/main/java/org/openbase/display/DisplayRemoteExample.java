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

import org.openbase.display.jp.JPBroadcastDisplayScope;
import org.openbase.display.jp.JPDisplayScope;
import org.openbase.jps.core.JPService;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.printer.ExceptionPrinter;
import org.openbase.jul.pattern.Remote.ConnectionState;
import org.slf4j.LoggerFactory;
import org.openbase.type.configuration.EntryType.Entry;
import org.openbase.type.configuration.MetaConfigType.MetaConfig;
import org.openbase.type.configuration.MetaConfigType.MetaConfig.Builder;

import java.util.concurrent.ExecutionException;

/**
 * This is a example class which shows how to use the generic display remote.
 *
 * @author <a href="mailto:divine@openbase.org">Divine Threepwood</a>
 */
public class DisplayRemoteExample {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DisplayRemoteExample.class);

    public static final long DELAY = 3000;

    /**
     * @param args the command line arguments
     *
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        try {
            // Configure and parse command line properties
            JPService.setApplicationName("generic-display-test");
            JPService.registerProperty(JPBroadcastDisplayScope.class);
            JPService.registerProperty(JPDisplayScope.class, JPService.getProperty(JPBroadcastDisplayScope.class).getValue());
            JPService.parseAndExitOnError(args);

            // Init remote instance
            DisplayRemote remote = new DisplayRemote();
            remote.init();
            remote.activate();
            remote.waitForConnectionState(ConnectionState.CONNECTED);

            // Switch displays on
            remote.setVisible(true);

            // Some example calls in a loop.
            while (!Thread.interrupted()) {

                // Display URL example and wait until done
                remote.showUrl("http://basecubeone.org").get();


                Thread.sleep(DELAY);

                // Display URL example, force reload the content and wait until done
                remote.showUrlAndReload("http://openbase.org").get();

                // Display HTML content example
                remote.showHtmlContent("<html lang=\"de\">"
                        + "    <head>"
                        + "        <title>Datum und Zeit</title>"
                        + "        <style>"
                        + "        html, body {"
                        + "            height: 100%;"
                        + "            margin: 0;"
                        + "            padding: 0;"
                        + "            width: 100%;"
                        + "            font-size: 300%;"
                        + "            font-family: Helvetica, Arial, sans-serif;"
                        + "            line-height: 150%;"
                        + "        }"
                        + "        body {"
                        + "            display: table;"
                        + "        }"
                        + "        .block {"
                        + "            text-align: center;"
                        + "            display: table-cell;"
                        + "            vertical-align: middle;"
                        + "        }"
                        + "        </style>"
                        + "    </head>"
                        + "    <body>"
                        + "    <div class=\"block\">"
                        + "       <p id=\"dateAndTime\"></p>"
                        + "    </div>"
                        + "        <script type=\"text/javascript\">"
                        + "            var now = new Date();"
                        + "            document.getElementById('dateAndTime').innerHTML = now;"
                        + "        </script>"
                        + "    </body>"
                        + "</html>").get();

                Thread.sleep(DELAY);

                // Display text example
                remote.showText("Text").get();

                Thread.sleep(DELAY);

                // Display warn text example
                remote.showWarnText("Warn").get();

                Thread.sleep(DELAY);

                // Display eroor text example
                remote.showErrorText("Error").get();

                Thread.sleep(DELAY);

                // Display info text example
                remote.showInfoText("Info").get();

                Thread.sleep(DELAY);

                // Set visible false example
                remote.showWarnText("Disable display...").get();

                Thread.sleep(DELAY / 3);
                remote.setVisible(false);
                Thread.sleep(DELAY);

                // Set visible true example
                remote.showInfoText("Display enabled!").get();

                remote.setVisible(true);
                Thread.sleep(DELAY);

                // Show UHD Image
                remote.showImage("http://previews.123rf.com/images/mazirama/mazirama1408/mazirama140800293/30486102-UHD-User-Help-Desk-text-concept-on-green-digital-world-map-background--Stock-Photo.jpg").get();

                Thread.sleep(DELAY * 2);

                // Show UHD Image
                remote.showImage("http://orig09.deviantart.net/4343/f/2015/211/c/0/the_international_2015_banners_by_goldenhearted-d93eop6.jpg").get();

                Thread.sleep(DELAY * 2);

                // Show UHD Image
                remote.showImage("http://uhd-wallpapers.net/images/venice-grand-canal_542.jpeg").get();

                Thread.sleep(DELAY * 2);

                // Show Template
                MetaConfig metaConfig = MetaConfig.newBuilder()
                        .addEntry(Entry.newBuilder().setKey("TEMPLATE").setValue("IMAGE_TEXT_TEXT_VIEW"))
                        .addEntry(Entry.newBuilder().setKey("IMAGE_URL").setValue("https://heise.cloudimg.io/width/700/q75.png-lossy-75.webp-lossy-75.foil1/_www-heise-de_/imgs/18/2/3/5/1/3/7/9/2_Hey-Guys-2f1a8016eb56480d.jpeg"))
                        .addEntry(Entry.newBuilder().setKey("TEXT_TOP").setValue("How to control lights?"))
                        .addEntry(Entry.newBuilder().setKey("TEXT_BOTTOM").setValue("Raise your hand and spread your fingers."))
                        .build();
                remote.showTemplate(metaConfig).get();
                Thread.sleep(DELAY * 2);

                // All commands can be optionally send in broadcast mode to reach all displays simultaneously.
                //
                // e.g. remote.broadcast().setText("send to all instances.");
                remote.broadcast().setText("Press ESC to exit the fullscreen mode :)").get();
                Thread.sleep(DELAY);

                // Just remove the ".get()" suffix if you don't want to wait for a server confirmation.
            }

            // Switch displays off
            remote.setVisible(false);
        } catch (CouldNotPerformException | ExecutionException ex) {
            ExceptionPrinter.printHistory("Error while performing example code.", ex, LOGGER);
        }
    }
}
