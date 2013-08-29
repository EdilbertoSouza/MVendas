package br.com.example.mvendas;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ListarClientesActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clientes_listar);
		testarWS();
	}
	
	public void testarWS() {
    	Log.i("teste", "Iniciando o uso do KSOAP");
    	
		WebServiceClient wsc = new WebServiceClient();
		List clientes = wsc.buscarClientes();
		for (int i=0; i < clientes.size(); i++) {
			Log.i("teste", "Cliente: " + clientes.get(i));
		}
		
    	Log.i("teste", "Iniciando o uso do KSOAP");		
	}

}
