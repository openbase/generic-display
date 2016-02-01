package org.dc.display.jp;

/*
 * #%L
 * GenericDisplay
 * %%
 * Copyright (C) 2015 - 2016 DivineCooperation
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
