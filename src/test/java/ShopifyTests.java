import com.rest.StoreInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShopifyTests {
    private String PROPERTY_FILE = "src/main/resources/StoreInfo.properties";
    private StoreInfo STORE_INFO = null;

    @BeforeEach
    void init(TestInfo testInfo) throws IOException {
        STORE_INFO = new StoreInfo();
    }

    @Test
    @DisplayName("Test for getting required properties")
    void testReadProperties() {
        assertNotNull(STORE_INFO);

        String storeUrl = null;
        String token = null;
        try {
            List<String> lines = Files.readAllLines(Paths.get(PROPERTY_FILE), StandardCharsets.UTF_8);
            if (lines.size() == 2) {
                storeUrl = lines.get(0).substring(lines.get(0).indexOf(":") + 1, lines.get(0).length());
                token = lines.get(1).substring(lines.get(1).indexOf(":") + 1, lines.get(1).length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(storeUrl);
        assertNotNull(token);
    }
}