package br.com.mvendas.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DaoFactory {

	protected SQLiteDatabase db = null;
	private static SQLiteHelper dbHelper = null;
	
	// Dados do banco de dados
	protected static int VERSAO = 3;
	protected static String NOME_BANCO = "mvendas";
	
	private static final String[] SCRIPT_DATABASE_DELETE = {
		"DROP TABLE IF EXISTS " + ClienteDao.TABELA
	};
	
	private static final String[] SCRIPT_DATABASE_CREATE = {		
		"CREATE TABLE " + ClienteDao.TABELA + " (" +
				"id_local INTEGER PRIMARY KEY AUTOINCREMENT," +
				"id TEXT," +
				"name TEXT NOT NULL," + 
				"billing_address_street TEXT," + 
				"billing_address_city TEXT," +
				"billing_address_state TEXT," + 
				"phone_office TEXT," +  
				"website TEXT" +
		");"
	};
	
	/**
	 * Cria o banco de dados com um script SQL
	 * @param context
	 */
	public DaoFactory(Context context) {
		dbHelper = new SQLiteHelper(context, NOME_BANCO, VERSAO, 
				SCRIPT_DATABASE_CREATE, SCRIPT_DATABASE_DELETE);
		
		//abre o banco no modo de escrita.
		db = dbHelper.getWritableDatabase();
	}
	
	public void fechar() {
		if(db != null){
			db.close();
		}		
		if(dbHelper != null){
			dbHelper.close();
		}
	}
	
}
