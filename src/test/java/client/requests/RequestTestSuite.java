package client.requests;

import client.requests.dataTypes.DataTypesTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RequestTest.class,
        RequestWithKeyTest.class,
        DataTypesTestSuite.class
})
public class RequestTestSuite {}
