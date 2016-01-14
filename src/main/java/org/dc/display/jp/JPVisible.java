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
package org.dc.display.jp;

import org.dc.jps.exception.JPNotAvailableException;
import org.dc.jps.preset.AbstractJPBoolean;

/**
 *
 * @author <a href="mailto:mpohling@cit-ec.uni-bielefeld.de">Divine Threepwood</a>
 */
public class JPVisible extends AbstractJPBoolean {

    public static final String[] COMMANDIDENTIFIER = {"--visible"};

    public JPVisible() {
        super(COMMANDIDENTIFIER);
    }

    @Override
    protected Boolean getPropertyDefaultValue() throws JPNotAvailableException {
        return true;
    }



    @Override
    public String getDescription() {
        return "Property can be used to make the screen visible in fullscreen mode or hide the overall display.";
    }
}
