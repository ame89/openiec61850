/**
 * This class file was automatically generated by jASN1 (http://www.openmuc.org)
 */

package org.openmuc.josistack.internal.presentation.asn1;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerIdentifier;
import org.openmuc.jasn1.ber.BerLength;
import org.openmuc.jasn1.ber.types.BerObjectIdentifier;

import java.io.IOException;
import java.io.InputStream;

public final class Default_context_name {

    public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.UNIVERSAL_CLASS, BerIdentifier.CONSTRUCTED, 16);
    protected BerIdentifier id;

    public byte[] code = null;
    public BerObjectIdentifier abstract_syntax_name = null;

    public BerObjectIdentifier transfer_syntax_name = null;

    public Default_context_name() {
        id = identifier;
    }

    public Default_context_name(byte[] code) {
        id = identifier;
        this.code = code;
    }

    public Default_context_name(BerObjectIdentifier abstract_syntax_name, BerObjectIdentifier transfer_syntax_name) {
        id = identifier;
        this.abstract_syntax_name = abstract_syntax_name;
        this.transfer_syntax_name = transfer_syntax_name;
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
            codeLength += transfer_syntax_name.encode(berOStream, false);
            codeLength += (new BerIdentifier(BerIdentifier.CONTEXT_CLASS, BerIdentifier.PRIMITIVE, 1)).encode(berOStream);

            codeLength += abstract_syntax_name.encode(berOStream, false);
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
        boolean decodedIdentifier = false;

        if (explicit) {
            codeLength += id.decodeAndCheck(iStream);
        }

        BerLength length = new BerLength();
        codeLength += length.decode(iStream);

        if (subCodeLength < length.val) {
            if (decodedIdentifier == false) {
                subCodeLength += berIdentifier.decode(iStream);
                decodedIdentifier = true;
            }
            if (berIdentifier.equals(BerIdentifier.CONTEXT_CLASS, BerIdentifier.PRIMITIVE, 0)) {
                abstract_syntax_name = new BerObjectIdentifier();
                subCodeLength += abstract_syntax_name.decode(iStream, false);
                decodedIdentifier = false;
            } else {
                throw new IOException("Identifier does not macht required sequence element identifer.");
            }
        }
        if (subCodeLength < length.val) {
            if (decodedIdentifier == false) {
                subCodeLength += berIdentifier.decode(iStream);
                decodedIdentifier = true;
            }
            if (berIdentifier.equals(BerIdentifier.CONTEXT_CLASS, BerIdentifier.PRIMITIVE, 1)) {
                transfer_syntax_name = new BerObjectIdentifier();
                subCodeLength += transfer_syntax_name.decode(iStream, false);
                decodedIdentifier = false;
            } else {
                throw new IOException("Identifier does not macht required sequence element identifer.");
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
