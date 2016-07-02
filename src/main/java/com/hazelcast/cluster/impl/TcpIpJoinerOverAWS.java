package com.hazelcast.cluster.impl;

import static com.amazonaws.regions.RegionUtils.getRegion;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.hazelcast.config.AwsConfig;
import com.hazelcast.instance.Node;
import com.hazelcast.logging.ILogger;
import com.hazelcast.util.ExceptionUtil;

/**
 * An Hazelcast {@link TcpIpJoiner} for EC2, which uses the official AWS SDK. Forked and rewritten
 * from the original class.
 */
public class TcpIpJoinerOverAWS extends TcpIpJoiner
{
    private final ILogger logger;
    private final AwsConfig awsConfig;
    private final AmazonEC2 ec2;

    public TcpIpJoinerOverAWS(final Node node)
    {
        super(Validate.notNull(node, "node can't be null"));

        System.out.println("HEllooo");
        logger = node.getLogger(getClass());
        awsConfig = node.getConfig().getNetworkConfig().getJoin().getAwsConfig();
        ec2 = new AmazonEC2Client(newAwsCredentialsProvider(awsConfig.getAccessKey(),
                awsConfig.getSecretKey()));

        if (isNotBlank(awsConfig.getRegion()))
        {
            ec2.setRegion(getRegion(awsConfig.getRegion()));
        }
    }

    private static AWSCredentialsProvider newAwsCredentialsProvider(final String accessKey,
                                                                    final String secretKey)
    {
        if (isBlank(accessKey) && isBlank(secretKey))
        {
            return new DefaultAWSCredentialsProviderChain();
        }

        return new StaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
    }

    @Override
    protected Collection<String> getMembers()
    {
        try
        {
            final Collection<String> list = getPrivateIpAddresses();
            if (list.isEmpty())
            {
                logger.warning("No EC2 instances found!");
            }
            else
            {
                if (logger.isFinestEnabled())
                {
                    final StringBuilder sb = new StringBuilder("Found the following EC2 instances:\n");
                    for (final String ip : list)
                    {
                        sb.append("    ").append(ip).append("\n");
                    }
                    logger.finest(sb.toString());
                }
            }
            return list;
        }
        catch (final Exception e)
        {
            logger.warning(e);
            throw ExceptionUtil.rethrow(e);
        }
    }

    private Collection<String> getPrivateIpAddresses()
    {
        final DescribeInstancesRequest request = new DescribeInstancesRequest();

        if (isNotBlank(awsConfig.getSecurityGroupName()))
        {
            request.getFilters().add(
                    new Filter().withName("group-name").withValues(awsConfig.getSecurityGroupName()));
        }

        if (isNotBlank(awsConfig.getTagKey()) && isNotBlank(awsConfig.getTagValue()))
        {
            request.getFilters().add(
                    new Filter().withName("tag:" + awsConfig.getTagKey()).withValues(awsConfig.getTagValue()));
        }

        final List<String> privateIpAddresses = new ArrayList<>();

        do
        {
            logger.finest("Describing instances with request: " + request);

            final DescribeInstancesResult result = ec2.describeInstances(request);
            handleDescribeInstancesResult(result, privateIpAddresses);

            request.setNextToken(result.getNextToken());
        }
        while (isNotBlank(request.getNextToken()));

        return privateIpAddresses;
    }

    private void handleDescribeInstancesResult(final DescribeInstancesResult result,
                                               final List<String> privateIpAddresses)
    {
        for (final Reservation reservation : result.getReservations())
        {
            handleReservation(reservation, privateIpAddresses);
        }
    }

    private void handleReservation(final Reservation reservation, final List<String> privateIpAddresses)
    {
        for (final Instance instance : reservation.getInstances())
        {
            handleInstance(instance, privateIpAddresses);
        }
    }

    private void handleInstance(final Instance instance, final List<String> privateIpAddresses)
    {
        if ("running".equals(instance.getState().getName()))
        {
            privateIpAddresses.add(instance.getPrivateIpAddress());

            logger.finest("Accepted instance: " + instance);
        }
        else
        {
            logger.finest("Ignoring not-running instance: " + instance);
        }
    }

    @Override
    protected int getConnTimeoutSeconds()
    {
        return awsConfig.getConnectionTimeoutSeconds();
    }

    @Override
    public String getType()
    {
        return "aws";
    }
}
