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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dc.display;

import javafx.scene.paint.Color;

/**
 *
 * @author <a href="mailto:mpohling@cit-ec.uni-bielefeld.de">Divine Threepwood</a>
 */
public class TextBoxHTMLGenerator {

    public static String generate(String text, Color color) {
        System.out.println("rgb(" + (int)(color.getRed() * 255) + "," + (int)(color.getGreen() * 255) + "," + (int)(color.getBlue() * 255) + ");");
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
                + "            color: rgb(" + (int)(color.getRed() * 255) + "," + (int)(color.getGreen() * 255) + "," + (int)(color.getBlue() * 255) + ");"
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
}
