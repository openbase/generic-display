/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class FileStreamLoader {

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(FileStreamLoader.class);

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
            logger.debug("Could not load file absolut or relativ");
        }

        // Try to read file out of jar.
        try {
            in = ClassLoader.getSystemResourceAsStream(uri);
            return in;
        } catch (Exception ex) {
            logger.debug("Could not load file out of jar");
        }

//        // Try to read file as resource.
//        try {
//            in = new File(FileStreamLoader.class.getClass().getClassLoader().getResource(uri).getFile());
//            return in;
//        } catch (Exception ex) {
//            logger.debug("Could not load file out of jar");
//        }
        throw new IOException("Could not open " + uri);

    }
}
