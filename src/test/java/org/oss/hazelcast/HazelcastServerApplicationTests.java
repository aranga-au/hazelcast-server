package org.oss.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oss.hazelcast.lock.DistributedLocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HazelcastServerApplicationTests {



	private ExecutorService executorService = Executors.newFixedThreadPool(10000);

	private List<String> list;

	@Autowired
	private DistributedLocksService locksService;


	@Before
	public void  setUp()
	{
		list = new ArrayList<>();
	}


	@Test
	public void testTwoProcess() throws Exception
	{
		Lock lock = locksService.getLock("my-key");
		if (lock.tryLock(50, TimeUnit.SECONDS)) {
			System.out.println("Lock acure");
			Thread.sleep(60000);
			lock.unlock();
		}
		else
		{
			System.out.println("******************************************************");
			System.out.println("******Lock failed ***");
			System.out.println("******************************************************");

		}


	}

	public void testLock() throws  Exception
	{

			executorService.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println("Helo Executor A");
					Lock lock= locksService.getLock("my-lock");
					lock.lock();
					System.out.println("GOT LOCK A");

					list.add("AAA");
					try {
						System.out.println("Waiting t50");
						Thread.sleep(12000);
						System.out.println("Waiting A done");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					lock.unlock();;
					System.out.println("Lock release AAA");
				}
			});
		executorService.execute(new Runnable() {
			@Override
			public void run() {

				System.out.println("Helo Executor BBB");
				Lock lock= locksService.getLock("my-lock");
				lock.lock();
				System.out.println("GOT LOCK BBB");
				list.add("BBB");
				try {
					System.out.println("Waiting t50");
					Thread.sleep(12000);
					System.out.println("Waiting B done");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				lock.unlock();;
				System.out.println("Lock release BBB");
			}
		});

		Thread.sleep(300000);
	}

}
