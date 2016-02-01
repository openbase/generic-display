package org.dc.display;

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

import org.dc.jul.exception.CouldNotPerformException;
import org.dc.jul.exception.InstantiationException;
import org.dc.jul.extension.rsb.com.RPCHelper;
import org.dc.jul.extension.rsb.com.RSBCommunicationService;
import org.dc.jul.extension.rsb.iface.RSBLocalServerInterface;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import rst.homeautomation.unit.UnitConfigType;
import rst.homeautomation.unit.UnitConfigType.UnitConfig;

/**
 *
 * @author * @author <a href="mailto:DivineThreepwood@gmail.com">Divine Threepwood</a>
 */
public class DisplayServer extends RSBCommunicationService<UnitConfigType.UnitConfig, UnitConfigType.UnitConfig.Builder> {

    static {
        DefaultConverterRepository.getDefaultConverterRepository().addConverter(new ProtocolBufferConverter<>(UnitConfig.getDefaultInstance()));
    }

    private final Display display;

    public DisplayServer(final Display display) throws InstantiationException, CouldNotPerformException {
        super(UnitConfig.newBuilder());
        this.display = display;
    }

    /**
     * {@inheritDoc}
     *
     * @param server
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public void registerMethods(final RSBLocalServerInterface server) throws CouldNotPerformException {
        RPCHelper.registerInterface(Display.class, display, server);
    }
}
