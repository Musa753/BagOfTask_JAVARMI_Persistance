import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.ArrayList;

public class WorkerRouter {
    private BagOfTasksInterface bagOfTask;
    private List<Worker> workers;
    private List<TaskInterface> tasks;

    public WorkerRouter(List<Worker> _workers) {
        try {
            int Port = 1099;
            Registry registry = LocateRegistry.getRegistry("localhost", Port);
            bagOfTask = (BagOfTasksInterface) registry.lookup("BagOfTasks");
            tasks= bagOfTask.getTasks();
            this.workers = _workers;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public List<TaskInterface> getListTasks(String _Client__id, List<TaskInterface> tasks){
        List<TaskInterface> tmp=new ArrayList<TaskInterface>();
        for (TaskInterface task : tasks){
            if (task.getClient_id().equals(_Client__id)){
                    tmp.add(task);
            }
        }
        tasks.removeAll(tmp);
        return tmp;
    }
    
    public void routeTasks() throws java.rmi.RemoteException{
        while (!this.tasks.isEmpty()) {
            String _Client__id = tasks.remove(0).getClient_id();
            List<TaskInterface> tmp = getListTasks(_Client__id, tasks);
            Worker worker = getAvailableWorker();
           if (worker != null) {
            if(tmp.size()>6){
                bagOfTask.addTaskToPending(getFirstNElements(tmp,6));
                worker.assignTasks(getFirstNElements(tmp,6));
                tasks.removeAll(getFirstNElements(tmp,6));
            }else{
                bagOfTask.addTaskToPending((tmp));
                worker.assignTasks(tmp);
                tasks.removeAll(tmp);
            }            
            }
            
        }
    }
    
    private List<TaskInterface> getFirstNElements(List<TaskInterface> sourceList, int n) {
        List<TaskInterface> result = new ArrayList<TaskInterface>();
        for (int i = 0; i < n && i < sourceList.size(); i++) {
            result.add(sourceList.get(i));
        }
        return result;
    }
    

    private Worker getAvailableWorker() {
        for (Worker worker : workers) {
            if (worker.isAvailable()) {
                return worker;
            }
        }
        return null;
    }

    public static void main(String[] args)throws java.rmi.RemoteException {
        List<Worker> workers = new ArrayList<Worker>();
        Worker work_1=new Worker("1");
        Worker work_2=new Worker("2");
        workers.add(work_2);
        workers.add(work_1);
        
        WorkerRouter workerRouter = new WorkerRouter(workers);
        workerRouter.routeTasks();

        
    }
}
