import java.rmi.RemoteException;

public class Task implements TaskInterface {
    private CallbackInterface _callback;
    private String _Client_id;
    private String _taskData;
    private String _desc; // Ajoutez cette variable pour la description

    public Task(String _taskData, String _Client_id, CallbackInterface _Callback) {
        this._callback = _Callback;
        this._taskData = _taskData;
        this._Client_id = _Client_id;
    }

    public String execute() throws RemoteException {
        return this._taskData;
    }

    public String getClient_id() {
        return this._Client_id;
    }

    public CallbackInterface getCallback() {
        return this._callback;
    }
    public String getTaskData(){
        return this._taskData;
    }
 
}
