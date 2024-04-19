import org.apache.fury.Fury;
import org.apache.fury.config.Language;
import org.junit.Assert;
import org.junit.Test;
import org.pointkyto.model.Attribute;
import org.pointkyto.model.GPSPoint;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class testsFury {
    @Test
    public void test_fury() {
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

        Fury fury = Fury.builder().withLanguage(Language.JAVA)
                // Allow to deserialize objects unknown types, more flexible
                // but may be insecure if the classes contains malicious code.
                .requireClassRegistration(true)
                .build();

        fury.register(GPSPoint.class);
        fury.register(GPSPoint.class);
        fury.register(HashMap.class);
        fury.register(org.pointkyto.model.GPSPoint[].class);
        fury.register(org.locationtech.jts.geom.Coordinate[].class);
        fury.register(org.locationtech.jts.geom.impl.CoordinateArraySequence.class);
        fury.register(org.locationtech.jts.geom.Coordinate.class);
        fury.register(org.pointkyto.model.Attribute.class);
        fury.register(org.locationtech.jts.geom.GeometryFactory.class);
        fury.register(org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory.class);
        fury.register(org.locationtech.jts.geom.PrecisionModel.class);
        fury.register(org.locationtech.jts.geom.PrecisionModel.Type.class);
        fury.register(java.sql.Timestamp.class);

        long startTimeSerialize = System.currentTimeMillis();

        byte[] serializedData = fury.serialize(points);

        long endTimeSerialize = System.currentTimeMillis();

        System.out.println("Serialization time: " + (endTimeSerialize - startTimeSerialize) + " ms");

        System.out.println("Serialized data length: " + serializedData.length);

        long startTimeDeserialize = System.currentTimeMillis();

        GPSPoint[] deserializedPoints = (GPSPoint[]) fury.deserialize(serializedData);

        long endTimeDeserialize = System.currentTimeMillis();

        System.out.println("Deserialization time: " + (endTimeDeserialize - startTimeDeserialize) + " ms");

        Assert.assertEquals(points[0], deserializedPoints[0]);
        Assert.assertEquals(points[42], deserializedPoints[42]);
        Assert.assertEquals(points[1000], deserializedPoints[1000]);
        Assert.assertEquals(points[4999], deserializedPoints[4999]);
    }
}
