package org.openbase.display.jp;

/*
 * #%L
 * GenericDisplay
 * %%
 * Copyright (C) 2015 - 2020 openbase.org
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

import org.openbase.jps.preset.AbstractJPString;

/**
 *
 * @author <a href="mailto:divine@openbase.org">Divine Threepwood</a>
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
