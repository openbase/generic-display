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
package org.dc.display.jp;

import org.dc.jps.preset.AbstractJPString;

/**
 *
 * @author <a href="mailto:mpohling@cit-ec.uni-bielefeld.de">Divine Threepwood</a>
 */
public class JPMessage extends AbstractJPString {

    public static final String[] COMMANDIDENTIFIER = {"-m", "--message"};
    
    public JPMessage() {
        super(COMMANDIDENTIFIER);
    }
    
    @Override
    protected String getPropertyDefaultValue() {
        return "This is a test message!";
    }

    @Override
    public String getDescription() {
        return "Property can be used to specify any message to send.";
    }
}
