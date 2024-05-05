import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
public class BagOfTasks extends UnicastRemoteObject implements BagOfTasksInterface {
    private List<TaskInterface> pendingTasks;
    private List<TaskInterface> completedTasks;
    private List<TaskInterface> InitialTasks;

    public BagOfTasks() throws RemoteException {
        super(); // Appel au constructeur de UnicastRemoteObject
        pendingTasks = new ArrayList<TaskInterface>();
        completedTasks = new ArrayList<TaskInterface>();
        InitialTasks= new ArrayList<TaskInterface>();
    }

    public void addTaskToInitial(TaskInterface task) throws RemoteException {
        InitialTasks.add(task);
    }

    
    public void addTaskToPending(List<TaskInterface> tasks) throws RemoteException {
        pendingTasks.addAll(tasks);
    }

    public List<TaskInterface> getTasks() throws RemoteException {
        return InitialTasks;
    }
    
    public void addTaskToCompleted(TaskInterface task) throws RemoteException {
        completedTasks.add(task);
    }


    public void addCompletedTasksFromPending() throws RemoteException {
        List<TaskInterface> tasksToAddToCompleted = new ArrayList<TaskInterface>();
    
        for (TaskInterface task : pendingTasks) {
            if (task.getCallback() != null && task.getCallback().getResultat() != null) {
                tasksToAddToCompleted.add(task);
            }
        }
        completedTasks.addAll(tasksToAddToCompleted);
        pendingTasks.removeAll(tasksToAddToCompleted);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        InitialTasks.addAll(pendingTasks);
        pendingTasks.clear();
    }


}