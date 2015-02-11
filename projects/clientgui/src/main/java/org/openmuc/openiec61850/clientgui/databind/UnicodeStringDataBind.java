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

import org.openmuc.openiec61850.BdaType;
import org.openmuc.openiec61850.BdaUnicodeString;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class UnicodeStringDataBind extends TextFieldDataBind<BdaUnicodeString> {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    public UnicodeStringDataBind(BdaUnicodeString data) {
        super(data, BdaType.UNICODE_STRING, new Utf8Filter(data.getMaxLength()));
    }

    @Override
    protected void resetImpl() {
        inputField.setText(new String(data.getValue(), UTF8));
    }

    @Override
    protected void writeImpl() {
        data.setValue(UTF8.encode(inputField.getText()).array());
    }

    private static class Utf8Filter extends AbstractFilter {
        private final CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
        private final int maxBytes;

        public Utf8Filter(int maxBytes) {
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
