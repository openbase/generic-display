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
import java.util.concurrent.Future;
import org.dc.display.jp.JPBroadcastDisplayScope;
import org.dc.display.jp.JPDisplayScope;
import org.dc.jps.core.JPService;
import org.dc.jps.exception.JPServiceException;
import org.dc.jul.exception.CouldNotPerformException;
import org.dc.jul.exception.InitializationException;
import org.dc.jul.extension.rsb.com.RPCHelper;
import org.dc.jul.extension.rsb.com.RSBRemoteService;
import org.dc.jul.pattern.Remote;
import rsb.config.ParticipantConfig;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import rst.homeautomation.unit.UnitConfigType.UnitConfig;
import rst.rsb.ScopeType;

/**
 * A remote to control a generic display server via rsb.
 *
 * @author * @author <a href="mailto:DivineThreepwood@gmail.com">Divine Threepwood</a>
 */
public class DisplayRemote extends RSBRemoteService<UnitConfig> implements Display, Remote<ScopeType.Scope> {

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
     * @throws java.lang.InterruptedException
     */
    public void init() throws InitializationException, InterruptedException {
        try {
            this.init(JPService.getProperty(JPDisplayScope.class).getValue());
        } catch (JPServiceException ex) {
            throw new InitializationException(this, ex);
        }
    }

    @Override
    public synchronized void init(final ScopeType.Scope scope, final ParticipantConfig participantConfig) throws InitializationException, InterruptedException {
        try {
            super.init(scope, participantConfig);
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
