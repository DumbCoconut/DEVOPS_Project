package client.requests.dataTypes;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RequestGetTest.class,
        RequestSetTest.class,
        RequestTypeTest.class,
        RequestDecrTest.class,
        RequestDecrByTest.class,
        RequestIncrTest.class,
        RequestIncrByTest.class,
        RequestDelTest.class
})
public class RequestDataTypesTestSuite {}
