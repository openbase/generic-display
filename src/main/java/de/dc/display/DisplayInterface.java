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
import java.util.concurrent.Future;

/**
 *
 * @author * @author <a href="mailto:DivineThreepwood@gmail.com">Divine Threepwood</a>
 */
public interface DisplayInterface {

    /**
     * Shows the given URL on the generic display.
     *
     * @param url the URL to display
     * @return
     * @throws CouldNotPerformException
     */
    public Future<Void> showURL(final String url) throws CouldNotPerformException;

    /**
     * Shows the given html content on the generic display.
     *
     * @param content
     * @return
     * @throws CouldNotPerformException
     */
    public Future<Void> showHTMLContent(String content) throws CouldNotPerformException;

    /**
     * NOT SUPPORTED YET!
     *
     * @param presetId
     * @return
     * @throws CouldNotPerformException
     */
    public Future<Void> showPreset(final String presetId) throws CouldNotPerformException;

    /**
     * Shows the given info text on the generic display.
     *
     * @param text the text to display
     * @return
     * @throws CouldNotPerformException
     */
    public Future<Void> showInfoText(final String text) throws CouldNotPerformException;

    /**
     * Shows the given warn text on the generic display.
     *
     * @param text the text to display
     * @return
     * @throws CouldNotPerformException
     */
    public Future<Void> showWarnText(final String text) throws CouldNotPerformException;

    /**
     * Shows the given error text on the generic display.
     *
     * @param text the text to display
     * @return
     * @throws CouldNotPerformException
     */
    public Future<Void> showErrorText(final String text) throws CouldNotPerformException;

    /**
     * Shows the given text on the generic display.
     *
     * @param text the text to display
     * @return
     * @throws CouldNotPerformException
     */
    public Future<Void> showText(final String text) throws CouldNotPerformException;

}
