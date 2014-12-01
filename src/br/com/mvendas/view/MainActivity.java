package br.com.mvendas.view;

import java.util.List;

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
import br.com.mvendas.dao.ClienteDao;
import br.com.mvendas.dao.ContatoDao;
import br.com.mvendas.dao.DaoFactory;
import br.com.mvendas.model.Cliente;
import br.com.mvendas.model.Contato;
import br.com.mvendas.utils.Constantes;

public class MainActivity extends Activity {
	
	SugarClientSingleton sc;
	String session;
	ProgressDialog progress;
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

		Button btSincronizar = (Button) findViewById(R.id.btSincronizar);
		btSincronizar.setOnClickListener(btSincronizarOnClickListener);

		Conectar();
	}
	
	@Override
	protected void onDestroy() {
        sc.close();
        dao.close();
		Log.i("info", "MVendas finalizado");
		super.onDestroy();
	}
	
	private void Conectar() {
		progress = ProgressDialog.show(MainActivity.this, "SQLite", "Conectando ao Banco de Dados...", true);
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
						//sc.login("mvendas", "srlke58x");
						Log.i("info", "MVendas iniciado");						
					} catch (Exception e) {
						Log.e("info", "login " + e.getMessage());
					} finally{
						progress.dismiss();
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

	private OnClickListener btSincronizarOnClickListener = new OnClickListener() {
	    @Override
	    public void onClick(View v) {
	    	sincronizarClick(v);
	    }
	};

	public void sincronizarClick(View v) {		
		progress = ProgressDialog.show(this, "Atenção", "Conectando...", false, true);
    	
		try{
			new Thread(new Runnable() {
				@Override
				public void run() {
					String resp = "";					
					try {
						sc.login("mvendas", "srlke58x");
						
						AtualizarMensagem("Baixando Clientes...");
						ClienteDao clienteDao = new ClienteDao(getApplicationContext());
						List<Cliente> clientes = clienteDao.listarRemoto("Accounts.assigned_user_id='f38e2557-d7f2-6ce2-ea05-528e97fd1519'");
						for (int i = 0; i < clientes.size(); i++) {
							Cliente cliente = clientes.get(i);
							Cliente clienteExistente = clienteDao.buscarLocal(cliente.getName());
							if (clienteExistente != null) cliente.setIdLocal(clienteExistente.getIdLocal());								
							clienteDao.salvarLocal(cliente);								
						}
						
						AtualizarMensagem("Baixando Contatos...");
						ContatoDao contatoDao = new ContatoDao(getApplicationContext());
						List<Contato> contatos = contatoDao.listarRemoto("Contacts.assigned_user_id='f38e2557-d7f2-6ce2-ea05-528e97fd1519'");
						for (int i = 0; i < contatos.size(); i++) {
							Contato contato = contatos.get(i);
							Contato contatoExistente = contatoDao.buscarLocal(contato.getName());
							if (contatoExistente != null) contato.setIdLocal(contatoExistente.getIdLocal());
							contatoDao.salvarLocal(contato);
						}					

						AtualizarMensagem("Desconectando...");
						sc.logout();
					} catch (Exception e) {						
						Log.e("info", e.getMessage(), e);
						sc.close();
					} finally{
						Log.i("info", ""+resp);
						sc.close();
						progress.dismiss();
					}
				}
			}).start();
		} catch(Throwable e){
			Log.e("info", e.getMessage(), e);
		}
	}
	
	public void AtualizarMensagem(final String mensagem) {
		runOnUiThread ( new Runnable () {
			@Override
			public void run () {
				progress.setMessage(mensagem);
			}
		});		
	}

}
