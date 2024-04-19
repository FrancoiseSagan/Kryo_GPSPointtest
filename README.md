## Kryo serializer test

#### Result

Class GPSPoint can't be serialized by kyro's default serializer

#### Reason&Improvement

Because kryo requires that types and all types that they depend on have parameterless constructors

However, overwriting the logic of kryo can fix it, by accessing the information of .class with the help of reflection when in deserialization:

```java
public class Kryox extends Kryo {

    private final ReflectionFactory REFLECTION_FACTORY = ReflectionFactory
            .getReflectionFactory();

    private final ConcurrentHashMap<Class<?>, Constructor<?>> _constructors = new ConcurrentHashMap<Class<?>, Constructor<?>>();

    @Override
    public <T> T newInstance(Class<T> type) {
        try {
            return super.newInstance(type);
        } catch (Exception e) {
            return (T) newInstanceFromReflectionFactory(type);
        }
    }

    private Object newInstanceFrom(Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T newInstanceFromReflectionFactory(Class<T> type) {
        Constructor<?> constructor = _constructors.get(type);
        if (constructor == null) {
            constructor = newConstructorForSerialization(type);
            Constructor<?> saved = _constructors.putIfAbsent(type, constructor);
            if(saved!=null)
                constructor=saved;
        }
        return (T) newInstanceFrom(constructor);
    }

    private <T> Constructor<?> newConstructorForSerialization(
            Class<T> type) {
        try {
            Constructor<?> constructor = REFLECTION_FACTORY
                    .newConstructorForSerialization(type,
                            Object.class.getDeclaredConstructor());
            constructor.setAccessible(true);
            return constructor;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```



#### Present test

When surfing in Zhihu, I found a serializer made by Alipay's employees, and he claims it is about 25 times faster than kryo, however:

(maybe it doesn't suit our GPSPoint object :)  )

##### Test settings

```java
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
```

generize points of indefinite length randomly

##### Kryo:

Serialization time: 26 ms

Serialized data length: 443502

Deserialization time: 331 ms

##### Fury:

Serialization time: 480 ms

Serialized data length: 712144

Deserialization time: 26 ms
