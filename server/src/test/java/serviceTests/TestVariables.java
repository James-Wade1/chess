package serviceTests;

import dataAccess.AuthDAO;
import dataAccess.DAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import responseException.ResponseException;
import service.SystemService;

import java.util.HashSet;

public class TestVariables {

    protected AuthDAO myAuthDAO = null;
    protected GameDAO myGameDAO = null;
    protected UserDAO myUserDAO = null;

    @BeforeEach
    void initialize() {
        try {
            HashSet<DAO> myDAOs = SystemService.instantiateDAOs();
            for (DAO myDAO : myDAOs) {
                if (myDAO instanceof AuthDAO) {
                    this.myAuthDAO = (AuthDAO) myDAO;
                } else if (myDAO instanceof UserDAO) {
                    this.myUserDAO = (UserDAO) myDAO;
                } else if (myDAO instanceof GameDAO) {
                    this.myGameDAO = (GameDAO) myDAO;
                }
            }

            SystemService mySystemService = new SystemService(myAuthDAO, myGameDAO, myUserDAO);
            mySystemService.clearData();
        } catch(ResponseException ex) {
            Assertions.fail("Expected not error but got " + ex);
        }
    }
}
