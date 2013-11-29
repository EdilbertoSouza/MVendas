package br.com.mvendas.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import br.com.mvendas.comunication.SugarClientSingleton;
import br.com.mvendas.model.Equipamento;

public class EquipamentoDao {
	
	private SugarClientSingleton sc;
	private String session;

	public EquipamentoDao() {	
		sc = SugarClientSingleton.getInstance();
		session = sc.getSession();		
	}

	public String recuperarId(String sitio) {
		String equipamento_id = "";
		
		// Definindo os parametros para chamar o método web
		String fields[] = {"id"};
		String select_fields = sc.toArrayData(fields);
		String parameters[][] = { 
			{"session", session}, 
			{"module_name", "os_Equipamentos"},
			{"query", "name='" + sitio + "'"},
			{"order_by", ""},
			{"offset", "0"},
			{"select_fields", select_fields}, 
			{"link_name_to_fields_array", "[]"}, 
			{"max_results", "1"},
			{"deleted", "0"},
			{"Favorites", "false"}
		};
					
		// Chamando o método web
		String result = sc.call("get_entry_list", parameters);
		
		// Tratando o retorno para devolver apenas o id
		try {
			JSONObject jsonResult  		= (JSONObject) new JSONTokener(result).nextValue();
			String entryList      		= jsonResult.getJSONArray("entry_list").get(0).toString();
			JSONObject jsonEntryList	= (JSONObject) new JSONTokener(entryList).nextValue();
			JSONObject jsonNameValueList= jsonEntryList.getJSONObject("name_value_list");
			JSONObject jsonId          	= jsonNameValueList.getJSONObject("id");
			equipamento_id 		   		= jsonId.getString("value").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return equipamento_id;
	}

	public String recuperarStatus(String id) throws JSONException {
		String status = "";
		
		// Definindo os parametros para chamar o método web
		String fields[] = {"status"};
		String select_fields = this.sc.toArrayData(fields);
		String parameters[][] = {
			{"session", this.session}, 
			{"module_name", "os_Equipamentos"},
			{"query", "os_equipamentos.id = '" + id + "'"},
			{"order_by", ""},
			{"offset", "0"},
			{"select_fields", select_fields}, 
			{"link_name_to_fields_array", "[]"}, 
			{"max_results", "1"},
			{"deleted", "0"},
			{"Favorites", "false"}
		};

		// Chamando o método web
		String result = sc.call("get_entry_list", parameters);

		// Tratando o retorno para devolver apenas o id
		try {
			JSONObject jsonResult  		= (JSONObject) new JSONTokener(result).nextValue();
			String entryList      		= jsonResult.getJSONArray("entry_list").get(0).toString();
			JSONObject jsonEntryList	= (JSONObject) new JSONTokener(entryList).nextValue();
			JSONObject jsonNameValueList= jsonEntryList.getJSONObject("name_value_list");
			JSONObject jsonStatus 		= jsonNameValueList.getJSONObject("status");
			status		 		   		= jsonStatus.getString("value").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
				
		return status;
	}

	public String atualizarStatus(String id, String status)	{
		// Definindo os parametros para chamar o método web
		String name_value_list[][][] = {
			{{"name", "id"}, {"value", id}},
			{{"name", "status"}, {"value", status}}				
		};
		String value_list = sc.toRestData(name_value_list);
		
		String parameters[][] = {
				{"session", session}, 
				{"module_name", "os_Equipamentos"},
				{"name_value_list", value_list}
		};
		// Chamando o método web
		String result = sc.call("set_entry", parameters);
		return result;
	}

	public List<Equipamento> listar(String where) {
		List<Equipamento> equipamentos = new ArrayList<Equipamento>();

		String fields[] = {"name", "status", "endereco"};
		String select_fields = sc.toArrayData(fields);
		
		// Definindo os parametros para chamar o método web
		String parameters[][] = { 
			{"session", session}, 
			{"module_name", "os_Equipamentos"},
			{"query", where},
			{"order_by", ""},
			{"offset", "0"},
			{"select_fields", select_fields}, 
			{"link_name_to_fields_array", "[]"}, 
			{"max_results", "20"},
			{"deleted", "0"},
			{"Favorites", "false"}
		};
					
		// Chamando o método web
		String result = sc.call("get_entry_list", parameters);

		// Tratando o retorno para devolver a lista
		String [] split = result.split("---");
		for (int i = 0; i < split.length; i++) {
			Equipamento equipamento = new Equipamento(split[i]);
			equipamentos.add(equipamento);			
		}

		return equipamentos;
	}
	
}
