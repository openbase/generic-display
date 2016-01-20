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
package org.dc.display;

import java.util.concurrent.Future;
import org.dc.display.jp.JPBroadcastDisplayScope;
import org.dc.display.jp.JPDisplayScope;
import org.dc.jps.core.JPService;
import org.dc.jps.exception.JPServiceException;
import org.dc.jul.exception.CouldNotPerformException;
import org.dc.jul.exception.InitializationException;
import org.dc.jul.extension.rsb.com.RPCHelper;
import org.dc.jul.extension.rsb.com.RSBRemoteService;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import rst.homeautomation.unit.UnitConfigType.UnitConfig;

/**
 * A remote to control a generic display server via rsb.
 *
 * @author * @author <a href="mailto:DivineThreepwood@gmail.com">Divine Threepwood</a>
 */
public class DisplayRemote extends RSBRemoteService<UnitConfig> implements Display {

    static {
        DefaultConverterRepository.getDefaultConverterRepository().addConverter(new ProtocolBufferConverter<>(UnitConfig.getDefaultInstance()));
    }

    private final DisplayRemote broadcastDisplayRemote;

    public DisplayRemote() {
        this.broadcastDisplayRemote = new DisplayRemote(true);
    }

    public DisplayRemote(final boolean broadcast) {
        this.broadcastDisplayRemote = null;
    }

    @Override
    public void activate() throws InterruptedException, CouldNotPerformException {
        super.activate();
        if (broadcastDisplayRemote != null) {
            broadcastDisplayRemote.activate();
        }
    }

    @Override
    public void deactivate() throws InterruptedException, CouldNotPerformException {
        super.deactivate();
        if (broadcastDisplayRemote != null) {
            broadcastDisplayRemote.deactivate();
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
        if (broadcastDisplayRemote != null) {
            broadcastDisplayRemote.shutdown();
        }
    }

    @Override
    public void notifyUpdated(UnitConfig data) throws CouldNotPerformException {

    }

    /**
     * Initializes the remote with the default scope.
     *
     * @throws InitializationException
     */
    public void init() throws InitializationException {
        try {
            super.init(JPService.getProperty(JPDisplayScope.class).getValue());
            if (broadcastDisplayRemote != null) {
                broadcastDisplayRemote.init(JPService.getProperty(JPBroadcastDisplayScope.class).getValue());
            }
        } catch (JPServiceException ex) {
            throw new InitializationException(this, ex);
        }
    }

    public Display broadcast() {
        return broadcastDisplayRemote;
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future showURL(String url) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(url, this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future showHTMLContent(String content) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(content, this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showInfoText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showWarnText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showErrorText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showImage(String image) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(image, this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future setURL(String url) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(url, this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future setHTMLContent(String content) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(content, this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setInfoText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setWarnText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setErrorText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setImage(String image) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(image, this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.dc.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setVisible(Boolean visible) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(visible, this);
    }

}
