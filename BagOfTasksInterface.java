import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BagOfTasksInterface extends Remote {
    List<TaskInterface> getTasks() throws RemoteException;
    void addTaskToInitial(TaskInterface task) throws RemoteException;
    void addTaskToPending(List<TaskInterface> task) throws RemoteException;
    void addTaskToCompleted(TaskInterface task) throws RemoteException;
    public void addCompletedTasksFromPending() throws RemoteException;
}
