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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.LoggerFactory;

/**
 *
 * @author <a href="mailto:mpohling@cit-ec.uni-bielefeld.de">Divine Threepwood</a>
 */
public class ResourceStreamLoader {

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(ResourceStreamLoader.class);

    public static InputStream loadFileInputStream(final String uri) throws IOException {
        InputStream in = null;
        // Try to read file absolut or relativ of project location.
        try {
            File file = new File(uri);
            if (!file.exists()) {
                throw new IOException("File does not exist!");
            } else if (!file.isFile()) {
                throw new IOException("URI is not a file!");
            } else {
                in = new FileInputStream(file);
            }
            return in;
        } catch (Exception ex) {
            logger.debug("Could not load file absolut or relativ", ex);
        }

        // Try to read file out of jar.
        try {
            in = ClassLoader.getSystemResourceAsStream(uri);
            return in;
        } catch (Exception ex) {
            logger.debug("Could not load file out of jar", ex);
        }

        // Try to read file as resource.
        try {
            in = ResourceStreamLoader.class.getClass().getClassLoader().getResource(uri).openStream();
            return in;
        } catch (Exception ex) {
            logger.debug("Could not load resource via classloader!", ex);
        }
        throw new IOException("Could not open " + uri);

    }
}
