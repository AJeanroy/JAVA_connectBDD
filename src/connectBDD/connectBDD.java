package connectBDD;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

public class connectBDD {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		
		//Chargement du pilote
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		//Creation d'une connexion
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/test?" + "user=root&password=");
			conn.setReadOnly(true);
			
			InfoBase(conn);
			AffichageWarnings(conn);
			
			if(conn.isValid(1000))
				System.out.println("Connection valid");
			
			if(conn.isReadOnly())
				System.out.println("Connection is set on read only mode");
		         
		    } catch (SQLException e) {
		    	System.out.println("SQLException: " + e.getMessage());
		        System.out.println("SQLState: " + e.getSQLState());
		        System.out.println("VendorError: " + e.getErrorCode());
		    }
		
		
		//Creation de statements génériques
		/*try {
				Statement statement1 = conn.createStatement();
				Statement statement2 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		
		
		//Fermeture de la connexion
		try {
			conn.close();
			
			if(conn.isClosed())
				System.out.println("Connection closed");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Gere l'affichage des warnings SQL
	public static void AffichageWarnings(Connection conn){
		SQLWarning warning = null;
		SQLException ex;
		
		try {
			warning = conn.getWarnings();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(warning.getNextWarning() != null){
			ex = null;
			
			System.out.println("SQLException: " + ex.getMessage());
	        System.out.println("SQLState: " + ex.getSQLState());
	        System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		try {
			conn.clearWarnings();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Affiche les informations de la base
	public static void InfoBase(Connection conn){
		DatabaseMetaData databaseMetaData = null;
		try {
			databaseMetaData = conn.getMetaData();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			String   catalog          = null;
			String   schemaPattern    = null;
			String   tableNamePattern = null;
			String[] types            = null;
			String procedureNamePattern = null;
			
			int majorVersion = databaseMetaData.getDatabaseMajorVersion();
			int minorVersion = databaseMetaData.getDatabaseMinorVersion();

			String productName = databaseMetaData.getDatabaseProductName();
			String productVersion = databaseMetaData.getDatabaseProductVersion();
			String driverName = databaseMetaData.getDriverName();
			String userName = databaseMetaData.getUserName();
			String dbURL = databaseMetaData.getURL();
			ResultSet res = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);
			ResultSet methods = databaseMetaData.getProcedures(catalog, schemaPattern, procedureNamePattern);
			
			System.out.println("Version: "+majorVersion+"."+minorVersion);
			System.out.println("Produit: "+productName);
			System.out.println("Version produit: "+productVersion);
			System.out.println("Pilote: "+driverName);
			System.out.println("Utilisateur: "+userName);
			System.out.println("URL: "+dbURL);
			
			System.out.println("Tables: ");
			while(res.next()) {
				System.out.println(res.getString(3));
			}
			
			System.out.println("Procédures: ");
			while(methods.next()) {
				System.out.println(methods.getString(3));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void TestExecute(Connection conn){
		String query = null;
		Statement statement = null;
		try{
	        BufferedReader bufferedReader = new BufferedReader(new FileReader("queries.txt"));
	        while(bufferedReader.readLine()!=null){
	             query = new StringBuilder().append(query).append(bufferedReader.readLine()).toString();
	        }
	        
	        statement = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    }
	    catch (FileNotFoundException e){
	        e.printStackTrace();
	    }
	    catch (IOException e){
	        e.printStackTrace();
	        
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(statement.execute(query)){
				ResultSet res = statement.getResultSet();
				int rows = statement.getMaxRows();
				
				while(res.next()) {
					System.out.println(res.getString(3));
				}
				System.out.println(rows);
			}else{
				System.out.println("Nombre de modifications: "+statement.getUpdateCount());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
