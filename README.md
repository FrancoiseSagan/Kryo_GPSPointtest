### Kryo serializer test

#### Result

Class GPSPoint can't be serialized by kyro's default serializer

##### Reason

Because kryo requires that types and all types that depend on them have parameterless constructors

##### Present test(cumsom serializer)

Time taken for Kryo serialization: 129 ms

Time taken for Kryo deserialization and assertion: 10 ms

Time taken for Java default serialization: 97 ms

Time taken for Java deserialization and assertion: 90 ms

kryo: 240000

java: 1541332
