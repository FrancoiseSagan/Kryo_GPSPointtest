package org.pointkyto;

import org.pointkyto.kyro.GPSPointSerializer;
import org.pointkyto.model.GPSPoint;

import java.sql.Timestamp;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.Input;
import java.io.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        //generize Points

        GPSPoint[] points = new GPSPoint[10000];

        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            double lat = -180 + 360 * random.nextDouble();
            double lng = -180 + 360 * random.nextDouble();
            points[i] = new GPSPoint(new Timestamp(System.currentTimeMillis()), lng, lat);
        }

        //kryo

        long startTimeKryo = System.currentTimeMillis();
        byte[][] serializedDataKryo = new byte[10000][];
        Kryo kryo = new Kryo();
        GPSPointSerializer serializer = new GPSPointSerializer();

        for (int i = 0; i < 10000; i++) {
            Output outputKryo = new Output(48);
            serializer.write(kryo, outputKryo, points[i]);
            serializedDataKryo[i] = outputKryo.toBytes();
        }

        long endTimeKryo = System.currentTimeMillis();
        System.out.println("Time taken for Kryo serialization: " + (endTimeKryo - startTimeKryo) + " ms");

        long startTimeDeserializationKryo = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            Input input = new Input(serializedDataKryo[i]);
            GPSPoint deserializedPoint = serializer.read(kryo, input, GPSPoint.class);
            assert points[i].equals(deserializedPoint) : "Kryo deserialization failed!";
        }

        long endTimeDeserializationKryo = System.currentTimeMillis();
        System.out.println("Time taken for Kryo deserialization and assertion: " + (endTimeDeserializationKryo - startTimeDeserializationKryo) + " ms");

        //java default

        long startTimeJava = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            for (int i = 0; i < 10000; i++) {
                objectOutputStream.writeObject(points[i]);
            }
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] serializedDataJava = byteArrayOutputStream.toByteArray();
        long endTimeJava = System.currentTimeMillis();
        System.out.println("Time taken for Java default serialization: " + (endTimeJava - startTimeJava) + " ms");

        long startTimeDeserializationJava = System.currentTimeMillis();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedDataJava);
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            for (int i = 0; i < 10000; i++) {
                GPSPoint deserializedPoint = (GPSPoint) objectInputStream.readObject();
                assert points[i].equals(deserializedPoint) : "Java deserialization failed!";
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        long endTimeDeserializationJava = System.currentTimeMillis();
        System.out.println("Time taken for Java deserialization and assertion: " + (endTimeDeserializationJava - startTimeDeserializationJava) + " ms");

        int kryoTotalLength = 0;
        for (int i = 0; i < 10000; i++) {
            kryoTotalLength += serializedDataKryo[i].length;
        }

        int javaTotalLength = serializedDataJava.length;

        System.out.println("kryo: "+kryoTotalLength+"\n"+"java: "+javaTotalLength);
    }
}



