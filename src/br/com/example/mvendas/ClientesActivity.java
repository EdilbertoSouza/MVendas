package br.com.example.mvendas;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ClientesActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clientes);
	}
	
	public void listarClientesClick() {
		WebServiceClient wsc = new WebServiceClient();
		List clientes = wsc.buscarClientes();
		
		for (Iterator iterator = clientes.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();			
			Log.i("teste", "Cliente: " + object.toString());
		}
		
	}

}
