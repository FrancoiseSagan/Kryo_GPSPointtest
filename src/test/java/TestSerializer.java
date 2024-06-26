import org.junit.Test;
import org.junit.Assert;
import org.pointkyto.model.Attribute;
import org.pointkyto.model.GPSPoint;
import org.pointkyto.kryo.Serializer;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestSerializer {
    @Test
    public void testSerializationDeserialization() {
        GPSPoint[] points = new GPSPoint[5000];

        Random random = new Random();
        for (int i = 0; i < 5000; i++) {
            double lat = -180 + 360 * random.nextDouble();
            double lng = -180 + 360 * random.nextDouble();

            Attribute attribute1 = new Attribute("test1","test2");
            Attribute attribute2 = new Attribute("test3","test4");

            Map<String, Attribute> attributes = new HashMap<>();

            if(i%2 == 0) attributes.put("testtype1",attribute1);
            if(i%5 == 0) attributes.put("testtype2",attribute2);

            points[i] = new GPSPoint(new Timestamp(System.currentTimeMillis()), lng, lat, attributes);
        }

        Serializer serializer = new Serializer();

        long startTimeSerialize = System.currentTimeMillis();
        byte[] serializedData = serializer.serialize(points);
        long endTimeSerialize = System.currentTimeMillis();
        System.out.println("Serialization time: " + (endTimeSerialize - startTimeSerialize) + " ms");

        System.out.println("Serialized data length: " + serializedData.length);

        long startTimeDeserialize = System.currentTimeMillis();
        GPSPoint[] deserializedPoints = serializer.deserialize(serializedData);
        long endTimeDeserialize = System.currentTimeMillis();
        System.out.println("Deserialization time: " + (endTimeDeserialize - startTimeDeserialize) + " ms");

        Assert.assertEquals(points[0], deserializedPoints[0]);
        Assert.assertEquals(points[42], deserializedPoints[42]);
        Assert.assertEquals(points[1000], deserializedPoints[1000]);
        Assert.assertEquals(points[4999], deserializedPoints[4999]);
    }
}


