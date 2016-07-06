package org.oss.hazelcast.lock;

/**
 * Created by arang on 6/07/2016.
 */
public class HazelLockTimeoutException extends  RuntimeException
{
    public HazelLockTimeoutException()
    {
        super("Lock timeout exception");
    }
}
