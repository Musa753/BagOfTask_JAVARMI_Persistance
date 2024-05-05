import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private List<TaskInterface> taskList;
    private List<CallbackInterface> callbackList;
    private String Client_id;

    public Client(List<TaskInterface> tasks, String client_id, List<CallbackInterface> callbacks) {
        taskList = tasks;
        Client_id = client_id;
        callbackList = callbacks;
    }

    public List<TaskInterface> getTaskList() {
        return taskList;
    }

    public List<CallbackInterface> getCallbackList() {
        return callbackList;
    }

    public static void main(String[] args) {
        try {
            List<CallbackInterface> callbacks = new ArrayList<CallbackInterface>();
            CallbackInterface clB = new Callback();
            CallbackInterface clB1 = new Callback();
            CallbackInterface clB2 = new Callback();
            callbacks.add(clB);
            callbacks.add(clB1);
            callbacks.add(clB2);

            TaskInterface task1 = new Task("UPDATE TRANSACTION SET MONTANT = 400.00 WHERE VERSION=0", "1", callbacks.get(0));            
            TaskInterface task2 = new Task("SELECT * FROM COMPTE", "2", callbacks.get(1));
            TaskInterface task3 = new Task("SELECT * FROM CLIENT", "1", callbacks.get(1));
            TaskInterface task4 = new Task("SELECT * FROM TRANSACTION", "1", callbacks.get(0));
            TaskInterface task5 = new Task("SELECT * FROM COMPTE", "2", callbacks.get(1));
            TaskInterface task6 = new Task("SELECT * FROM CLIENT", "1", callbacks.get(1));


            List<TaskInterface> tasks = new ArrayList<TaskInterface>();
            tasks.add(task1);
            tasks.add(task2);
            tasks.add(task3);
            tasks.add(task4);
            tasks.add(task5);
            tasks.add(task6);

            final Client client = new Client(tasks, "1", callbacks); // Créez une instance de Client
            final Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            List<Thread> taskThreads = new ArrayList<Thread>();
            final int numThreads = 2; // Limitez à 2 threads
            final int tasksPerThread = tasks.size() / numThreads; // Nombre de tâches par thread
            final int size=tasks.size();
            for (int i = 0; i < size; i += tasksPerThread) {
                final int startIndex = i;
                Thread taskThread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            BagOfTasksInterface bagOfTasks = (BagOfTasksInterface) registry.lookup("BagOfTasks");
                            for (int j = startIndex; j < startIndex + tasksPerThread && j < size; j++) {
                                bagOfTasks.addTaskToInitial(client.getTaskList().get(j));
                                System.out.println("Thread "+startIndex+"sent task " + j + ": " + client.getTaskList().get(j).getTaskData() + "\n");
                            }
                            for (int j = startIndex; j < startIndex + tasksPerThread && j < size; j++) {
                                while (client.getTaskList().get(j).getCallback().getResultat() == null)
                                    Thread.sleep(2000);
                                System.out.println("Thread "+startIndex+"received result " + j + ": " + client.getTaskList().get(j).getCallback().getResultat() + "\n");
                            }
                            System.out.println("-------------------------------------------------------------------------------------\n");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                taskThreads.add(taskThread);
            }

            for (Thread thread : taskThreads) {
                thread.start();
            }

            for (Thread thread : taskThreads) {
                thread.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
