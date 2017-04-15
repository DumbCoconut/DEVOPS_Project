package client;

import client.requests.RequestTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ClientTest.class,
        RequestTestSuite.class
})
public class ClientTestSuite {}