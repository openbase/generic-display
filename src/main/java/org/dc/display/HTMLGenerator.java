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

import javafx.scene.paint.Color;

/**
 *
 * @author <a href="mailto:mpohling@cit-ec.uni-bielefeld.de">Divine Threepwood</a>
 */
public class HTMLGenerator {

    public static String generateTextBox(String text, Color color) {
        return "<html lang=\"de\">"
                + "    <head>"
                + "        <title>Datum und Zeit</title>"
                + "        <style>"
                + "        html, body {"
                + "            height: 100%;"
                + "            margin: 0;"
                + "            padding: 0;"
                + "            width: 100%;"
                + "            font-size: 300%;"
                + "            font-family: Helvetica, Arial, sans-serif;"
                + "            line-height: 150%;"
                + "            color: rgb(" + (int) (color.getRed() * 255) + "," + (int) (color.getGreen() * 255) + "," + (int) (color.getBlue() * 255) + ");"
                + "        }"
                + "        body {"
                + "            display: table;"
                + "        }"
                + "        .block {"
                + "            text-align: center;"
                + "            display: table-cell;"
                + "            vertical-align: middle;"
                + "        }"
                + "        </style>"
                + "    </head>"
                + "    <body>"
                + "    <div class=\"block\">"
                + "       <p>" + text + "</p>"
                + "    </div>"
                + "    </body>"
                + "</html>";
    }

    public static String generateImageBox(String image) {
        return "<html lang=\"de\">"
                + "    <head>"
                + "        <title>Datum und Zeit</title>"
                + "        <style>"
                + "        html, body {"
                + "            text-align: center;"
                + "            height: 100%;"
                + "            margin: 0;"
                + "            padding: 0;"
                + "            width: 100%;"
                + "            background-color: black;"
                + "        }"
                + "        body {"
                + "            display: table;"
                + "        }"
                + "        .image {"
                + "            width:100%;"
                + "            height:100%;"
                + "            background-size: cover;"
                + "        }"
                + "        </style>"
                + "    </head>"
                + "    <body>"
                + "    <div class=\"image\">"
                //                + "        <img src=\"" + image + "\" alt=\"Image[" + image + "]\" class=\"image\">"
                + "        <img src=\"" + image + "\" height=\"100%\" alt=\"Image[" + image + "]\">"
                + "    </div>"
                + "    </body>"
                + "</html>";
    }
}
