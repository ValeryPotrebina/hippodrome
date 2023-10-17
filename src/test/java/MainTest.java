import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

public class MainTest {

    @Test
    @Timeout(value = 21, unit = TimeUnit.SECONDS)
    public void mainTest() throws Exception {
        Main.main(null);
    }
}
