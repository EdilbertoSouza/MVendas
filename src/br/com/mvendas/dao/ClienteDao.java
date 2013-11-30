package br.com.mvendas.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import br.com.mvendas.comunication.SugarClientSingleton;
import br.com.mvendas.model.Cliente;
import br.com.mvendas.utils.Constantes;
import br.com.mvendas.utils.StringUtil;

public class ClienteDao {
	
	private SugarClientSingleton sc;
	private String session;

	public ClienteDao() {	
		sc = SugarClientSingleton.getInstance();
		session = sc.getSession();		
	}

	public List<Cliente> listar(String where) throws Exception {
		List<Cliente> clientes = new ArrayList<Cliente>();

		String fields[] = {"id", "name", "billing_address_street", "billing_address_city", "billing_address_state", "phone_office", "website"};
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

	/*
	public List<Cliente> listarMock() {
		List<Cliente> clientes = new ArrayList<Cliente>();
		String[] fields = {"name", "billing_address_street", "billing_address_city", "billing_address_state", "phone_office"};
		String[] values = {"NAGEM", "Av. Washington Soares, 100", "Fortaleza", "CE", "88448122"};
		Cliente cliente = new Cliente(fields, values);
		clientes.add(cliente);
		return clientes;
	}
	*/

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
						//File arquivo = new File(Constantes.DIR_IMAGENS, aluno.getId() + ".jpg");
						//aluno.setFoto(FileUtils.lerImagemBitmap(arquivo));
						
						return cliente;
					}
				}
			}
		}
		
		return null;
	}

}
