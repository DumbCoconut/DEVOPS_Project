package client.requests.exceptions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        InvalidNbArgExceptionTest.class,
        NoTokensExceptionTest.class
})

public class ClientExceptionsTestSuite {}
