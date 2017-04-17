package client.requests.dataStructures.list;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RequestLIndexTest.class,
        RequestLLenTest.class,
        RequestLPopTest.class,
        RequestLPushTest.class,
        RequestLRangeTest.class,
        RequestLRemTest.class,
        RequestLSetTest.class,
        RequestLTrimTest.class,
        RequestRPopTest.class,
        RequestRPushTest.class
})
public class RequestDataStructuresTestSuite {}
