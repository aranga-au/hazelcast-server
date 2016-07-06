# hazelcast-node test

NOTE: new TCP Joiner introduce to use AWS Instance Role (based on https://gist.github.com/ddossot/814fadc6e2cf577f87f7)

http://hazelcast.org/


sample application to test distributed locking and publisher subscriber mechanism.
Utilise spring auto configuration to load correct configurations (set env IS_AWS=true to activate EC2 discovery)
build will create executable compatible with build env

to execute in nix systems
./build/libs/hazelcast-server-0.0.1-SNAPSHOT.jar


to activate subscriber node

./build/libs/hazelcast-server-0.0.1-SNAPSHOT.jar --subscriber=true
