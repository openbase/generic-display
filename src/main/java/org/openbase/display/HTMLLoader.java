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

import java.io.IOException;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import org.apache.commons.io.IOUtils;
import org.openbase.jps.core.JPService;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.InstantiationException;
import org.openbase.jul.exception.VerificationFailedException;
import org.openbase.jul.processing.VariableProcessor;
import org.openbase.jul.processing.VariableStore;

/**
 *
 * @author <a href="mailto:divine@openbase.org">Divine Threepwood</a>
 */
public class HTMLLoader {

    public enum Template {

        IMAGE_VIEW("template/html/ImageView.html"),
        TEXT_VIEW("template/html/TextView.html");
//        IMAGE_VIEW("/vol/csra/releases/trusty/lsp-csra-nightly/share/generic-display/template/html/ImageView.html"),
//        TEXT_VIEW("/vol/csra/releases/trusty/lsp-csra-nightly/share/generic-display/template/html/TextView.html");

        private final String uri;
        private String template;

        private Template(final String uri) {
            this.uri = uri;
        }

        public String getTemplate() throws CouldNotPerformException {
            if (template == null) {
                try {
                    template = IOUtils.toString(ResourceStreamLoader.loadFileInputStream(uri), "UTF-8");
                } catch (IOException ex) {
                    throw new CouldNotPerformException("Could not load " + this + "!");
                }
            }
            return template;
        }

        private void verify() throws VerificationFailedException {
            try {
                getTemplate();
            } catch (CouldNotPerformException ex) {
                throw new VerificationFailedException("Could not load " + this + "!");
            }
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "." + name() + "[" + uri + "]";
        }

    }

    private final VariableStore variableStore;

    public HTMLLoader() throws InstantiationException {
        try {
            this.variableStore = new VariableStore(HTMLLoader.class.getSimpleName());

            //verify templates
            for (Template template : Template.values()) {
                template.verify();
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
            return buildContext(Template.TEXT_VIEW);
        } catch (CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not load TextView!", ex);
        }
    }

    public String loadImageView(String image) throws CouldNotPerformException {
        try {
            variableStore.store("IMAGE", image);
            return buildContext(Template.IMAGE_VIEW);
        } catch (CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not load ImageView!", ex);
        }

    }

    public String buildContext(final Template template) throws CouldNotPerformException {
        try {
            return VariableProcessor.resolveVariables(template.getTemplate(), true, variableStore);
        } catch (CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not build context out of Template[" + template + "]!", ex);
        }
    }
}
