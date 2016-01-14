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

import org.dc.jul.extension.rsb.scope.jp.JPScope;
import rsb.Scope;

/**
 *
 * @author <a href="mailto:mpohling@cit-ec.uni-bielefeld.de">Divine Threepwood</a>
 */
public class JPBroadcastDisplayScope extends JPScope {

    public final static String[] COMMAND_IDENTIFIERS = {"--broadcast-scope"};

    public JPBroadcastDisplayScope() {
        super(COMMAND_IDENTIFIERS);
    }

    @Override
    protected Scope getPropertyDefaultValue() {
        return super.getPropertyDefaultValue().concat(new Scope("/app/display/broadcast"));
    }

    @Override
    public String getDescription() {
        return "Defines the rsb scope of the generic display which is used for broadcast communication to reach all displays simultaneously.";
    }
}
