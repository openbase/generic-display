package org.openbase.display;

/*
 * #%L
 * GenericDisplay
 * %%
 * Copyright (C) 2015 - 2017 openbase.org
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

/**
 * This is a example class which shows how to use the generic display remote.
 *
 * @author <a href="mailto:divine@openbase.org">Divine Threepwood</a>
 */
public class DisplayRemoteExample {

    public static final long DELAY = 3000;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        // Configure and parse command line properties
        JPService.setApplicationName("generic-display-test");
        JPService.registerProperty(JPBroadcastDisplayScope.class);
        JPService.registerProperty(JPDisplayScope.class, JPService.getProperty(JPBroadcastDisplayScope.class).getValue());
        JPService.parseAndExitOnError(args);

        // Init remote instance
        DisplayRemote remote = new DisplayRemote();
        remote.init();
        remote.activate();

        // Switch displays on
        remote.setVisible(true);

        // Some example calls in a loop.
        while (!Thread.interrupted()) {

            // All following commands can be optinally send in broadcast mode to reach all displays simultaneously.
            //
            // e.g. remote.broadcast().setText("send to all instances.");

            remote.broadcast().setText("send to all instances.");

            // Display URL example
            remote.showURL("http://www.wunderground.com/cgi-bin/findweather/getForecast?query=bielefeld");
            Thread.sleep(DELAY);

            // Display HTML content example
            remote.broadcast().showHTMLContent("<html lang=\"de\">"
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
                    + "</html>");
            Thread.sleep(DELAY);

            // Display text example
            remote.showText("Text");
            Thread.sleep(DELAY);

            // Display warn text example
            remote.showWarnText("Warn");
            Thread.sleep(DELAY);

            // Display eroor text example
            remote.showErrorText("Error");
            Thread.sleep(DELAY);

            // Display info text example
            remote.showInfoText("Info");
            Thread.sleep(DELAY);

            // Set visible false example
            remote.showWarnText("Disable display...");
            Thread.sleep(DELAY/3);
            remote.setVisible(false);
            Thread.sleep(DELAY);

            // Set visible true example
            remote.showInfoText("Display enabled!");
            remote.setVisible(true);
            Thread.sleep(DELAY);

            // Show UHD Image
            remote.showImage("http://www.geckohomecinema.co.uk/wp-content/uploads/2014/06/UHD-resolution_201309231.jpg");
            Thread.sleep(DELAY*2);

            // Show UHD Image
            remote.showImage("http://previews.123rf.com/images/mazirama/mazirama1408/mazirama140800293/30486102-UHD-User-Help-Desk-text-concept-on-green-digital-world-map-background--Stock-Photo.jpg");
            Thread.sleep(DELAY*2);

            // Show UHD Image
            remote.showImage("http://orig09.deviantart.net/4343/f/2015/211/c/0/the_international_2015_banners_by_goldenhearted-d93eop6.jpg");
            Thread.sleep(DELAY*2);

            // Show UHD Image
            remote.showImage("http://architektur.mapolismagazin.com/sites/default/files/null/porta-fira_6.jpg");
            Thread.sleep(DELAY*2);
        }

        // Switch displays off
        remote.setVisible(false);
    }
}
