/**
 * This class file was automatically generated by jASN1 (http://www.openmuc.org)
 */

package org.openmuc.josistack.internal.acse.asn1;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerIdentifier;
import org.openmuc.jasn1.ber.types.BerObjectIdentifier;

import java.io.IOException;
import java.io.InputStream;

public final class AP_title {

    public byte[] code = null;
    public BerObjectIdentifier ap_title_form2 = null;

    public AP_title() {
    }

    public AP_title(byte[] code) {
        this.code = code;
    }

    public AP_title(BerObjectIdentifier ap_title_form2) {
        this.ap_title_form2 = ap_title_form2;
    }

    public int encode(BerByteArrayOutputStream berOStream, boolean explicit) throws IOException {
        if (code != null) {
            for (int i = code.length - 1; i >= 0; i--) {
                berOStream.write(code[i]);
            }
            return code.length;

        }
        int codeLength = 0;
        if (ap_title_form2 != null) {
            codeLength += ap_title_form2.encode(berOStream, true);
            return codeLength;

        }

        throw new IOException("Error encoding BerChoice: No item in choice was selected.");
    }

    public int decode(InputStream iStream, BerIdentifier berIdentifier) throws IOException {
        int codeLength = 0;
        BerIdentifier passedIdentifier = berIdentifier;
        if (berIdentifier == null) {
            berIdentifier = new BerIdentifier();
            codeLength += berIdentifier.decode(iStream);
        }
        if (berIdentifier.equals(BerObjectIdentifier.identifier)) {
            ap_title_form2 = new BerObjectIdentifier();
            codeLength += ap_title_form2.decode(iStream, false);
            return codeLength;
        }

        if (passedIdentifier != null) {
            return 0;
        }
        throw new IOException("Error decoding BerChoice: Identifier matched to no item.");
    }

    public void encodeAndSave(int encodingSizeGuess) throws IOException {
        BerByteArrayOutputStream berOStream = new BerByteArrayOutputStream(encodingSizeGuess);
        encode(berOStream, false);
        code = berOStream.getArray();
    }
}
