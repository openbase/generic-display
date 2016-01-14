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

import org.dc.jps.preset.AbstractJPEnum;

/**
 *
 * @author <a href="mailto:mpohling@cit-ec.uni-bielefeld.de">Divine Threepwood</a>
 */
public class JPOutput extends AbstractJPEnum<JPOutput.Display> {

    public enum Display {
        PRIMARY,
        SECONDARY,
        D0 (0),
        D1 (1),
        D2 (2),
        D3 (3);

        private final int id;

        private Display() {
            this(-1);
        }
        private Display(final int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public static final String[] COMMANDIDENTIFIER = { "--out"};

    public JPOutput() {
        super(COMMANDIDENTIFIER);
    }

    @Override
    protected Display getPropertyDefaultValue() {
        return Display.PRIMARY;
    }

    @Override
    public String getDescription() {
        return "Property can be used to specify the output display with is used for fullscreen mode.";
    }
}
