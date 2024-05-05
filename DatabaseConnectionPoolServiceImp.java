import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

public class DatabaseConnectionPoolServiceImp extends UnicastRemoteObject implements DatabaseConnectionPoolService {
    private LinkedList<OracleDatabaseConnectionImp> connections = new LinkedList<OracleDatabaseConnectionImp>();
    private int poolSize;

    public DatabaseConnectionPoolServiceImp(int poolSize) throws RemoteException {

        super();
        this.poolSize=poolSize;
        for (int i = 0; i < poolSize; i++) {
            OracleDatabaseConnectionImp newConnection = new OracleDatabaseConnectionImp();
                connections.add(newConnection);
        }
    }
    
 
    public void close() throws RemoteException {
        for (OracleDatabaseConnectionImp connection : connections) {
            connection.closeConnection();
        }
        connections.clear();
    }


    public String getAvailableDatabase( String taskData) throws RemoteException {
        for (OracleDatabaseConnectionImp _connection : connections) {
            if (_connection.isInUse()) {
                _connection.setInUse(false); // Marquez la connexion comme utilisée
                String result= _connection.executeTaskInDatabase(taskData);
                _connection.setInUse(true);
                return result;
            }
        }
        
        return null;
    }
    
}
