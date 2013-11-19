package br.com.mvendas.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import br.com.example.mvendas.R;
import br.com.mvendas.comunication.SugarClientSingleton;


public class MainActivity extends Activity implements Runnable {
	
	SugarClientSingleton sc;
	ProgressDialog dialog;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		setContentView(R.layout.activity_main);

		Button btClientes = (Button) findViewById(R.id.btClientes);
		btClientes.setOnClickListener(btClientesOnClickListener);

		Button btEquipamentos = (Button) findViewById(R.id.btEquipamentos);
		btEquipamentos.setOnClickListener(btEquipamentosOnClickListener);

		Button btOrcamentos = (Button) findViewById(R.id.btOrcamentos);
		btOrcamentos.setOnClickListener(btOrcamentosOnClickListener);
		
		dialog = ProgressDialog.show(MainActivity.this, "", "Conectando ao SugarCRM", true);
		new Thread(this).start();
	}
		
	@Override
	public void run() {
		try {
			sc = SugarClientSingleton.getInstance();
			sc.login("fotomonitor", "fotomon");
		} catch (Exception e) {
			Log.e("info", "Erro ao efetuar Login");
			Log.e("info", e.toString());
			//Toast.makeText(MainActivity.this, "SugarCRM Não Conectado", Toast.LENGTH_LONG).show();
		}		
		dialog.dismiss();
	}

	@Override
	protected void onDestroy() {
        sc.logout();
        sc = null;
		//Toast.makeText(MainActivity.this, "Logout Efetuado", Toast.LENGTH_LONG).show();
		super.onDestroy();
	}

	private OnClickListener btClientesOnClickListener = new OnClickListener() {
	    @Override
	    public void onClick(View v) {
	    	clientesClick(v);
	    }
	};

	public void clientesClick(View v) {
		Intent i = new Intent(this, ClientesActivity.class);
		startActivity(i);		
	}
		
	private OnClickListener btEquipamentosOnClickListener = new OnClickListener() {
	    @Override
	    public void onClick(View v) {
	    	equipamentosClick(v);
	    }
	};
	
	public void equipamentosClick(View v) {
		Intent i = new Intent(this, EquipamentosActivity.class);
		startActivity(i);
	}

	private OnClickListener btOrcamentosOnClickListener = new OnClickListener() {
	    @Override
	    public void onClick(View v) {
	    	orcamentosClick(v);
	    }
	};

	public void orcamentosClick(View v) {
		//Intent i = new Intent(this, OrcamentosActivity.class);
		//startActivity(i);		
        Toast.makeText(MainActivity.this, "Rotina a Implementar...", Toast.LENGTH_LONG).show();
	}
	
}
