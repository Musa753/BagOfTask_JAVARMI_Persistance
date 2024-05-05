import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject{
    protected Server() throws RemoteException {
        super();
    }
    public static void main(String[] args) {
        try {
            //Créer une instance de BagOfTasksImpl
            BagOfTasksInterface bagOfTasks = new BagOfTasks();
            int port=1099;
            //Obtenez le registre RMI
            Registry registry = LocateRegistry.createRegistry(port);
            //Liez l'objet distant (le sac de tâches) au registre
            registry.rebind("BagOfTasks", bagOfTasks);
            System.out.println("Serveur prêt.");
        } catch (Exception e) {
            System.err.println("Erreur du serveur : " + e.toString());
        }
    }
}

 

