java -jar ./zookeeper/target/zookeeper-0.0.1-SNAPSHOT.jar --server.port=9999 &
sleep 5

java -jar ./userservice/target/userservice-0.0.1-SNAPSHOT.jar --server.port=9001 --application.id=1 &
java -jar ./userservice/target/userservice-0.0.1-SNAPSHOT.jar --server.port=9002 --application.id=2 &
java -jar ./userservice/target/userservice-0.0.1-SNAPSHOT.jar --server.port=9003 --application.id=3 &

java -jar ./inventoryservice/target/inventoryservice-0.0.1-SNAPSHOT.jar --server.port=9101 --application.id=1 &
java -jar ./inventoryservice/target/inventoryservice-0.0.1-SNAPSHOT.jar --server.port=9102 --application.id=2 &
java -jar ./inventoryservice/target/inventoryservice-0.0.1-SNAPSHOT.jar --server.port=9103 --application.id=3 &