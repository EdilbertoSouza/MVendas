package br.com.mvendas.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.mvendas.comunication.SugarClientSingleton;
import br.com.mvendas.model.Contato;
import br.com.mvendas.utils.Constantes;
import br.com.mvendas.utils.StringUtil;

public class ContatoDao {
	
	private SugarClientSingleton sc;
	private String session;
	private static SQLiteDatabase db = null;
	private static final String LOG_BD = "bd";	
	protected static String NOME_BANCO = "mvendas"; 
	protected static String TABELA = "contato";
	protected static String fields[] = {
		"id_local",
		"id", 
		"first_name", 
		"last_name", 
		"title", 
		"department",
		"primary_address_street", 
		"primary_address_city", 
		"primary_address_state", 
		"phone_mobile"
	};

	public ContatoDao(Context context) {	
		sc = SugarClientSingleton.getInstance();
		session = sc.getSession();		
		db = abrirLocal(context);
	}
	
	public List<Contato> listar() throws Exception {
		return listarLocal();
	}
	
	/**
	 * Lista todos os contatos do banco
	 * select * from contato
	 * 
	 * @return List<Contato>
	 * 
	 */
	public List<Contato> listarLocal(){
		List<Contato> contatos = null;
		Contato contato = null;
		Cursor c = null;
		
		try {
			c = db.query(TABELA, fields, null, null, null, null, null, null);
		} catch (Exception e) {
			Log.e(LOG_BD, "Erro ao listar contatos: " + e.getMessage(), e);
			fecharLocal();
			return null;
		}
	
		if(c != null){
						
			contatos = new ArrayList<Contato>();
			
			if(c.moveToFirst()){
				do{
					contato = new Contato();
					
					contato.setIdLocal(c.getLong(c.getColumnIndex("id_local")));
					contato.setId(c.getString(c.getColumnIndex("id")));
					contato.setName(c.getString(c.getColumnIndex("first_name")));
					contato.setLastName(c.getString(c.getColumnIndex("last_name")));
					contato.setCargo(c.getString(c.getColumnIndex("title")));
					contato.setDepto(c.getString(c.getColumnIndex("department")));					
					contato.setStreet(c.getString(c.getColumnIndex("primary_address_street")));
					contato.setCity(c.getString(c.getColumnIndex("primary_address_city")));
					contato.setState(c.getString(c.getColumnIndex("primary_address_state")));
					contato.setPhone(c.getString(c.getColumnIndex("phone_mobile")));
					//contato.setEmail(c.getString(c.getColumnIndex("email")));
					
					contatos.add(contato);
					
				} while(c.moveToNext());
				
				c.close();
			}
		} 
		
		return contatos;
	}

	public List<Contato> listarRemoto(String where, String max) throws Exception {
		List<Contato> contatos = new ArrayList<Contato>();

		String select_fields = StringUtil.toArrayData(fields);
		
		// Definindo os parametros para chamar o método web
		String parameters[][] = { 
			{"session", session}, 
			{"module_name", "Contacts"},
			{"query", where},
			{"order_by", ""},
			{"offset", "0"},
			{"select_fields", select_fields}, 
			{"link_name_to_fields_array", "[]"}, 
			{"max_results", max},
			{"deleted", "0"},
			{"favorites", "false"}
		};
					
		// Chamando o método web
		String result = sc.call("get_entry_list", parameters);

		// Tratando o retorno para devolver apenas a lista
		if (result.length() < 10) {
			throw new Exception(result);			
		} else {
			JSONObject jsonData = (JSONObject) new JSONTokener(result).nextValue();	
			JSONArray entry_list = jsonData.getJSONArray("entry_list");
			for (int i = 0;i < entry_list.length();i++) {
				JSONObject register = (JSONObject) entry_list.get(i);
				JSONObject name_value_list = register.getJSONObject("name_value_list");
				String name_values = "";
				for (int j = 1;j < fields.length;j++) {
					JSONObject name_value = (JSONObject) name_value_list.get(fields[j]);
					name_values += name_value.getString("name") + "=" + name_value.getString("value") + ";"; 
				}
				Contato contato = new Contato(name_values);
				contatos.add(contato);						
			}					
		}
		return contatos;
	}
	
	public long salvar(Contato contato) {
		return salvarLocal(contato);		
	}
	
	public long salvarRemoto(Contato contato) {
		long success = 1;
		// Definindo os parametros para chamar o método web
		String name_value_list[][][] = {
			{{"name", "id"}, {"value", contato.getId()}},
			{{"name", "assigned_user_id"}, {"value", "f38e2557-d7f2-6ce2-ea05-528e97fd1519"}},
			{{"name", "first_name"}, {"value", contato.getName()}},
			{{"name", "title"}, {"value", contato.getCargo()}},
			{{"name", "department"}, {"value", contato.getDepto()}},
			{{"name", "primary_address_street"}, {"value", contato.getStreet()}},
			{{"name", "primary_address_city"}, {"value", contato.getCity()}},
			{{"name", "primary_address_state"}, {"value", contato.getState()}},
			{{"name", "phone_mobile"}, {"value", contato.getPhone()}}
		};
		String value_list = StringUtil.toRestData(name_value_list);
		
		String parameters[][] = {
				{"session", session}, 
				{"module_name", "Contacts"},
				{"name_value_list", value_list}
		};
		// Chamando o método web
		try {
			String result = sc.call("set_entry", parameters);
			Log.i("info", "Contato.salvar - result = " + result);
			if (result == null) {
				success = 0;
			}			
		} catch (Exception e) {
			Log.e("Info", "Erro ao Salvar Contatos. Motivo: " + e.getMessage());
		}
		return success;
	}
		
	public long salvarLocal(Contato contato){
		long id = contato.getIdLocal();
		
		if(id == 0){
			id = inserirLocal(contato);
		} else{
			id = atualizarLocal(contato);
		}		
		return id;
	}

	/**
	 * Insere uma entidade no banco de dados.
	 * 
	 * SQL: inser into contato values...
	 * 
	 * @param contato
	 * @return ID
	 */
	public long inserirLocal(Contato contato){
		ContentValues values = new ContentValues();
		
		values.put("first_name", contato.getName());
		values.put("last_name", contato.getLastName());
		values.put("title", contato.getCargo());
		values.put("department", contato.getDepto());
		values.put("primary_address_street", contato.getStreet());
		values.put("primary_address_city", contato.getCity());
		values.put("primary_address_state", contato.getState());
		values.put("phone_mobile", contato.getPhone());
		
		long id = db.insert(TABELA, null, values);
		Log.i(LOG_BD, "Salvou o registro [" + id + "].");		
		return id;
	}
	
	/**
	 * Atualiza a entidade no banco. o id da entidade eh utilizado.
	 * 
	 * SQL: update contato set nome = ?, telefone = ?, ... where id_local = idlocal; 
	 * 
	 * @param 
	 * @return cont
	 */
	public int atualizarLocal(Contato contato){
		ContentValues values = new ContentValues();
		
		values.put("id", contato.getId());
		values.put("first_name", contato.getName());
		values.put("last_name", contato.getLastName());
		values.put("title", contato.getCargo());
		values.put("department", contato.getDepto());
		values.put("primary_address_street", contato.getStreet());
		values.put("primary_address_city", contato.getCity());
		values.put("primary_address_state", contato.getState());
		values.put("phone_mobile", contato.getPhone());		
		
		String where = "id_local=?";
		String[] whereArgs = new String[] { contato.getIdLocal().toString() };

		int cont = db.update(TABELA, values, where, whereArgs);
		Log.i(LOG_BD, "Atualizou [" + cont + "] registros.");				
		return cont;
	}
	
	/**
	 * Deleta a entidade com o id fornecido.
	 * 
	 * SQL: delete from contato where id = ID;
	 * 
	 * @param id
	 * @return
	 */
	public int deletarLocal(long idLocal) {
		String where = "id_local=?";
		String[] whereArgs = new String[] { String.valueOf(idLocal) };

		int cont = db.delete(TABELA, where, whereArgs);
		Log.i(LOG_BD, "Deletou [" + cont + "] registros.");				
		return cont;
	}
	
	public Contato buscar(String nome) {
		File [] files = Constantes.DIR_MVENDAS.listFiles();
		
		if(files.length > 0){
			for (File file : files) {
				//List<String> conteudo = FileUtils.lerTxt(file);
				String conteudo = file.toString();
				if(conteudo != null){
					Contato contato = new Contato(conteudo);
					
					if(contato.getName().equalsIgnoreCase(nome)){						
						//File arquivo = new File(Constantes.DIR_IMAGENS, aluno.getId() + ".jpg");
						//aluno.setFoto(FileUtils.lerImagemBitmap(arquivo));
						
						return contato;
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Faz uma consulta pelo idLocal do contato
	 * select * from contato where id_local = idLocal;
	 * @param id
	 * 
	 * @return Contato
	 */
	public Contato buscarLocal(long idLocal){
		Cursor c = db.query(TABELA, fields, "id_local = " + idLocal, null, null, null, null);
		
		Contato contato = null;
		
		if(c.getCount() > 0){
			contato = new Contato();
			c.moveToFirst();
			
			contato.setIdLocal(c.getLong(c.getColumnIndex("id_local")));
			contato.setId(c.getString(c.getColumnIndex("id")));
			contato.setName(c.getString(c.getColumnIndex("first_name")));
			contato.setLastName(c.getString(c.getColumnIndex("last_name")));
			contato.setCargo(c.getString(c.getColumnIndex("title")));
			contato.setDepto(c.getString(c.getColumnIndex("department")));					
			contato.setStreet(c.getString(c.getColumnIndex("primary_address_street")));
			contato.setCity(c.getString(c.getColumnIndex("primary_address_city")));
			contato.setState(c.getString(c.getColumnIndex("primary_address_state")));
			contato.setPhone(c.getString(c.getColumnIndex("phone_mobile")));
			//contato.setEmail(c.getString(c.getColumnIndex("email")));
			
			c.close();
		}
				
		return contato;
	}
	
	/**
	 * Faz uma consulta pelo nome do contato
	 * select * from contato where name = name;
	 * @param name
	 * 
	 * @return Contato
	 */
	public Contato buscarLocal(String name) {
		Cursor c = db.query(TABELA, fields, "first_name = ?", new String[]{name}, null, null, null);
		
		Contato contato = null;
		
		if(c.getCount() > 0){
			contato = new Contato();
			c.moveToFirst();
			
			contato.setIdLocal(c.getLong(c.getColumnIndex("id_local")));
			contato.setId(c.getString(c.getColumnIndex("id")));
			contato.setName(c.getString(c.getColumnIndex("first_name")));
			contato.setLastName(c.getString(c.getColumnIndex("last_name")));
			contato.setCargo(c.getString(c.getColumnIndex("title")));
			contato.setDepto(c.getString(c.getColumnIndex("department")));					
			contato.setStreet(c.getString(c.getColumnIndex("primary_address_street")));
			contato.setCity(c.getString(c.getColumnIndex("primary_address_city")));
			contato.setState(c.getString(c.getColumnIndex("primary_address_state")));
			contato.setPhone(c.getString(c.getColumnIndex("phone_mobile")));
			//contato.setEmail(c.getString(c.getColumnIndex("email")));
			
			c.close();
		}
		
		return contato;
	}
	
	/**
	 * abre a conexao com o banco de dados local.
	 */
	private SQLiteDatabase abrirLocal(Context context) {		
		// Abre um conexao com o Banco de Dados
		if(db == null){
			db = context.openOrCreateDatabase(DaoFactory.NOME_BANCO, Context.MODE_PRIVATE, null);
		}		
		return db;
	}
	
	/**
	 * Fecha a conexao com o banco de dados local.
	 */
	private void fecharLocal() {
		if (db != null) {
			db.close();
		}
	}
	
	/*
	public List<Contato> listarMock() {
		List<Contato> contatos = new ArrayList<Contato>();
		String[] fields = {"name", "billing_address_street", "billing_address_city", "billing_address_state", "phone_office"};
		String[] values = {"NAGEM", "Av. Washington Soares, 100", "Fortaleza", "CE", "88448122"};
		Contato contato = new Contato(fields, values);
		contatos.add(contato);
		return contatos;
	}
	*/

}
