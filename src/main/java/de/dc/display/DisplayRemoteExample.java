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

import de.dc.jp.JPGenericDisplayScope;
import org.dc.jps.core.JPService;

/**
 * This is a example class which shows how to use the generic display remote. 
 * 
 * @author * @author <a href="mailto:DivineThreepwood@gmail.com">Divine Threepwood</a>
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
        JPService.registerProperty(JPGenericDisplayScope.class);
        JPService.parseAndExitOnError(args);
        
        // Init remote instance
        DisplayRemote remote = new DisplayRemote();
        remote.init();
        remote.activate();
        
        // Some example calls in a loop.
        while (!Thread.interrupted()) {
            
            // Display URL example
            remote.showURL("http://www.wunderground.com/cgi-bin/findweather/getForecast?query=bielefeld");
            Thread.sleep(DELAY);
            
            // Display HTML content example
            remote.showHTMLContent("<html lang=\"de\">"
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
        }
    }
}
