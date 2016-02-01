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
