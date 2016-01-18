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
import java.io.IOException;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import org.apache.commons.io.FileUtils;
import org.dc.jps.core.JPService;
import org.dc.jul.exception.CouldNotPerformException;
import org.dc.jul.exception.InstantiationException;
import org.dc.jul.exception.NotAvailableException;
import org.dc.jul.processing.VariableProcessor;
import org.dc.jul.processing.VariableStore;

/**
 *
 * @author <a href="mailto:mpohling@cit-ec.uni-bielefeld.de">Divine Threepwood</a>
 */
public class HTMLLoader {

    public enum Template {

        IMAGE_VIEW("/vol/csra/releases/trusty/lsp-csra-nightly/share/generic-display/template/html/ImageView.html"),
        TEXT_VIEW("/vol/csra/releases/trusty/lsp-csra-nightly/share/generic-display/template/html/TextView.html");

        private final File file;

        private Template(final String uri) {
            this.file = new File(getClass().getClassLoader().getResource(uri).getFile());
        }

        public File getFile() {
            return file;
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "[" + file.getAbsolutePath() + "]";
        }

    }

    private final VariableStore variableStore;

    public HTMLLoader() throws InstantiationException {
        try {
            this.variableStore = new VariableStore(HTMLLoader.class.getSimpleName());

            //verify templates
            for(Template template : Template.values()) {
                if(!template.getFile().exists()) {
                    throw new NotAvailableException(Template.class, template.getFile().getAbsolutePath());
                }
            }
        } catch (CouldNotPerformException ex) {
            throw new InstantiationException(this, ex);
        }
    }

    public void init(final Screen screen) {
        variableStore.store("SCREEN_WIDTH", Double.toString(screen.getBounds().getWidth()));
        variableStore.store("SCREEN_HEIGHT", Double.toString(screen.getBounds().getHeight()));
        variableStore.store("APP", JPService.getApplicationName());
    }

    public String loadTextView(final String text, final Color color) throws CouldNotPerformException {
        try {
            variableStore.store("TEXT", text);
            variableStore.store("COLOR", "rgb(" + (int) (color.getRed() * 255) + "," + (int) (color.getGreen() * 255) + "," + (int) (color.getBlue() * 255) + ")");
            return loadHTML(Template.TEXT_VIEW);
        } catch (CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not load TextView!", ex);
        }
    }

    public String loadImageView(String image) throws CouldNotPerformException {
        try {
            variableStore.store("IMAGE", image);
            return loadHTML(Template.IMAGE_VIEW);
        } catch (CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not load ImageView!", ex);
        }

    }

    public String loadHTML(final Template template) throws CouldNotPerformException {
        try {
            String context = FileUtils.readFileToString(template.getFile());
            return VariableProcessor.resolveVariables(context, true, variableStore);
        } catch (IOException | CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not load Template[" + template + "]", ex);
        }
    }
}
