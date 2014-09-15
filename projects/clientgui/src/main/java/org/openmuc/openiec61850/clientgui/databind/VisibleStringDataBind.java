/*
 * Copyright 2013-14 Fraunhofer ISE
 *
 * This file is part of OpenIEC61850 Client GUI.
 * For more information visit http://www.openmuc.org
 *
 * OpenIEC61850 Client GUI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenIEC61850 Client GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenIEC61850 Client GUI.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.openiec61850.clientgui.databind;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.openmuc.openiec61850.BdaType;
import org.openmuc.openiec61850.BdaVisibleString;

public class VisibleStringDataBind extends TextFieldDataBind<BdaVisibleString> {

	private static final Charset ASCII = Charset.forName("US-ASCII");

	public VisibleStringDataBind(BdaVisibleString data) {
		super(data, BdaType.VISIBLE_STRING, new AsciiFilter(data.getMaxLength()));
	}

	@Override
	protected void resetImpl() {
		inputField.setText(new String(data.getValue(), ASCII));
	}

	@Override
	protected void writeImpl() {
		data.setValue(ASCII.encode(inputField.getText()).array());
	}

	private static class AsciiFilter extends TextFieldDataBind.AbstractFilter {
		private final CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder();
		private final int maxBytes;

		public AsciiFilter(int maxBytes) {
			this.maxBytes = maxBytes;
		}

		@Override
		protected boolean test(String text) {
			try {
				byte[] codedString = encoder.encode(CharBuffer.wrap(text)).array();
				return codedString.length <= maxBytes;
			} catch (CharacterCodingException e) {
				return false;
			}
		}
	}

}
