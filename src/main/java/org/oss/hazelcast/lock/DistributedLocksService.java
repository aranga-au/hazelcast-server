package org.oss.hazelcast.lock;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;

/**
 * Created by arang on 6/07/2016.
 */
@Service
public class DistributedLocksService
{
    private static final String LOCK_MAP="U_LOCK_MAP";
    private ConcurrentMap<String,String> lockMap ;

    @Autowired
    private HazelcastInstance hazelcast;

    @PostConstruct
    public void postLoad()
    {

        lockMap  = hazelcast.getMap(LOCK_MAP);
        System.out.println("Lock map created");

    }

    public Lock getLock(String key)

    {
        return new HazelLock(key,lockMap);
    }


}
