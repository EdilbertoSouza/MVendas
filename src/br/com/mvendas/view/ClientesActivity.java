package br.com.mvendas.view;

import br.com.example.mvendas.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ClientesActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clientes);
	}

	public void listarClientesClick(View v) {
		Intent i = new Intent(this, ListarClientesActivity.class);
		startActivity(i);		
	}

	public void buscarClienteClick(View v) {
		Intent i = new Intent(this, BuscarClienteActivity.class);
		startActivity(i);		
	}

	public void voltarClick(View v) {
		super.onDestroy();
	}

}
