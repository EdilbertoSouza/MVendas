package br.com.mvendas.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;
import br.com.mvendas.comunication.SugarClientSingleton;
import br.com.mvendas.model.Equipamento;

public class EquipamentoDao {
	
	private SugarClientSingleton sc;
	private String session;

	public EquipamentoDao() {	
		sc = SugarClientSingleton.getInstance();
		session = sc.getSessionId();		
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
			{"max_results", "10"},
			{"deleted", "0"},
			{"Favorites", "false"}
		};
					
		// Chamando o método web
		String result = sc.call("get_entry_list", parameters);

		// Tratando o retorno para devolver apenas a lista
		String split[];
		int result_count = 0;
		String entry_list;
		String name_value_list, name_value_field, value;
		String resultFields[];
		try {
			split = result.split(",", 3);
			result_count = Integer.parseInt(split[0].split(":")[1]);
			entry_list = split[2];
			entry_list = entry_list.substring(14);			
			for (int i = 1; i < result_count + 1; i++) {
				name_value_list = entry_list.split("name_value_list")[i].substring(2);
				name_value_list = name_value_list.split("\\}\\}\\}")[0];	

				List<String> values = new ArrayList<String>();				
				for (int j = 0; j < 3; j++) {
					name_value_field = name_value_list.split("\\},", 3)[j] + "}";
					name_value_field = name_value_field.substring(iif(j==0,1,0));
					name_value_field = name_value_field.split("\\}")[0];
					resultFields = name_value_field.split("\\:");
					value = resultFields[3].replace(Character.toString((char) 34), "");
					values.add(value);				
				}
				Equipamento equipamento = new Equipamento(fields, values);
				equipamentos.add(equipamento);				
			}
		} catch (Exception e) {
			Log.e("info", "Erro ao tratar retorno");
			Log.e("info", e.toString());
		}

		return equipamentos;
	}

	private static int iif(Boolean arg, int seVerdadeiro, int seFalso) {
		if (arg) {
			return seVerdadeiro;
		} else {
			return seFalso;
		}
	}

}
