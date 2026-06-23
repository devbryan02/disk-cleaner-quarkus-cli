package bryan.app;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainTest;

@QuarkusMainTest
public class DiskCleanerCommandTest {

    @Test
    @Launch({ "--help" })
    public void testHelp(LaunchResult result) {
        assertTrue(result.getOutput().contains("cleanb"));
        assertTrue(result.getOutput().contains("order"));
        assertTrue(result.getOutput().contains("temp"));
        assertTrue(result.getOutput().contains("recycle-bin"));
        assertTrue(result.getOutput().contains("large-folders"));
    }
}
