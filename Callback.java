import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Callback extends UnicastRemoteObject implements CallbackInterface{
    private String _Resultat ;

    public Callback() throws RemoteException {
        super();  
        this._Resultat = null;  
    }

    public String getResultat() throws RemoteException {
        return _Resultat;
    }
    public void setResultat(String resultat)  throws RemoteException{
        this._Resultat = resultat;
    }
}
