/**
 * This file is part of GenericDisplay.
 *
 * GenericDisplay is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GenericDisplay is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GenericDisplay.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * This file is part of GenericDisplay.
 *
 * GenericDisplay is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * GenericDisplay is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with GenericDisplay. If not, see <http://www.gnu.org/licenses/>.
 */
package de.dc.display;

import de.citec.jul.exception.CouldNotPerformException;
import de.citec.jul.exception.InstantiationException;
import de.citec.jul.extension.rsb.com.RSBCommunicationService;
import de.citec.jul.extension.rsb.iface.RSBLocalServerInterface;
import de.citec.jul.extension.rsb.com.RPCHelper;
import java.util.concurrent.Future;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import rst.homeautomation.unit.UnitConfigType;
import rst.homeautomation.unit.UnitConfigType.UnitConfig;

/**
 *
 * @author * @author <a href="mailto:DivineThreepwood@gmail.com">Divine Threepwood</a>
 */
public class DisplayServer extends RSBCommunicationService<UnitConfigType.UnitConfig, UnitConfigType.UnitConfig.Builder> implements DisplayInterface {

    static {
        DefaultConverterRepository.getDefaultConverterRepository().addConverter(new ProtocolBufferConverter<>(UnitConfig.getDefaultInstance()));
    }

    private DisplayInterface display;

    public DisplayServer(final DisplayInterface display) throws InstantiationException, CouldNotPerformException {
        super(UnitConfig.newBuilder());
        this.display = display;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerMethods(RSBLocalServerInterface server) throws CouldNotPerformException {
        RPCHelper.registerInterface(DisplayInterface.class, this, server);
    }

    public DisplayInterface getDisplay() {
        return display;
    }

    public void setDisplay(DisplayInterface display) {
        this.display = display;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> showURL(String url) throws CouldNotPerformException {
        return display.showURL(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> showHTMLContent(String context) throws CouldNotPerformException {
        return display.showHTMLContent(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> showPreset(String presetId) throws CouldNotPerformException {
        return display.showPreset(presetId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> showInfoText(String text) throws CouldNotPerformException {
        return display.showInfoText(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> showWarnText(String text) throws CouldNotPerformException {
        return display.showWarnText(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> showErrorText(String text) throws CouldNotPerformException {
        return display.showErrorText(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> showText(String text) throws CouldNotPerformException {
        return display.showText(text);
    }

}
