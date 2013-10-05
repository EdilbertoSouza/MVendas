package br.com.example.mvendas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import br.com.comunication.mvendas.Equipamento;
import br.com.comunication.mvendas.RestClient;
import br.com.comunication.mvendas.SoapClient;
import br.com.comunication.mvendas.SugarClient;

public class ListarClientesActivity extends Activity implements Runnable {
	
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clientes_listar);
				
		dialog = ProgressDialog.show(ListarClientesActivity.this, "", "Listando Clientes...", true); 
		new Thread(this).start();		
	}
	
	// Método da classe Runnable, executa ao iniciar a classe.	
	public void run() { testarWS();	}

	public void testarWS() {
    	
		testarClientSugar();
		//testarClientRest();    	    	
		//testarClientSoap();		
    	dialog.dismiss();
	}
	
	private void testarClientSugar() {
		// Criando um objeto do tipo SugarClient
		SugarClient sc = new SugarClient("http://10.0.2.2/sugardev/service/v2/rest.php");
		String sitio = "FS001"; 
		try {
			// Entrando no sistema
			String session = sc.login("fotomonitor", "fotomon");			
			Log.i("info", session);
			// Criando um objeto do tipo Equipamento
			Equipamento equipamento = new Equipamento(sc);
			// Recuperando o id e status atual
			String equipamento_id = equipamento.recuperarId(sitio);
			String status         = equipamento.recuperarStatus(equipamento_id);
			// Verificando o status para poder alternar entre ativo e inativo
			String novo_status;
			if (status == "ativo") {
			      novo_status = "Inativo";
			} else {
			      novo_status = "ativo";
			}
			// Atualizando o status do equipamento
			equipamento.atualizarStatus(equipamento_id, novo_status);
			// Exibindo id, sitio, status anterior e atual
			Log.i("Info", "ID: " + equipamento_id);
			Log.i("Info", "Sitio: " + sitio);
			//Log.i("Info", "Status Anterior: " + status);
			//Log.i("Info", "Status Atual...: " + novo_status);
			// Recuperando o registro pelo id
			//registro = equipamento.recuperarRegistro(equipamento_id);
			// Exibindo id, sitio, status anterior e atual
			//Log.i("Info", "Endereco: " + registro.endereco.value);
			// Saindo do sistema
			//sc.logout();
			
		} catch (Exception e) {
			Log.e("Erro", "testarClienteSugar com erro");
			e.getMessage();
			//e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void testarClientRest() {
		// Array de String que recebe o JSON do Web Service
		String[] jsonResult = new RestClient().get("http://www.cheesejedi.com/rest_services/get_big_cheese?level=1");
		
		Log.i("JSON", "Result: " + jsonResult[0] + " : " + jsonResult[1]);	        
	}

	@SuppressWarnings("unused")
	private void testarClientSoap() {
		Log.i("teste", "Iniciando o uso do KSOAP ...");    	
		
		SoapClient soap = new SoapClient();
		soap.listarClientes();
				
    	Log.i("teste", "Finalizando o uso do KSOAP");
	}

}
