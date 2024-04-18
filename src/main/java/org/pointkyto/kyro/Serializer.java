package org.pointkyto.kyro;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.pointkyto.model.GPSPoint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Serializer {

    private final Kryo kryo;

    public Serializer() {
        kryo = new Kryo();
        /*
        kryo.register(GPSPointSerializer.class);
        kryo.register(GPSPoint.class);
        kryo.register(HashMap.class);
        kryo.register(org.locationtech.jts.geom.Coordinate[].class);
        kryo.register(org.locationtech.jts.geom.impl.CoordinateArraySequence.class);
        kryo.register(org.locationtech.jts.geom.Coordinate.class);
        kryo.register(org.locationtech.jts.geom.GeometryFactory.class);
        kryo.register(org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory.class);
        kryo.register(org.locationtech.jts.geom.PrecisionModel.class);
        kryo.register(org.locationtech.jts.geom.PrecisionModel.Type.class);
        kryo.register(java.sql.Timestamp.class);
         */
    }

    public byte[] serialize(GPSPoint obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo.writeObject(output, obj);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    public GPSPoint deserialize(byte[] data) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Input input = new Input(byteArrayInputStream);
        GPSPoint obj = kryo.readObject(input, GPSPoint.class);
        input.close();
        return obj;
    }
}


