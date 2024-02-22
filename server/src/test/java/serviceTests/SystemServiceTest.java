package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.SystemService;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SystemServiceTest {

    @Test
    void instantiateDAOs() {
        HashSet<DAO> myDAOs = SystemService.instantiateDAOs();

        HashSet<DAO> expected = new HashSet<>(List.of(new MemoryAuthDAO(), new MemoryUserDAO(), new MemoryGameDAO()));

        int expectedSize = expected.size();
        int actualSize = 0;

        for (DAO myDAO : myDAOs) {
            if (myDAO instanceof AuthDAO) {
                actualSize++;
            }
            else if (myDAO instanceof UserDAO) {
                actualSize++;
            }
            else if (myDAO instanceof GameDAO) {
                actualSize++;
            }
        }

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    void clearData() {
    }
}