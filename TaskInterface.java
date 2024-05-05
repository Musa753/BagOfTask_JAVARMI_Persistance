import java.io.Serializable;
import java.rmi.RemoteException;

public interface TaskInterface extends Serializable {
    String execute() throws RemoteException;
    String getClient_id();
    String getTaskData();
    CallbackInterface getCallback();
}
