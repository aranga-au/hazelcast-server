package org.oss.hazelcast.lock;

import com.hazelcast.core.HazelcastInstance;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by arang on 6/07/2016.
 */
public class HazelLock implements Lock
{

    private String uniqueKey;
    private ConcurrentMap<String,String> map;

    private String uuid = UUID.randomUUID().toString();


    HazelLock(String uniqueKey, ConcurrentMap<String,String> map) {
        this.uniqueKey = uniqueKey;
        this.map = map;
    }

    @Override
    public void lock()
    {

        //try for ever;
        System.out.println("Tring lock "+uuid);
       Object o = waitAndTry(999999999999L,9999999999999999L);
        if (o == null)
        {
            throw new HazelLockTimeoutException();
        }
        //try for ever
    }

    @Override
    public void lockInterruptibly() throws InterruptedException
    {

    }

    private Object waitAndTry(long times,long time)
    {
        String o= "11";
        long i =0;
        long t = System.currentTimeMillis();
        DateTime stime = new DateTime(new Date());
        System.out.println("Try lock for thread "+Thread.currentThread().getId());
        time = time +150; //minimul 150 ms
        long m = 0;

        while ( (!uuid.equals(o)) && i < times  && (m < time) )
        {

            m = System.currentTimeMillis()-t;
            o = map.putIfAbsent(uniqueKey,uuid);

            i++;
            if (i >= times)
            {
                break;
            }



            System.out.println(m+" "+time);
            sleepRandom();


        }
        System.out.println();
        System.out.println("current thred id"+Thread.currentThread().getId());
        return  o;
    }
    @Override
    public boolean tryLock()
    {
        Object o = waitAndTry(1000,500);
        System.out.println("object id"+o);
        if (o == null)
        {
            o = map.get(uniqueKey);
        }
        System.out.println(uuid.equals(o));
        return uuid.equals(o);
    }

    private void sleepRandom()
    {
        int wait = 100 + ((int)(Math.random()* 200));
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException
    {

        long waitTime = unit.toMillis(time);
        Object o = waitAndTry(999999999,waitTime);
        return  uuid.equals(o);
    }

    @Override
    public void unlock()
    {
        System.out.println("Lock release "+uuid);
        if (map.get(uniqueKey).equals(uuid))
        {
            System.out.println("All good");
        }

        System.out.println("Removed "+map.remove(uniqueKey));

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
