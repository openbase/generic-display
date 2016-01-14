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

import org.dc.display.jp.JPMessageType.MessageType;
import org.dc.jps.preset.AbstractJPEnum;

/**
 *
 * @author <a href="mailto:mpohling@cit-ec.uni-bielefeld.de">Divine Threepwood</a>
 */
public class JPMessageType extends AbstractJPEnum<MessageType> {

    public enum MessageType {

        UNKNOWN, STANDARD, INFO, ERROR, WARNING
    };

    public static final String[] COMMANDIDENTIFIER = {"-t", "--message-type"};

    public JPMessageType() {
        super(COMMANDIDENTIFIER);
    }

    @Override
    protected MessageType getPropertyDefaultValue() {
        return MessageType.STANDARD;
    }

    @Override
    public String getDescription() {
        return "Configures the type of a message.";
    }

}
