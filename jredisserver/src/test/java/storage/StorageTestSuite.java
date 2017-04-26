package storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import storage.exceptions.ExceptionsStorageTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        StorageTest.class,
        ExceptionsStorageTestSuite.class
})
public class StorageTestSuite {}
