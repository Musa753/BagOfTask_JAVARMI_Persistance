import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.ArrayList;


public class Worker {
    private List<TaskInterface> tasks;
    private List<Thread> Liste_Threads;
    DatabaseConnectionPoolService connectionPoolService;
    private String Worker_Id;
    public OracleDatabaseConnectionInterface connection;

    public Worker(String id)throws java.rmi.RemoteException{
        tasks = new ArrayList<TaskInterface>();
        Liste_Threads=new ArrayList<Thread>();
        this.Worker_Id=id;
        this.connection=null;
       
        int port=1098;
        try {
        Registry registry = LocateRegistry.getRegistry("localhost", port);
        this.connectionPoolService = (DatabaseConnectionPoolService) registry.lookup("DatabaseConnectionPoolService");
       } catch (java.rmi.NotBoundException e) {
        e.printStackTrace();
       } catch (Exception e) {
        e.printStackTrace();
      }
      Thread_IniZialing();
    }
    
    public void Thread_IniZialing() {
        final int numThreads = 2; // Limitez à 2 threads
        final int tasksPerThread = 3; // Nombre de tâches par thread
    
        for (int i = 0; i < numThreads; i++) {
            final int startIndex = i * tasksPerThread;
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        for (int j = startIndex; j < startIndex + tasksPerThread && j<5; j++) {
                            String result = null;
                            while (result == null) {
                                Thread.sleep(1000);
                                if (!tasks.isEmpty()) {
                                    TaskInterface task = tasks.remove(0);
                                    result = connectionPoolService.getAvailableDatabase(task.execute());
                                    task.getCallback().setResultat(result);
                                    System.out.println(result);
                                    System.out.println(Liste_Threads.size()+1);
                                } else {
                                    Thread.sleep(1000);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Liste_Threads.add(thread);
            thread.start();
        }
    }
    
    
  
    
    public void assignTasks(List<TaskInterface> newTasks) throws java.rmi.RemoteException{
        if (newTasks != null) {
            tasks.addAll(newTasks);
            executeTasks();
        }
    }

    public boolean isAvailable() {
        return tasks.isEmpty();
    }


    

    public void executeTasks() {
        for (Thread thread : Liste_Threads) {
            if (thread.getState() == Thread.State.NEW) {
                thread.start();
            }
        }
    }
    
}
