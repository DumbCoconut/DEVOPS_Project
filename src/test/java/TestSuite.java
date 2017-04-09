import client.requests.RequestTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import server.ServerTestSuite;
import storage.StorageTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RequestTestSuite.class,
        ServerTestSuite.class,
        StorageTestSuite.class
})
public class TestSuite {}
