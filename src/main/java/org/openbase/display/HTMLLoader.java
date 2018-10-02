package org.openbase.display;

/*
 * #%L
 * GenericDisplay
 * %%
 * Copyright (C) 2015 - 2018 openbase.org
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
import java.net.URL;

import javafx.scene.paint.Color;
import javafx.stage.Screen;
import org.apache.commons.io.IOUtils;
import org.openbase.jps.core.JPService;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.InstantiationException;
import org.openbase.jul.exception.VerificationFailedException;
import org.openbase.jul.extension.rst.processing.MetaConfigVariableProvider;
import org.openbase.jul.processing.VariableProcessor;
import org.openbase.jul.processing.VariableProvider;
import org.openbase.jul.processing.VariableStore;
import rst.configuration.EntryType.Entry;
import rst.configuration.MetaConfigType.MetaConfig;

/**
 *
 * @author <a href="mailto:divine@openbase.org">Divine Threepwood</a>
 */
public class HTMLLoader {

    public enum Template {

        IMAGE_TEXT_TEXT_VIEW("template/html/ImageTextTextView.html"),
        IMAGE_VIEW("template/html/ImageView.html"),
        TEXT_VIEW("template/html/TextView.html");

        public static final String KEY_TEMPLATE = "TEMPLATE";
        private final String uri;
        private String template;

        Template(final String uri) {
            this.uri = uri;
        }

        public String getTemplate() throws CouldNotPerformException {
            if (template == null) {
                try {
                    template = IOUtils.toString(ResourceStreamLoader.loadFileInputStream(uri), "UTF-8");
                } catch (IOException ex) {
                    throw new CouldNotPerformException("Could not load " + this + "!", ex);
                }
            }
            return template;
        }

        private void verify() throws VerificationFailedException {
            try {
                getTemplate();
            } catch (CouldNotPerformException ex) {
                throw new VerificationFailedException("Could not load " + this + "!", ex);
            }
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "." + name() + "[" + uri + "]";
        }

    }

    private final VariableStore globalVariableStore;

    public HTMLLoader() throws InstantiationException {
        try {
            this.globalVariableStore = new VariableStore(HTMLLoader.class.getSimpleName());

            //verify templates
            for (Template template : Template.values()) {
                template.verify();
            }
        } catch (CouldNotPerformException ex) {
            throw new InstantiationException(this, ex);
        }
    }

    public void init(final Screen screen) {
        globalVariableStore.store("SCREEN_WIDTH", Double.toString(screen.getBounds().getWidth()));
        globalVariableStore.store("SCREEN_HEIGHT", Double.toString(screen.getBounds().getHeight()));
        globalVariableStore.store("APP", JPService.getApplicationName());
    }

    public String loadTextView(final String text, final Color color) throws CouldNotPerformException {
        try {
            globalVariableStore.store("TEXT", text);
            globalVariableStore.store("COLOR", "rgb(" + (int) (color.getRed() * 255) + "," + (int) (color.getGreen() * 255) + "," + (int) (color.getBlue() * 255) + ")");
            return buildContext(Template.TEXT_VIEW, globalVariableStore, true);
        } catch (CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not load TextView!", ex);
        }
    }

    public String loadImageView(final String image) throws CouldNotPerformException {
        try {
            validateURI(image);
            globalVariableStore.store("IMAGE", image);
            return buildContext(Template.IMAGE_VIEW, globalVariableStore, true);
        } catch (CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not load ImageView!", ex);
        }
    }

    public String loadTemplateView(final Template template, final MetaConfig metaConfig, boolean failOnMissingVariables) throws CouldNotPerformException {
        try {
            for (Entry entry : metaConfig.getEntryList()) {
                if (entry.getKey().contains("URL")) {
                    validateURI(entry.getValue());
                }
            }
            return buildContext(template, new MetaConfigVariableProvider("passed parameters", metaConfig), failOnMissingVariables);
        } catch (CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not load ImageView!", ex);
        }
    }

    static String buildContext(final Template template, final VariableProvider variableProvider, boolean failOnMissingVariables) throws CouldNotPerformException {
        try {
            return VariableProcessor.resolveVariables(template.getTemplate(), failOnMissingVariables, variableProvider);
        } catch (CouldNotPerformException ex) {
            throw new CouldNotPerformException("Could not build context out of Template[" + template + "]!", ex);
        }
    }

    private static void validateURI(final String uri) throws VerificationFailedException {
        try {
            new URL(uri).openStream().close();
        } catch (Exception ex) {
            throw new VerificationFailedException("URI["+uri+"] is not valid!", ex);
        }
    }
}
