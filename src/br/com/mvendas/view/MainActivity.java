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
import br.com.mvendas.dao.DaoFactory;
import br.com.mvendas.utils.Constantes;

public class MainActivity extends Activity {
	
	SugarClientSingleton sc;
	ProgressDialog dialog;
	DaoFactory dao;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btClientes = (Button) findViewById(R.id.btClientes);
		btClientes.setOnClickListener(btClientesOnClickListener);

		Button btContatos = (Button) findViewById(R.id.btContatos);
		btContatos.setOnClickListener(btContatosOnClickListener);

		Button btEquipamentos = (Button) findViewById(R.id.btEquipamentos);
		btEquipamentos.setOnClickListener(btEquipamentosOnClickListener);

		Button btOrcamentos = (Button) findViewById(R.id.btOrcamentos);
		btOrcamentos.setOnClickListener(btOrcamentosOnClickListener);
		
		Conectar();
	}
	
	@Override
	protected void onDestroy() {
        sc.logout();
        sc = null;
        dao.close();
		super.onDestroy();
	}
	
	private void Conectar() {
		dialog = ProgressDialog.show(MainActivity.this, "", "Conectando ao SugarCRM...", true);
		try{
			new Thread(new Runnable() {				
				@Override
				public void run() {
					try {
				        // Cria os diretórios
						if(!Constantes.DIR_MVENDAS.exists()){
							Constantes.DIR_MVENDAS.mkdir();
						}
						if(!Constantes.DIR_BASE.exists()){
							Constantes.DIR_BASE.mkdir();
						}
				        // Cria o repositorio e as instancias de controle
				        dao = new DaoFactory(getApplicationContext());
						sc = SugarClientSingleton.getInstance();
						sc.login("mvendas", "srlke58x");
					} catch (Exception e) {
						Log.e("info", "run - login " + e.getMessage());
					} finally{
						dialog.dismiss();
					}
				}
			}).start();
		} catch(Throwable e){
			Log.e("info", e.getMessage(), e);
		}	
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
		
	private OnClickListener btContatosOnClickListener = new OnClickListener() {
	    @Override
	    public void onClick(View v) {
	    	contatosClick(v);
	    }
	};

	public void contatosClick(View v) {
		Intent i = new Intent(this, ContatosActivity.class);
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
