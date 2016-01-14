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

import org.dc.jps.preset.AbstractJPString;

/**
 *
 * @author <a href="mailto:mpohling@cit-ec.uni-bielefeld.de">Divine Threepwood</a>
 */
public class JPImageUrl extends AbstractJPString {

    public static final String[] COMMANDIDENTIFIER = { "--image"};

    public JPImageUrl() {
        super(COMMANDIDENTIFIER);
    }

    @Override
    protected String getPropertyDefaultValue() {
        return "NoImage";
    }

    @Override
    public String getDescription() {
        return "Property can be used to specify a image url to display.";
    }
}
