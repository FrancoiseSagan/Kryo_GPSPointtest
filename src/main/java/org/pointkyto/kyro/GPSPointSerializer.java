package org.pointkyto.kyro;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.pointkyto.model.GPSPoint;

import java.sql.Timestamp;

import com.esotericsoftware.kryo.Serializer;

public class GPSPointSerializer extends Serializer<GPSPoint> {
    @Override
    public void write(Kryo kryo, Output output, GPSPoint point) {
        output.writeLong(point.getTime().getTime());
        output.writeDouble(point.getLng());
        output.writeDouble(point.getLat());
    }

    @Override
    public GPSPoint read(Kryo kryo, Input input, Class<? extends GPSPoint> type) {
        long timeMillis = input.readLong();
        double longitude = input.readDouble();
        double latitude = input.readDouble();
        return new GPSPoint(new Timestamp(timeMillis), longitude, latitude);
    }
}




