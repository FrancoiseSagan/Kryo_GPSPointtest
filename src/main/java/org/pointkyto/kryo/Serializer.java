package org.pointkyto.kryo;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.pointkyto.model.GPSPoint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Serializer {

    private final Kryox kryox;

    public Serializer() {
        kryox = new Kryox();
        kryox.register(GPSPoint.class);
        kryox.register(HashMap.class);
        kryox.register(org.locationtech.jts.geom.Coordinate[].class);
        kryox.register(org.locationtech.jts.geom.impl.CoordinateArraySequence.class);
        kryox.register(org.locationtech.jts.geom.Coordinate.class);
        kryox.register(org.locationtech.jts.geom.GeometryFactory.class);
        kryox.register(org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory.class);
        kryox.register(org.locationtech.jts.geom.PrecisionModel.class);
        kryox.register(org.locationtech.jts.geom.PrecisionModel.Type.class);
        kryox.register(java.sql.Timestamp.class);
    }

    public byte[] serialize(GPSPoint obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryox.writeObject(output, obj);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    public GPSPoint deserialize(byte[] data) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Input input = new Input(byteArrayInputStream);
        GPSPoint obj = kryox.readObject(input, GPSPoint.class);
        input.close();
        return obj;
    }
}


