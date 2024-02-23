package serviceTests;

import dataAccess.AuthDAO;
import dataAccess.DAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import service.SystemService;

import java.util.HashSet;

public class TestVariables {

    protected AuthDAO myAuthDAO = null;
    protected GameDAO myGameDAO = null;
    protected UserDAO myUserDAO = null;

    @BeforeEach
    void initialize() {
        HashSet<DAO> myDAOs = SystemService.instantiateDAOs();
        for (DAO myDAO : myDAOs) {
            if (myDAO instanceof AuthDAO) {
                this.myAuthDAO = (AuthDAO)myDAO;
            }
            else if (myDAO instanceof UserDAO) {
                this.myUserDAO = (UserDAO)myDAO;
            }
            else if (myDAO instanceof GameDAO) {
                this.myGameDAO = (GameDAO)myDAO;
            }
        }
    }
}
