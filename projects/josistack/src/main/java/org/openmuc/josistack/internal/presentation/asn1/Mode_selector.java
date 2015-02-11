/**
 * This class file was automatically generated by jASN1 (http://www.openmuc.org)
 */

package org.openmuc.josistack.internal.presentation.asn1;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerIdentifier;
import org.openmuc.jasn1.ber.BerLength;
import org.openmuc.jasn1.ber.types.BerInteger;

import java.io.IOException;
import java.io.InputStream;

public final class Mode_selector {

    public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.UNIVERSAL_CLASS, BerIdentifier.CONSTRUCTED, 17);
    protected BerIdentifier id;

    public byte[] code = null;
    public BerInteger mode_value = null;

    public Mode_selector() {
        id = identifier;
    }

    public Mode_selector(byte[] code) {
        id = identifier;
        this.code = code;
    }

    public Mode_selector(BerInteger mode_value) {
        id = identifier;
        this.mode_value = mode_value;
    }

    public int encode(BerByteArrayOutputStream berOStream, boolean explicit) throws IOException {

        int codeLength;

        if (code != null) {
            codeLength = code.length;
            for (int i = code.length - 1; i >= 0; i--) {
                berOStream.write(code[i]);
            }
        } else {
            codeLength = 0;
            codeLength += mode_value.encode(berOStream, false);
            codeLength += (new BerIdentifier(BerIdentifier.CONTEXT_CLASS, BerIdentifier.PRIMITIVE, 0)).encode(berOStream);

            codeLength += BerLength.encodeLength(berOStream, codeLength);
        }

        if (explicit) {
            codeLength += id.encode(berOStream);
        }

        return codeLength;

    }

    public int decode(InputStream iStream, boolean explicit) throws IOException {
        int codeLength = 0;
        int subCodeLength = 0;
        BerIdentifier berIdentifier = new BerIdentifier();

        if (explicit) {
            codeLength += id.decodeAndCheck(iStream);
        }

        BerLength length = new BerLength();
        codeLength += length.decode(iStream);

        while (subCodeLength < length.val) {
            subCodeLength += berIdentifier.decode(iStream);
            if (berIdentifier.equals(BerIdentifier.CONTEXT_CLASS, BerIdentifier.PRIMITIVE, 0)) {
                mode_value = new BerInteger();
                subCodeLength += mode_value.decode(iStream, false);
            }
        }
        if (subCodeLength != length.val) {
            throw new IOException("Decoded sequence has wrong length tag");

        }
        codeLength += subCodeLength;

        return codeLength;
    }

    public void encodeAndSave(int encodingSizeGuess) throws IOException {
        BerByteArrayOutputStream berOStream = new BerByteArrayOutputStream(encodingSizeGuess);
        encode(berOStream, false);
        code = berOStream.getArray();
    }
}
