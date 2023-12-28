package it.finanze.sanita.fse2.ms.gtw.garbage;

import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.enums.ConfigItemTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles(Constants.Profile.TEST)
@Slf4j
class ConfigItemTypeEnumTest {

    @Test
    void testPriority() {
        // Call the priority method
        List<ConfigItemTypeEnum> items = ConfigItemTypeEnum.priority();

        // Assert that the first item is GENERIC
        assertEquals(ConfigItemTypeEnum.GENERIC, items.get(0));

        // Assert that the last item is GARBAGE
        assertEquals(ConfigItemTypeEnum.GARBAGE, items.get(items.size() - 1));
    }
}

