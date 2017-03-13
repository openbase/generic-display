package org.openbase.display;

/*
 * #%L
 * GenericDisplay
 * %%
 * Copyright (C) 2015 - 2017 openbase.org
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
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.iface.annotations.RPCMethod;

/**
 *
 * @author <a href="mailto:divine@openbase.org">Divine Threepwood</a>
 */
public interface Display {

    /**
     * Shows the given URL on the generic display.
     * Display will set to foreground if the panel is hided.
     *
     * @param url the URL to display
     * @return the future which represents the execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.on
     */
    @RPCMethod
    public Future<Void> showURL(final String url) throws CouldNotPerformException;

    /**
     * Shows the given html content on the generic display.
     * Display will set to foreground if the panel is hided.
     *
     * @param content
     * @return the future which gives feedback about the asynchronous execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.
     */
    @RPCMethod
    public Future<Void> showHTMLContent(String content) throws CouldNotPerformException;

    /**
     * Shows the given info text on the generic display.
     * Display will set to foreground if the panel is hided.
     *
     * @param text the text to display
     * @return the future which gives feedback about the asynchronous execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.
     */
    @RPCMethod
    public Future<Void> showInfoText(final String text) throws CouldNotPerformException;

    /**
     * Shows the given warn text on the generic display.
     * Display will set to foreground if the panel is hided.
     *
     * @param text the text to display
     * @return the future which gives feedback about the asynchronous execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.
     */
    @RPCMethod
    public Future<Void> showWarnText(final String text) throws CouldNotPerformException;

    /**
     * Shows the given error text on the generic display.
     * Display will set to foreground if the panel is hided.
     *
     * @param text the text to display
     * @return the future which gives feedback about the asynchronous execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.
     */
    @RPCMethod
    public Future<Void> showErrorText(final String text) throws CouldNotPerformException;

    /**
     * Shows the given text on the generic display.
     * Display will set to foreground if the panel is hided.
     *
     * @param text the text to display
     * @return the future which gives feedback about the asynchronous execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.
     */
    @RPCMethod
    public Future<Void> showText(final String text) throws CouldNotPerformException;

    /**
     * Shows the given image centralized on the generic display.
     * Display will set to foreground if the panel is hided.
     *
     * @param image the image to display
     * @return the future which gives feedback about the asynchronous execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.
     */
    @RPCMethod
    public Future<Void> showImage(final String image) throws CouldNotPerformException;

    /**
     * Set the given URL on the generic display.
     *
     * @param url the URL to display
     * @return the future which gives feedback about the asynchronous execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.
     */
    @RPCMethod
    public Future<Void> setURL(final String url) throws CouldNotPerformException;

    /**
     * Set the given html content on the generic display.
     *
     * @param content the html string which will be displayed.
     * @return the future which gives feedback about the asynchronous execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.
     */
    @RPCMethod
    public Future<Void> setHTMLContent(String content) throws CouldNotPerformException;

    /**
     * Set the given info text on the generic display.
     *
     * @param text the text to display
     * @return the future which gives feedback about the asynchronous execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.
     */
    @RPCMethod
    public Future<Void> setInfoText(final String text) throws CouldNotPerformException;

    /**
     * Set the given warn text on the generic display.
     *
     * @param text the text to display
     * @return the future which gives feedback about the asynchronous execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.
     */
    @RPCMethod
    public Future<Void> setWarnText(final String text) throws CouldNotPerformException;

    /**
     * Set the given error text on the generic display.
     *
     * @param text the text to display
     * @return the future which gives feedback about the asynchronous execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.
     */
    @RPCMethod
    public Future<Void> setErrorText(final String text) throws CouldNotPerformException;

    /**
     * Set the given text on the generic display.
     *
     * @param text the text to display
     * @return the future which gives feedback about the asynchronous execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.
     */
    @RPCMethod
    public Future<Void> setText(final String text) throws CouldNotPerformException;

    /**
     * Set the given image centralized on the generic display.
     *
     * @param image the image to display
     * @return the future which gives feedback about the asynchronous execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.
     */
    @RPCMethod
    public Future<Void> setImage(final String image) throws CouldNotPerformException;

    /**
     * Displays the server in foreground fullscreen mode or hides the overall window.
     *
     * @param visible this flag defines the visibility. True means the display will be set visible and false hides the display.
     * @return the future which gives feedback about the asynchronous execution process.
     * @throws CouldNotPerformException is thrown if the execution could not be performed.
     */
    @RPCMethod
    public Future<Void> setVisible(final Boolean visible) throws CouldNotPerformException;
}
