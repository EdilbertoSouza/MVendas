package br.com.mvendas.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.mvendas.comunication.SugarClientSingleton;
import br.com.mvendas.model.Cliente;
import br.com.mvendas.utils.Constantes;
import br.com.mvendas.utils.StringUtil;

public class ClienteDao {
	
	private SugarClientSingleton sc;
	private String session;
	private static SQLiteDatabase db = null;
	private static final String LOG_BD = "bd";	
	protected static String NOME_BANCO = "mvendas"; 
	protected static String TABELA = "cliente";
	protected static String fields[] = {
		"id_local",
		"id", 
		"name", 
		"billing_address_street", 
		"billing_address_city", 
		"billing_address_state", 
		"phone_office", 
		"website"
	};

	public ClienteDao(Context context) {	
		sc = SugarClientSingleton.getInstance();
		session = sc.getSession();
		// Abre um conexao com o Banco de Dados
		if(db == null){
			db = context.openOrCreateDatabase(DaoFactory.NOME_BANCO, Context.MODE_PRIVATE, null);
		}
	}

	public List<Cliente> listar(String where) throws Exception {
		List<Cliente> clientes = new ArrayList<Cliente>();

		String select_fields = StringUtil.toArrayData(fields);
		
		// Definindo os parametros para chamar o método web
		String parameters[][] = { 
			{"session", session}, 
			{"module_name", "Accounts"},
			{"query", where},
			{"order_by", ""},
			{"offset", "0"},
			{"select_fields", select_fields}, 
			{"link_name_to_fields_array", "[]"}, 
			{"max_results", "10"},
			{"deleted", "0"},
			{"Favorites", "false"}
		};
					
		// Chamando o método web
		String result = sc.call("get_entry_list", parameters);

		// Tratando o retorno para devolver apenas a lista
		try {
			if (result.equals("Sugar Indisponivel.")) {
				throw new Exception("Sugar Indisponivel.");
			} else if (result.equals("")) {
				throw new Exception("Não Há Registros.");			
			} else {
				if (result.length() < 10) {
					throw new Exception(result);			
				} else {
					String [] split = result.split("---");
					for (int i = 0; i < split.length; i++) {
						Cliente cliente = new Cliente(split[i]);
						clientes.add(cliente);				
					}				
				}
			}			
		} catch (Exception e) {
			throw e;
		}
		return clientes;
	}

	public void salvar(Cliente cliente) {
		// Definindo os parametros para chamar o método web
		String name_value_list[][][] = {
			{{"name", "id"}, {"value", cliente.getId()}},
			{{"name", "account_type"}, {"value", "Other"}},
			{{"name", "assigned_user_id"}, {"value", "f38e2557-d7f2-6ce2-ea05-528e97fd1519"}},
			{{"name", "name"}, {"value", cliente.getName()}},
			{{"name", "billing_address_street"}, {"value", cliente.getStreet()}},
			{{"name", "billing_address_city"}, {"value", cliente.getCity()}},
			{{"name", "billing_address_state"}, {"value", cliente.getState()}},
			{{"name", "phone_office"}, {"value", cliente.getPhone()}},
			{{"name", "website"}, {"value", cliente.getWebsite()}}				
		};
		String value_list = StringUtil.toRestData(name_value_list);
		
		String parameters[][] = {
				{"session", session}, 
				{"module_name", "Accounts"},
				{"name_value_list", value_list}
		};
		// Chamando o método web
		String result = sc.call("set_entry", parameters);
		Log.i("info", "Cliente.salvar - result = " + result);
		//return result;
	}

	public Cliente buscar(String nome) {
		File [] files = Constantes.DIR_MVENDAS.listFiles();
		
		if(files.length > 0){
			for (File file : files) {
				//List<String> conteudo = FileUtils.lerTxt(file);
				String conteudo = file.toString();
				if(conteudo != null){
					Cliente cliente = new Cliente(conteudo);
					
					if(cliente.getName().equalsIgnoreCase(nome)){						
						//File arquivo = new File(Constantes.DIR_IMAGENS, cliente.getId() + ".jpg");
						//cliente.setFoto(FileUtils.lerImagemBitmap(arquivo));
						
						return cliente;
					}
				}
			}
		}
		
		return null;
	}
	
	public long salvarBD(Cliente cliente){
		long id = cliente.getIdLocal();
		
		if(id == 0){
			id = inserirBD(cliente);
		} else{
			id = atualizarBD(cliente);
		}
		
		return id;
	}

	/**
	 * Insere uma entidade no banco de dados.
	 * 
	 * SQL: inser into cliente values...
	 * 
	 * @param cliente
	 * @return ID
	 */
	public long inserirBD(Cliente cliente){
		ContentValues values = new ContentValues();

		values.put("name", cliente.getName());
		values.put("billing_address_street", cliente.getStreet());
		values.put("billing_address_city", cliente.getCity());
		values.put("billing_address_state", cliente.getState());
		values.put("phone_office", cliente.getPhone());
		values.put("website", cliente.getWebsite());
		
		long id = inserirBD(values);
		return id;
	}

	private long inserirBD(ContentValues values) {
		long id = db.insert(TABELA, null, values);
		Log.i(LOG_BD, "Salvou o registro [" + id + "].");		
		return id;
	}

	/**
	 * Atualiza a entidade no banco. o id da entidade eh utilizado.
	 * 
	 * SQL: update cliente set nome = ?, telefone = ?, ... where id_local = idlocal; 
	 * 
	 * @param 
	 * @return cont
	 */
	public int atualizarBD(Cliente cliente){
		ContentValues values = new ContentValues();
		
		values.put("id", cliente.getId());
		values.put("name", cliente.getName());
		values.put("billing_address_street", cliente.getStreet());
		values.put("billing_address_city", cliente.getCity());
		values.put("billing_address_state", cliente.getState());
		values.put("phone_office", cliente.getPhone());
		values.put("website", cliente.getWebsite());
		
		String where = "id_local=?";
		String[] whereArgs = new String[] { cliente.getIdLocal().toString() };

		int cont = atualizarBD(values, where, whereArgs);
		return cont;
	}

	/**
	 * Atualiza a entidade com os valores abaixo
	 * 
	 * @param values
	 * @param where
	 * @param whereArgs
	 * @return
	 */
	private int atualizarBD(ContentValues values, String where, String[] whereArgs) {
		int cont = db.update(TABELA, values, where, whereArgs);
		Log.i(LOG_BD, "Atualizou [" + cont + "] registros.");				
		return cont;
	}
	
	/**
	 * Deleta a entidade com o id fornecido.
	 * 
	 * SQL: delete from cliente where id = ID;
	 * 
	 * @param id
	 * @return
	 */
	public int deletarBD(long idLocal) {
		String where = "id_local=?";
		String[] whereArgs = new String[] { String.valueOf(idLocal) };

		int cont = deletarBD(where, whereArgs);
		return cont;
	}

	/**
	 * Deleta a entidade com os argumentos fornecidos.
	 * 
	 * @param where
	 * @param whereArgs
	 * @return
	 */
	private int deletarBD(String where, String[] whereArgs) {
		int cont = db.delete(TABELA, where, whereArgs);
		Log.i(LOG_BD, "Deletou [" + cont + "] registros.");				
		return cont;
	}
	
	/**
	 * Lista todos os clientes do banco
	 * select * from cliente
	 * 
	 * @return List<Cliente>
	 * 
	 */
	public List<Cliente> listarBD(){
		return listarBD(getCursor());
	}
	
	private List<Cliente> listarBD(Cursor c){
		List<Cliente> clientes = null;
		Cliente cliente = null;
		
		if(c != null){
			
			if (c.getCount() < 1) inserirMock();
			
			clientes = new ArrayList<Cliente>();
			
			if(c.moveToFirst()){
				do{
					cliente = new Cliente();
					
					cliente.setIdLocal(c.getLong(c.getColumnIndex("id_local")));
					cliente.setId(c.getString(c.getColumnIndex("id")));
					cliente.setName(c.getString(c.getColumnIndex("name")));
					cliente.setPhone(c.getString(c.getColumnIndex("phone_office")));
					cliente.setStreet(c.getString(c.getColumnIndex("billing_address_street")));
					cliente.setCity(c.getString(c.getColumnIndex("billing_address_city")));
					cliente.setState(c.getString(c.getColumnIndex("billing_address_state")));
					cliente.setWebsite(c.getString(c.getColumnIndex("website")));
					
					clientes.add(cliente);
					
				} while(c.moveToNext());
				
				c.close();
			}
		} 
		
		return clientes;
	}

	/**
	 * Retorna um cursor com todos os clientes.
	 * select * from cliente
	 * @return cursor
	 */
	public Cursor getCursor() {
		try {
			return db.query(TABELA, fields,	null, null, null, null, null, null);
		} catch (Exception e) {
			Log.e(LOG_BD, "Erro ao buscar clientes: " + e.getMessage(), e);
			fechar();
			return null;
		}		
	}
	
	/**
	 * Faz uma consulta pelo idLocal do cliente
	 * select * from cliente where id_local = idLocal;
	 * @param id
	 * 
	 * @return Cliente
	 */
	public Cliente buscarBD(long idLocal){
		Cursor c = db.query(TABELA, fields, "id_local = " + idLocal, null, null, null, null);
		
		Cliente cliente = null;
		
		if(c.getCount() > 0){
			cliente = new Cliente();
			c.moveToFirst();
			
			cliente.setIdLocal(c.getLong(c.getColumnIndex("id_local")));
			cliente.setId(c.getString(c.getColumnIndex("id")));
			cliente.setName(c.getString(c.getColumnIndex("name")));
			cliente.setPhone(c.getString(c.getColumnIndex("phone_office")));
			cliente.setStreet(c.getString(c.getColumnIndex("billing_address_street")));
			cliente.setCity(c.getString(c.getColumnIndex("billing_address_city")));
			cliente.setState(c.getString(c.getColumnIndex("billing_address_state")));
			cliente.setWebsite(c.getString(c.getColumnIndex("website")));
			
			c.close();
		}
				
		return cliente;
	}
	
	/**
	 * Faz uma consulta pelo nome do cliente
	 * select * from cliente where name = name;
	 * @param name
	 * 
	 * @return Cliente
	 */
	public Cliente buscarBD(String name) {
		Cursor c = db.query(TABELA, fields, "name = ?", new String[]{name}, null, null, null);
		
		Cliente cliente = null;
		
		if(c.getCount() > 0){
			cliente = new Cliente();
			c.moveToFirst();
			
			cliente.setIdLocal(c.getLong(c.getColumnIndex("id_local")));
			cliente.setId(c.getString(c.getColumnIndex("id")));
			cliente.setName(c.getString(c.getColumnIndex("name")));
			cliente.setPhone(c.getString(c.getColumnIndex("phone_office")));
			cliente.setStreet(c.getString(c.getColumnIndex("billing_address_street")));
			cliente.setCity(c.getString(c.getColumnIndex("billing_address_city")));
			cliente.setState(c.getString(c.getColumnIndex("billing_address_state")));
			cliente.setWebsite(c.getString(c.getColumnIndex("website")));
			
			c.close();
		}
		
		return cliente;
	}
	
	
	/**
	 * Fecha a conexao com o banco.
	 */
	public void fechar() {
		if (db != null) {
			db.close();
		}
	}
	
	public List<Cliente> listarMock(String where) {
		List<Cliente> clientes = new ArrayList<Cliente>();
		Cliente cliente;
		cliente = new Cliente(1, null, "NAGEM", "88448122", "Av. Washington Soares, 100", "Fortaleza", "CE", "www.nagem.com.br");
		clientes.add(cliente);
		cliente = new Cliente(2, null, "CECOMIL", "88448122", "Av. Washington Soares, 100", "Fortaleza", "CE", "www.cecomil.com.br");
		clientes.add(cliente);
		return clientes;
	}
	
	public void inserirMock() {
		Cliente cliente;
		cliente = new Cliente(1, null, "NAGEM", "88448122", "Av. Washington Soares, 100", "Fortaleza", "CE", "www.nagem.com.br");
		inserirBD(cliente);
		cliente = new Cliente(2, null, "CECOMIL", "88448122", "Av. Washington Soares, 100", "Fortaleza", "CE", "www.cecomil.com.br");
		inserirBD(cliente);
	}

}
