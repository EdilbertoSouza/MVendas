package br.com.mvendas.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DaoFactory {

	protected SQLiteDatabase db = null;
	private static SQLiteHelper dbHelper = null;
	
	// Dados do banco de dados
	protected static int VERSAO = 4;
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
		");",
		"CREATE TABLE " + ContatoDao.TABELA + " (" +
				"id_local INTEGER PRIMARY KEY AUTOINCREMENT," +
				"id TEXT," +
				"first_name TEXT NOT NULL," + 
				"last_name TEXT NOT NULL," +
				"title TEXT NOT NULL," +
				"department TEXT NOT NULL," +
				"primary_address_street TEXT," + 
				"primary_address_city TEXT," +
				"primary_address_state TEXT," + 
				"phone_mobile TEXT" +  
		");"
	};
	
	/**
	 * Cria o banco de dados SQLite com um script SQL
	 * @param context
	 */
	public DaoFactory(Context context) {
		dbHelper = new SQLiteHelper(context, NOME_BANCO, VERSAO, 
				SCRIPT_DATABASE_CREATE, SCRIPT_DATABASE_DELETE);
		
		//abre o banco no modo de escrita.
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Abre conexão com banco de dados SQLite
	 */
	public SQLiteDatabase open(Context context) {
		if(db == null){
			db = context.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
		}		
		return db;
	}

	/**
	 * Fecha conexão com banco de dados SQLite
	 */
	public void close() {
		if(db != null){
			db.close();
		}		
		if(dbHelper != null){
			dbHelper.close();
		}
		//Log.i("info", "MVendas finalizado");		
	}
	
}
