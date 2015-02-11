/**
 * This class file was automatically generated by jASN1 (http://www.openmuc.org)
 */

package org.openmuc.openiec61850.internal.mms.asn1;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerIdentifier;
import org.openmuc.jasn1.ber.types.BerNull;

import java.io.IOException;
import java.io.InputStream;

public final class ConfirmedServiceResponse {

    public byte[] code = null;
    public GetNameListResponse getNameList = null;

    public ReadResponse read = null;

    public WriteResponse write = null;

    public GetVariableAccessAttributesResponse getVariableAccessAttributes = null;

    public BerNull defineNamedVariableList = null;

    public GetNamedVariableListAttributesResponse getNamedVariableListAttributes = null;

    public DeleteNamedVariableListResponse deleteNamedVariableList = null;

    public ConfirmedServiceResponse() {
    }

    public ConfirmedServiceResponse(byte[] code) {
        this.code = code;
    }

    public ConfirmedServiceResponse(GetNameListResponse getNameList, ReadResponse read, WriteResponse write,
                                    GetVariableAccessAttributesResponse getVariableAccessAttributes, BerNull defineNamedVariableList,
                                    GetNamedVariableListAttributesResponse getNamedVariableListAttributes,
                                    DeleteNamedVariableListResponse deleteNamedVariableList) {
        this.getNameList = getNameList;
        this.read = read;
        this.write = write;
        this.getVariableAccessAttributes = getVariableAccessAttributes;
        this.defineNamedVariableList = defineNamedVariableList;
        this.getNamedVariableListAttributes = getNamedVariableListAttributes;
        this.deleteNamedVariableList = deleteNamedVariableList;
    }

    public int encode(BerByteArrayOutputStream berOStream, boolean explicit) throws IOException {
        if (code != null) {
            for (int i = code.length - 1; i >= 0; i--) {
                berOStream.write(code[i]);
            }
            return code.length;

        }
        int codeLength = 0;
        if (deleteNamedVariableList != null) {
            codeLength += deleteNamedVariableList.encode(berOStream, false);
            codeLength += (new BerIdentifier(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 13)).encode(berOStream);
            return codeLength;

        }

        if (getNamedVariableListAttributes != null) {
            codeLength += getNamedVariableListAttributes.encode(berOStream, false);
            codeLength += (new BerIdentifier(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 12)).encode(berOStream);
            return codeLength;

        }

        if (defineNamedVariableList != null) {
            codeLength += defineNamedVariableList.encode(berOStream, false);
            codeLength += (new BerIdentifier(BerIdentifier.CONTEXT_CLASS, BerIdentifier.PRIMITIVE, 11)).encode(berOStream);
            return codeLength;

        }

        if (getVariableAccessAttributes != null) {
            codeLength += getVariableAccessAttributes.encode(berOStream, false);
            codeLength += (new BerIdentifier(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 6)).encode(berOStream);
            return codeLength;

        }

        if (write != null) {
            codeLength += write.encode(berOStream, false);
            codeLength += (new BerIdentifier(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 5)).encode(berOStream);
            return codeLength;

        }

        if (read != null) {
            codeLength += read.encode(berOStream, false);
            codeLength += (new BerIdentifier(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 4)).encode(berOStream);
            return codeLength;

        }

        if (getNameList != null) {
            codeLength += getNameList.encode(berOStream, false);
            codeLength += (new BerIdentifier(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 1)).encode(berOStream);
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
        if (berIdentifier.equals(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 1)) {
            getNameList = new GetNameListResponse();
            codeLength += getNameList.decode(iStream, false);
            return codeLength;
        }

        if (berIdentifier.equals(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 4)) {
            read = new ReadResponse();
            codeLength += read.decode(iStream, false);
            return codeLength;
        }

        if (berIdentifier.equals(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 5)) {
            write = new WriteResponse();
            codeLength += write.decode(iStream, false);
            return codeLength;
        }

        if (berIdentifier.equals(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 6)) {
            getVariableAccessAttributes = new GetVariableAccessAttributesResponse();
            codeLength += getVariableAccessAttributes.decode(iStream, false);
            return codeLength;
        }

        if (berIdentifier.equals(BerIdentifier.CONTEXT_CLASS, BerIdentifier.PRIMITIVE, 11)) {
            defineNamedVariableList = new BerNull();
            codeLength += defineNamedVariableList.decode(iStream, false);
            return codeLength;
        }

        if (berIdentifier.equals(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 12)) {
            getNamedVariableListAttributes = new GetNamedVariableListAttributesResponse();
            codeLength += getNamedVariableListAttributes.decode(iStream, false);
            return codeLength;
        }

        if (berIdentifier.equals(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 13)) {
            deleteNamedVariableList = new DeleteNamedVariableListResponse();
            codeLength += deleteNamedVariableList.decode(iStream, false);
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
