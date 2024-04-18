import org.junit.Test;
import org.junit.Assert;
import org.pointkyto.model.GPSPoint;
import org.pointkyto.kryo.Serializer;

import java.sql.Timestamp;

public class TestSerializer {
    @Test
    public void testSerializationDeserialization() {
        GPSPoint point = new GPSPoint(new Timestamp(System.currentTimeMillis()), 40.7128, -74.0060); // 以纽约市的经纬度为例

        Serializer serializer = new Serializer();

        byte[] serializedData = serializer.serialize(point);

        GPSPoint deserializedPoint = serializer.deserialize(serializedData);

        Assert.assertEquals(point, deserializedPoint);
    }
}

