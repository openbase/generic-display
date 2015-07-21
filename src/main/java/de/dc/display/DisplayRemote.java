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
package de.dc.display;

import de.citec.jul.exception.CouldNotPerformException;
import de.citec.jul.extension.rsb.com.RSBRemoteService;
import de.citec.jul.extension.rsb.com.RPCHelper;
import java.util.concurrent.Future;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import rst.homeautomation.unit.UnitConfigType.UnitConfig;

/**
 *
 * @author * @author <a href="mailto:DivineThreepwood@gmail.com">Divine Threepwood</a>
 */
public class DisplayRemote extends RSBRemoteService<UnitConfig> implements DisplayInterface {

    static {
        DefaultConverterRepository.getDefaultConverterRepository().addConverter(new ProtocolBufferConverter<>(UnitConfig.getDefaultInstance()));
    }

    @Override
    public void notifyUpdated(UnitConfig data) throws CouldNotPerformException {

    }

    @Override
    public Future showURL(String url) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(url, this);
    }

    @Override
    public Future showHTMLContent(String content) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(content, this);
    }

    @Override
    public Future showPreset(String presetId) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(presetId, this);
    }

}
