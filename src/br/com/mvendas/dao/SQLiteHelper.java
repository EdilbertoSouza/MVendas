package br.com.mvendas.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper{

	private static final String LOG_BD = "bd";
	private String[] scriptSQLCreate;
	private String[] scriptSQLDelete;
		
	/**
	 * Cria uma instancia de SQLiteHelper
	 * 
	 * @param context
	 * @param nomeBanco nome do banco de dados
	 * @param versaoBanco versao do banco de dados (se for diferente eh para atualizar)
	 * @param scriptSQLCreate SQL com o create table...
	 * @param scriptSQLDelete SQL com o drop table...
	 * 
	 */
	public SQLiteHelper(Context context, String nomeBanco, int versaoBanco,
			String[] scriptSQLCreate, String[] scriptSQLDelete) {
		
		super(context, nomeBanco, null, versaoBanco);
		this.scriptSQLCreate = scriptSQLCreate;
		this.scriptSQLDelete = scriptSQLDelete;
	}

	/**
	 * Cria um banco de dados executando o script de criacao.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(LOG_BD, "Criando banco com sql.");
		
		int qtdScripts = scriptSQLCreate.length;
		
		for (int i = 0; i < qtdScripts; i++) {
			String sql = scriptSQLCreate[i];			
			Log.i(LOG_BD, sql);			
			db.execSQL(sql);
		}
	}

	/**
	 * Mudou a versao...
	 * 
	 * Deleta as tabelas, removendo todos os registros do banco, e
	 * vai cria-las novamente.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LOG_BD, "Atualizando a versao: " + oldVersion + " para " + 
				newVersion +". Todos os registros serao removidos.");		
		
		int qtdScripts = scriptSQLDelete.length;
		
		for (int i = 0; i < qtdScripts; i++) {
			String sql = scriptSQLDelete[i];			
			Log.i(LOG_BD, sql);			
			db.execSQL(sql);
		}		
		onCreate(db);
	}
}
