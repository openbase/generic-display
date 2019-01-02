package org.openbase.display;

/*
 * #%L
 * GenericDisplay
 * %%
 * Copyright (C) 2015 - 2019 openbase.org
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
import org.openbase.jul.schedule.FutureProcessor;
import rsb.config.ParticipantConfig;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import org.openbase.type.configuration.MetaConfigType.MetaConfig;
import org.openbase.type.domotic.unit.dal.DisplayDataType.DisplayData;
import org.openbase.type.com.ScopeType;

/**
 * A remote to control a generic display server via rsb.
 *
 * @author <a href="mailto:divine@openbase.org">Divine Threepwood</a>
 */
public class DisplayRemote extends RSBRemoteService<DisplayData> implements Display {

    static {
        DefaultConverterRepository.getDefaultConverterRepository().addConverter(new ProtocolBufferConverter<>(MetaConfig.getDefaultInstance()));
        DefaultConverterRepository.getDefaultConverterRepository().addConverter(new ProtocolBufferConverter<>(DisplayData.getDefaultInstance()));
    }

    private final DisplayRemote broadcastDisplayRemote;

    public DisplayRemote() {
        super(DisplayData.class);
        this.broadcastDisplayRemote = new DisplayRemote(null);
    }

    public DisplayRemote(final DisplayRemote broadcastDisplayRemote) {
        super(DisplayData.class);
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
     * @param url {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showUrlAndReload(final String url) {
        try {
            return RPCHelper.callRemoteMethod(url, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param content {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showHtmlContentAndReload(final String content) {
        try {
            return RPCHelper.callRemoteMethod(content, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param url the URL to display {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showUrl(String url) {
        try {
            return RPCHelper.callRemoteMethod(url, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param content {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showHtmlContent(String content) {
        try {
            return RPCHelper.callRemoteMethod(content, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param text {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showInfoText(String text) {
        try {
            return RPCHelper.callRemoteMethod(text, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param text {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showWarnText(String text) {
        try {
            return RPCHelper.callRemoteMethod(text, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param text {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showErrorText(String text) {
        try {
            return RPCHelper.callRemoteMethod(text, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param text {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showText(String text) {
        try {
            return RPCHelper.callRemoteMethod(text, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param image {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showImage(String image) {
        try {
            return RPCHelper.callRemoteMethod(image, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param url {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setUrl(String url) {
        try {
            return RPCHelper.callRemoteMethod(url, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param content {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setHtmlContent(String content) {
        try {
            return RPCHelper.callRemoteMethod(content, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param text {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setInfoText(String text) {
        try {
            return RPCHelper.callRemoteMethod(text, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param text {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setWarnText(String text) {
        try {
            return RPCHelper.callRemoteMethod(text, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param text {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setErrorText(String text) {
        try {
            return RPCHelper.callRemoteMethod(text, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param text {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setText(String text) {
        try {
            return RPCHelper.callRemoteMethod(text, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param image {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setImage(String image) {
        try {
            return RPCHelper.callRemoteMethod(image, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param visible {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setVisible(Boolean visible) {
        try {
            return RPCHelper.callRemoteMethod(visible, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param metaConfig {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> setTemplate(MetaConfig metaConfig) {
        try {
            return RPCHelper.callRemoteMethod(metaConfig, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param metaConfig {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> showTemplate(MetaConfig metaConfig) {
        try {
            return RPCHelper.callRemoteMethod(metaConfig, this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Future<Void> closeAll() {
        try {
            return RPCHelper.callRemoteMethod(this, Void.class);
        } catch (CouldNotPerformException ex) {
            return FutureProcessor.canceledFuture(Void.class, ex);
        }
    }
}
