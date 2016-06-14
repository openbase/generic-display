package org.openbase.display;

/*
 * #%L
 * GenericDisplay
 * %%
 * Copyright (C) 2015 - 2016 openbase.org
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
import org.openbase.display.jp.JPBroadcastDisplayScope;
import org.openbase.display.jp.JPDisplayScope;
import org.openbase.jps.core.JPService;
import org.openbase.jps.exception.JPServiceException;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.InitializationException;
import org.openbase.jul.extension.rsb.com.RPCHelper;
import org.openbase.jul.extension.rsb.com.RSBRemoteService;
import rsb.config.ParticipantConfig;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import rst.homeautomation.unit.UnitConfigType.UnitConfig;
import rst.rsb.ScopeType;

/**
 * A remote to control a generic display server via rsb.
 *
 * @author * @author <a href="mailto:DivineThreepwood@gmail.com">Divine
 * Threepwood</a>
 */
public class DisplayRemote extends RSBRemoteService<UnitConfig> implements Display {

    static {
        DefaultConverterRepository.getDefaultConverterRepository().addConverter(new ProtocolBufferConverter<>(UnitConfig.getDefaultInstance()));
    }

    private final DisplayRemote broadcastDisplayRemote;

    public DisplayRemote() {
        super(UnitConfig.class);
        this.broadcastDisplayRemote = new DisplayRemote(null);
    }

    public DisplayRemote(final DisplayRemote broadcastDisplayRemote) {
        super(UnitConfig.class);
        this.broadcastDisplayRemote = broadcastDisplayRemote;
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
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showURL(String url) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(url, this, Void.class);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showHTMLContent(String content) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(content, this, Void.class);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showInfoText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this, Void.class);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showWarnText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this, Void.class);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showErrorText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this, Void.class);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this, Void.class);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> showImage(String image) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(image, this, Void.class);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setURL(String url) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(url, this, Void.class);
    }
    
    /**
     * {@inheritDoc}
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setHTMLContent(String content) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(content, this, Void.class);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setInfoText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this, Void.class);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setWarnText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this, Void.class);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setErrorText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this, Void.class);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setText(String text) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(text, this, Void.class);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setImage(String image) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(image, this, Void.class);
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.openbase.jul.exception.CouldNotPerformException
     */
    @Override
    public Future<Void> setVisible(Boolean visible) throws CouldNotPerformException {
        return RPCHelper.callRemoteMethod(visible, this, Void.class);
    }

}
