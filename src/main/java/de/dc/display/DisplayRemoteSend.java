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
import de.dc.jp.JPMessage;
import de.dc.jp.JPMessageType;

/**
 * This is a simple commandline remote to display text on a configured generic display.
 *
 * @author * @author <a href="mailto:DivineThreepwood@gmail.com">Divine Threepwood</a>
 */
public class DisplayRemoteSend {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        // Configure and parse command line properties
        JPService.setApplicationName("generic-display-send");
        JPService.registerProperty(JPGenericDisplayScope.class);
        JPService.registerProperty(JPMessage.class);
        JPService.registerProperty(JPMessageType.class);
        JPService.parseAndExitOnError(args);

        // Init remote instance
        DisplayRemote remote = new DisplayRemote();
        remote.init();
        remote.activate();
        
        // Display given Message
        
        String message = JPService.getProperty(JPMessage.class).getValue();
        
        switch(JPService.getProperty(JPMessageType.class).getValue()) {
            case ERROR:
                remote.showErrorText(message);
                break;
            case WARNING:
                remote.showWarnText(message);
                break;
            case INFO:
                remote.showInfoText(message);
                break;
            default:
                remote.showText(message);
                break;
        }
        
        System.exit(0);
    }
}
