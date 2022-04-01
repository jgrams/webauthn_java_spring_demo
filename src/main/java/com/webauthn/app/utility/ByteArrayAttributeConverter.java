package com.webauthn.app.utility;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.yubico.webauthn.data.ByteArray;

@Converter(autoApply = true)
public class ByteArrayAttributeConverter implements AttributeConverter<ByteArray, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(ByteArray attribute) {
        return attribute.getBytes();
    }

    @Override
    public ByteArray convertToEntityAttribute(byte[] dbData) {
        return new ByteArray(dbData);
    }

}
