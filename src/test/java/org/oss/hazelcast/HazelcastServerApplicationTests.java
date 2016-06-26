package org.oss.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.locks.Lock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HazelcastServerApplicationTests {

	@Autowired
	private HazelcastInstance instance;




	@Test
	public void contextLoads() {
	}

	@Test
	public void testLock() throws Exception
	{
		Lock lock= instance.getLock("my-dis-lock");
		System.out.println("aquriing lock");
		System.out.println("aquriing lock");
		System.out.println("aquriing lock");

		lock.lock();
		System.out.println("Lock aquire");
		System.out.println("Lock aquire");
		System.out.println("Lock aquire");
		Thread.sleep(40000);
		lock.unlock();
		System.out.println("Lock  release");
		System.out.println("Lock  release");
		System.out.println("Lock  release");

		Thread.sleep(60000);

	}


}
