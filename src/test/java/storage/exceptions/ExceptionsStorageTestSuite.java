package storage.exceptions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.security.Key;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DuplicatedKeyExceptionTest.class,
        KeyExceptionTest.class,
        NonExistentKeyExceptionTest.class
})
public class ExceptionsStorageTestSuite {}
