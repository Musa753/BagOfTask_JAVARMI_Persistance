import java.rmi.Remote;
import java.rmi.RemoteException;
public interface CallbackInterface extends Remote {
    String getResultat() throws RemoteException;
    // Autres méthodes nécessaires setResultat
    void setResultat(String resultat) throws RemoteException;
}
