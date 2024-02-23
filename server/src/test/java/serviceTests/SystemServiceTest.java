package serviceTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.SystemService;

import java.util.HashSet;
import java.util.List;;

class SystemServiceTest extends TestVariables {

    SystemService mySystemService = null;
    @BeforeEach
    void createSystemService() {
        mySystemService = new SystemService(myAuthDAO, myGameDAO, myUserDAO);
    }
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
        myUserDAO.createUser(new UserData("username","password","email@gmail.com"));
        int gameID = myGameDAO.createGame("NewGame");
        String authToken = myAuthDAO.createAuth("username").authToken();

        mySystemService.clearData();

        Assertions.assertNull(myAuthDAO.getAuth(authToken));
        Assertions.assertNull(myGameDAO.getGame(gameID));
        Assertions.assertNull(myUserDAO.getUser("username"));
    }
}