package org.pointkyto;

import org.pointkyto.kyro.GPSPointSerializer;
import org.pointkyto.model.GPSPoint;

import java.sql.Timestamp;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.sql.Timestamp;

public class Main {
    public static void main(String[] args) {
        GPSPoint point = new GPSPoint(new Timestamp(System.currentTimeMillis()), 1.234, 5.678);

        // Serialize
        GPSPointSerializer serializer = new GPSPointSerializer();
        Kryo kryo = new Kryo();
        Output output = new Output(1024); // 初始大小为 1024 字节，可根据需要调整
        serializer.write(kryo, output, point);
        byte[] serializedData = output.toBytes();

        // Deserialize
        Input input = new Input(serializedData);
        GPSPoint deserializedPoint = serializer.read(kryo, input, GPSPoint.class);

        // Check equality
        System.out.println(point.equals(deserializedPoint));
    }
}

