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
package de.dc.display;

import de.citec.jul.extension.rsb.scope.jp.JPScope;
import rsb.Scope;

/**
 *
 * @author <a href="mailto:mpohling@cit-ec.uni-bielefeld.de">Divine Threepwood</a>
 */
public class JPGenericDisplayScope extends JPScope {
    public final static String[] COMMAND_IDENTIFIERS = {"--display-scope"};

    
	public JPGenericDisplayScope() {
		super(COMMAND_IDENTIFIERS);
	}
    
    @Override
    protected Scope getPropertyDefaultValue() {
        return new Scope("/home/display");
    }

    @Override
	public String getDescription() {
		return "Defines the rsb scope of the generic display server instance.";
    }
}
