import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.rmi.RemoteException;
import java.util.StringTokenizer;

public class OracleDatabaseConnectionImp{

    // LOGIN et MDP doivent correspondre à ceux de la BD
    final private static String LOGIN = "mt244221";
    final private static String MDP = "mt244221";
    // URL pour se connecter à Eluard
    final private static String URL = "jdbc:oracle:thin:@eluard:1521:eluard2023";

    private Connection connection;
    boolean flag;

    public OracleDatabaseConnectionImp () {
        try {
            // Chargement du driver Oracle
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Ouverture de la connexion
            connection = DriverManager.getConnection(URL, LOGIN, MDP);
            System.out.println("Connexion à la base de données établie avec succès.");
            flag=true;
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Le driver Oracle n'a pas pu être chargé.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur : La connexion à la base de données a échoué.");
            e.printStackTrace();
        }
    }

    public String executeTaskInDatabase(String taskData) throws RemoteException {
        String table_name = getTableNameFromTaskData(taskData);
        if(table_name==null)
           table_name= getTableNameFromTaskDataUpdate(taskData);

        String operation=getFirstWord(taskData);
        if (connection == null) {
            System.err.println("Erreur : La connexion à la base de données n'a pas été établie.");
            return null;
        }
        String result = null;
        try {
            if ("SELECT".equals(operation)) {
                result = executeSelect(taskData);
            } else if ("INSERT".equals(operation)) {
                result = executeInsert(taskData);
            } else if ("UPDATE".equals(operation)) {
                result = executeUpdateWithRetry(taskData);
            } else if ("DELETE".equals(operation)) {
                result = executeDeleteWithRetry(taskData);
            } else {
                System.err.println("Opération non prise en charge : " + operation);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'exécution de la tâche dans la base de données.");
            e.printStackTrace();
        }
    
        return result;
    }
    
    public String executeSelect(String sql){
        try {
            PreparedStatement selectStmt = connection.prepareStatement(sql);
            ResultSet resultSet = selectStmt.executeQuery();
            String result ="operation échoué";
            String table_name="";
            while (resultSet.next()) {
                try {
                      table_name = getTableNameFromTaskData(sql);
                    // Le reste de votre code
                } catch (RemoteException e) {
                    throw new RuntimeException("Erreur lors de la récupération du nom de la table.", e);
                }
                switch (table_name.charAt(table_name.length() - 1)) {
                    case 'T':
                        result += "Nom: " + resultSet.getString("Nom") + "\n"
                                + "Prénom: " + resultSet.getString("Prenom") + "\n"
                                + "Adresse: " + resultSet.getString("Adresse") + "\n"
                                + "Numéro de téléphone: " + resultSet.getString("NumeroTelephone") + "\n\n";
                        break;
                    case 'E':
                        result += "Solde: " + resultSet.getString("Solde") + "\n"
                                + "Type de compte: " + resultSet.getString("TypeDeCompte") + "\n\n";
                        break;
                    case 'N':
                        result += "TRANSACTION: " + resultSet.getString("TypeDeTransaction") + "\n"
                                + "Montant: " + resultSet.getString("Montant") + "\n"
                                + "Date de transaction: " + resultSet.getString("DateTransaction") + "\n\n";
                        break;
                    default:
                        result = "Table inconnue : " + table_name;
                        break;
                }
            }
    
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestion des erreurs
            return null;
        }
    }
    
    
    
    public String executeUpdateWithRetry(String sql) {
        int maxRetries = 3; // Nombre maximum de réessais
        String result="operation echouee";
        for (int i = 0; i < maxRetries; i++) {
            try {
                PreparedStatement updateStmt = connection.prepareStatement(sql);
                int updatedRows = updateStmt.executeUpdate();
    
                if (updatedRows > 0) {
                    // Mise à jour réussie
                    result="Mise à jour réussie";
                  
                } else {
                   // result=executeUpdateWithRetry(incrementVersionQuery(sql,updateStmt.getString("version")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Gestion des erreurs
            }
           
    }
    return result;
}
    
    public String executeDeleteWithRetry(String sql) {
        int maxRetries = 1; // Nombre maximum de réessais
        String result="operation echouee";
        for (int i = 0; i < maxRetries; i++) {
            try {
                PreparedStatement deleteStmt = connection.prepareStatement(sql);
                int deletedRows = deleteStmt.executeUpdate();
                if (deletedRows > 0) {
                     result="suppression reuissi";
                } else {
                 result=executeUpdateWithRetry(incrementVersionQuery(sql));
                }
            } catch (SQLException e) {
                e.printStackTrace();
           }
          
        }
    
        return result;
    }
    
    public String executeInsert(String insertQuery) throws SQLException {
        String result="la requete a echouee";
        try{
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
            int updatedRows = insertStmt.executeUpdate();
            if (updatedRows > 0) {
                result = "L'insertion a reussi.";
            } else {
                result = "L'insertion a echoue.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
       }
        return result;
    }
    
    
    

    public void closeConnection() throws RemoteException {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion à la base de données fermée avec succès.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion à la base de données.");
                e.printStackTrace();
            }
        }
    }
 public boolean isAvailable(){
    return true;
 }

 public void setInUse(boolean flag){
    this.flag=flag;
 }
    
 public boolean isInUse(){
    return this.flag;
 }
 
 public  String getTableNameFromTaskData(String taskData)throws RemoteException {
     int fromIndex = taskData.indexOf("FROM");

    if (fromIndex != -1) {
         String afterFrom = taskData.substring(fromIndex + 4).trim();

        String[] words = afterFrom.split("\\s+");
        if (words.length > 0) {
            return words[0];
        }
    }
    return null;
}

public  String getTableNameFromTaskDataUpdate(String taskData)throws RemoteException {
    int fromIndex = taskData.indexOf("SET");

   if (fromIndex != -1) {
        String afterFrom = taskData.substring(fromIndex + 4).trim();

       String[] words = afterFrom.split("\\s+");
       if (words.length > 0) {
           return words[0];
       }
   }
   return null;
}

public static String getFirstWord(String input) {
    if (input != null) {
        StringTokenizer tokenizer = new StringTokenizer(input);
        if (tokenizer.hasMoreTokens()) {
            return tokenizer.nextToken();
        }
    }
    return null;
}


public String incrementVersionQuery(String inputString) {
    if (inputString == null || inputString.length() == 0) {
        return "Chaîne invalide";
    }

    char[] charArray = inputString.toCharArray();
    int lastIndex = charArray.length - 1;

    if (Character.isDigit(charArray[lastIndex])) {
        int lastDigit = Character.digit(charArray[lastIndex], 10);
        lastDigit++; // Incrémentation du dernier chiffre

        if (lastDigit >= 0 && lastDigit <= 9) {
            charArray[lastIndex] = (char) (lastDigit + '0');
            return new String(charArray);
        }
    }

    return "Aucun chiffre à incrémenter";
}

}
