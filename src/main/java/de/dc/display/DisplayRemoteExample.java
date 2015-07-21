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
import de.citec.jul.extension.rsb.scope.jp.JPScope;
import java.util.concurrent.ExecutionException;
import rsb.Scope;

/**
 *
 * @author * @author <a href="mailto:DivineThreepwood@gmail.com">Divine Threepwood</a>
 */
public class DisplayRemoteExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws CouldNotPerformException, InterruptedException, ExecutionException {
        JPService.setApplicationName("display-remote");
        JPService.registerProperty(JPScope.class, new Scope("/home/display"));
        JPService.registerProperty(JPVerbose.class, true);
        JPService.parseAndExitOnError(args);

        DisplayRemote remote = new DisplayRemote();
        remote.init(JPService.getProperty(JPScope.class).getValue());
        remote.activate();

        while (!Thread.interrupted()) {
            remote.showURL("http://www.wunderground.com/cgi-bin/findweather/getForecast?query=bielefeld");
            Thread.sleep(10000);
            remote.showHTMLContent("<html lang=\"de\">\n"
                    + "    <head>\n"
                    + "        <title>Datum und Zeit</title>\n"
                    + "        <style>\n"
                    + "        html, body {\n"
                    + "            height: 100%;\n"
                    + "            margin: 0;\n"
                    + "            padding: 0;\n"
                    + "            width: 100%;\n"
                    + "            font-size: 300%;\n"
                    + "            font-family: Helvetica, Arial, sans-serif;\n"
                    + "            line-height: 150%;"
                    + "        }\n"
                    + "        body {\n"
                    + "            display: table;\n"
                    + "        }\n"
                    + "        .block {\n"
                    + "            text-align: center;\n"
                    + "            display: table-cell;\n"
                    + "            vertical-align: middle;\n"
                    + "        }\n"
                    + "        </style>\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "    <div class=\"block\">\n"
                    + "       <p id=\"dateAndTime\"></p>"
                    + "    </div>\n"
                    + "        <script type=\"text/javascript\">"
                    + "            var now = new Date();"
                    + "            document.getElementById('dateAndTime').innerHTML = now;"
                    + "        </script>"
                    + "    </body>\n"
                    + "</html>");
            Thread.sleep(10000);
            remote.showURL("http://www.postmedien.ch/wp-content/uploads/2014/08/Roboter-der-Zukunft-900x1600.jpg");
            Thread.sleep(10000);
        }
    }
}
