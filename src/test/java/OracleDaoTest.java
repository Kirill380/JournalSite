import com.liubun.dao.DAORecord;
import com.liubun.dao.OracleDAORecord;
import com.liubun.exception.PersistException;
import com.liubun.model.Record;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Nout on 15/03/2015.
 */
public class OracleDaoTest extends TestCase {
    private DAORecord daoR;

    @Before
    public void setUp()  {
        daoR = new OracleDAORecord();

    }

    @Test
    public void testPersist() throws PersistException {
        Record r = new Record();
        Record res = daoR.persist(r);
        assertTrue(res.getId() != 0);
    }


    @Test
    public void testGetByID() throws PersistException {
        Record r = daoR.getByID(11);
        assertNotNull(r);
    }


    @Test
    public void testDelete() throws PersistException {

    }

    @Test
    public void testUpdate() throws PersistException {

    }

}
