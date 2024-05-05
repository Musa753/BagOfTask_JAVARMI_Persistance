import java.rmi.RemoteException;
import java.io.Serializable;
public interface OracleDatabaseConnectionInterface extends Serializable {

    public String executeTaskInDatabase(String taskData) throws RemoteException;
    public String getTableNameFromTaskData(String taskData)throws RemoteException;
    public void closeConnection() throws RemoteException;
    public boolean isAvailable() throws RemoteException;
    public void setInUse(boolean flag) throws RemoteException;
    public boolean isInUse()  throws RemoteException;
    // Vous pouvez ajouter d'autres méthodes si nécessaire
}