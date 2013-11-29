package br.com.mvendas.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import br.com.mvendas.comunication.SugarClientSingleton;
import br.com.mvendas.model.Contato;
import br.com.mvendas.utils.Constantes;

public class ContatoDao {
	
	private SugarClientSingleton sc;
	private String session;

	public ContatoDao() {	
		sc = SugarClientSingleton.getInstance();
		session = sc.getSession();		
	}

	public List<Contato> listar(String where) throws Exception {
		List<Contato> contatos = new ArrayList<Contato>();

		String fields[] = {"id", "first_name", "title", "department","primary_address_street", "primary_address_city", "primary_address_state", "phone_mobile"};
		String select_fields = sc.toArrayData(fields);
		
		// Definindo os parametros para chamar o método web
		String parameters[][] = { 
			{"session", session}, 
			{"module_name", "Contacts"},
			{"query", where},
			{"order_by", ""},
			{"offset", "0"},
			{"select_fields", select_fields}, 
			{"link_name_to_fields_array", "[]"}, 
			{"max_results", "3"},
			{"deleted", "0"},
			{"Favorites", "false"}
		};
					
		// Chamando o método web
		String result = sc.call("get_entry_list", parameters);

		// Tratando o retorno para devolver apenas a lista
		if (result.equals("Sugar Indisponivel.")) {
			throw new Exception("Sugar Indisponivel.");
		} else if (result.equals("")) {
			throw new Exception("Não Há Registros.");			
		} else {
			if (result.length() < 10) {
				throw new Exception(result);			
				//return null;
			} else {
				String [] split = result.split("---");
				for (int i = 0; i < split.length; i++) {
					Contato contato = new Contato(split[i]);
					contatos.add(contato);				
				}				
			}
		}
		return contatos;
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

	public void salvar(Contato contato) {
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
		String value_list = sc.toRestData(name_value_list);
		
		String parameters[][] = {
				{"session", session}, 
				{"module_name", "Accounts"},
				{"name_value_list", value_list}
		};
		// Chamando o método web
		String result = sc.call("set_entry", parameters);
		Log.i("info", "Contato.salvar - result = " + result);
		//return result;
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

}
