package org.openbase.display.jp;

/*
 * #%L
 * GenericDisplay
 * %%
 * Copyright (C) 2015 - 2021 openbase.org
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


import org.openbase.jps.exception.JPNotAvailableException;
import org.openbase.jul.communication.controller.jp.JPScope;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.extension.type.processing.ScopeProcessor;
import org.openbase.type.communication.ScopeType.Scope;

/**
 *
 * @author <a href="mailto:divine@openbase.org">Divine Threepwood</a>
 */
public class JPBroadcastDisplayScope extends JPScope {

    public final static String[] COMMAND_IDENTIFIERS = {"--broadcast-scope"};

    public JPBroadcastDisplayScope() {
        super(COMMAND_IDENTIFIERS);
    }

    @Override
    protected Scope getPropertyDefaultValue() throws JPNotAvailableException {
        try {
            return ScopeProcessor.generateScope("/app/display/broadcast");
        } catch (CouldNotPerformException ex) {
            throw new JPNotAvailableException(JPScope.class, ex);
        }
    }

    @Override
    public String getDescription() {
        return "Defines the rsb scope of the generic display which is used for broadcast communication to reach all displays simultaneously.";
    }
}
