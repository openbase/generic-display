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

import org.openbase.jul.annotation.RPCMethod;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.type.configuration.MetaConfigType.MetaConfig;

/**
 * @author <a href="mailto:divine@openbase.org">Divine Threepwood</a>
 */
public interface Display {

    /**
     * Shows the given URL on the generic display.
     * Display will set to foreground if the panel is hided.
     * The content is reloaded even if currently displayed.
     *
     * @param url the URL to display
     *
     * @return the future which represents the execution process.
     */
    @RPCMethod
    Future<Void> showUrlAndReload(final String url) throws CouldNotPerformException;

    /**
     * Shows the given html content on the generic display.
     * Display will set to foreground if the panel is hided.
     * The content is reloaded even if currently displayed.
     *
     * @param content
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> showHtmlContentAndReload(String content) throws CouldNotPerformException;

    /**
     * Shows the given URL on the generic display.
     * Display will set to foreground if the panel is hided.
     *
     * @param url the URL to display
     *
     * @return the future which represents the execution process.
     */
    @RPCMethod
    Future<Void> showUrl(final String url) throws CouldNotPerformException;

    /**
     * Shows the given html content on the generic display.
     * Display will set to foreground if the panel is hided.
     *
     * @param content
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> showHtmlContent(String content) throws CouldNotPerformException;

    /**
     * Shows the given info text on the generic display.
     * Display will set to foreground if the panel is hided.
     *
     * @param text the text to display
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> showInfoText(final String text);

    /**
     * Shows the given warn text on the generic display.
     * Display will set to foreground if the panel is hided.
     *
     * @param text the text to display
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> showWarnText(final String text);

    /**
     * Shows the given error text on the generic display.
     * Display will set to foreground if the panel is hided.
     *
     * @param text the text to display
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> showErrorText(final String text);

    /**
     * Shows the given text on the generic display.
     * Display will set to foreground if the panel is hided.
     *
     * @param text the text to display
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> showText(final String text);

    /**
     * Shows the given image centralized on the generic display.
     * Display will set to foreground if the panel is hided.
     *
     * @param image the image to display
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> showImage(final String image);

    /**
     * Set the given URL on the generic display.
     *
     * @param url the URL to display
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> setUrl(final String url);

    /**
     * Set the given html content on the generic display.
     *
     * @param content the html string which will be displayed.
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> setHtmlContent(String content);

    /**
     * Set the given info text on the generic display.
     *
     * @param text the text to display
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> setInfoText(final String text);

    /**
     * Set the given warn text on the generic display.
     *
     * @param text the text to display
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> setWarnText(final String text);

    /**
     * Set the given error text on the generic display.
     *
     * @param text the text to display
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> setErrorText(final String text);

    /**
     * Set the given text on the generic display.
     *
     * @param text the text to display
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> setText(final String text);

    /**
     * Set the given image centralized on the generic display.
     *
     * @param image the image to display
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> setImage(final String image);

    /**
     * Displays the server in foreground fullscreen mode or hides the overall window.
     *
     * @param visible this flag defines the visibility. True means the display will be set visible and false hides the display.
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> setVisible(final Boolean visible);


    /**
     * Set the given template as content on the generic display.
     * <p>
     * meta config example:
     * <ul>
     * <li>TEMPLATE="ImageTextTextView"
     * <li>IMAGE_URL="https://images.com/mycat.png"
     * <li>TEXT_TOP="I am a nice cat!"
     * <li>TEXT_BOTTOM="Wonderful"
     * </ul>
     *
     * @param metaConfig a key value set used to define the template type and setup all template parameters.
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> setTemplate(final MetaConfig metaConfig);

    /**
     * Shows the given template as content on the generic display.
     * Display will set to foreground if the panel is hided.
     * <p>
     * meta config example:
     * <ul>
     * <li>TEMPLATE="ImageTextTextView"
     * <li>IMAGE_URL="https://images.com/mycat.png"
     * <li>TEXT_TOP="I am a nice cat!"
     * <li>TEXT_BOTTOM="Wonderful"
     * </ul>
     *
     * @param metaConfig a key value set used to define the template type and setup all template parameters.
     *
     * @param metaConfig
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> showTemplate(final MetaConfig metaConfig);

    /**
     * Closes all visible or background tabs .
     *
     * @return the future which gives feedback about the asynchronous execution process.
     */
    @RPCMethod
    Future<Void> closeAll();
}
