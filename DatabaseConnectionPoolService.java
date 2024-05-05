import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DatabaseConnectionPoolService extends Remote {
    public String getAvailableDatabase( String taskData) throws RemoteException;
    public void close() throws RemoteException;
}

//java -cp .:hikari-cp-4.0.3.jar:oracle-jdbc-driver.jar MainClass
