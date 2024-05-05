import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DatabaseConnectionPoolServer {
    public static void main(String[] args) {
        try {
            DatabaseConnectionPoolService connectionPoolService = new DatabaseConnectionPoolServiceImp(3);
            int port = 1098; 
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("DatabaseConnectionPoolService", connectionPoolService);
            System.out.println("Serveur de pool de connexions prêt.");
        } catch (Exception e) {
            System.err.println("Erreur du serveur : " + e.toString());
        }
    }
}
