import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import server.ServerTest;
import storage.StorageTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ServerTest.class,
        StorageTest.class
})
public class TestSuite {}
