package org.oss.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HazelcastServerApplicationTests {


	@Autowired
	private HazelcastInstance hazelcast;



	@Test
	public void contextLoads() {
	}

	@Test
	public void testHazelcast()
	{
		 hazelcast.getMap("my-map");

	}

}
