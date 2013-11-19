package br.com.mvendas.view;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.example.mvendas.R;
import br.com.mvendas.dao.ClienteDao;
import br.com.mvendas.model.Cliente;

import com.google.inject.Inject;

@ContentView(R.layout.activity_clientes_form)
public class ClientesFormActivity extends RoboActivity implements OnClickListener {

	public static final String SAVE_STATE = "state_cliente";
	public static final String INTENT_EXTRA_DATA_ID = "_id_";
	public static final String INTENT_EXTRA_DATA_CLIENTE = "_cliente_";
	
	private static final int INTENT_RESULT_DATA_CAMERA = 101;	
	

	@Inject
	private ClienteDao clienteDao;
	
	@InjectView(R.id.tvId)
	private TextView tvId;
	
	@InjectView(R.id.etNome)
	private EditText etNome;
	
	@InjectView(R.id.etTelefone)
	private EditText etTelefone;
	
	@InjectView(R.id.etEndereco)
	private EditText etEndereco;
	
	@InjectView(R.id.etCidade)
	private EditText etCidade;
	
	@InjectView(R.id.etEstado)
	private EditText etEstado;
	
	@InjectView(R.id.btSalvar)
	private Button btSalvar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		// Habilita o Click sobre o nome do cliente
		//etNome.setOnClickListener(this);
		//etNome.setOnLongClickListener(this);
		
		// Habilita o Click sobre botao Salvar 
		btSalvar.setOnClickListener(this);
		
		// Obtem os dados da Activity que a chamou
		Intent it = getIntent();
		Cliente cliente = it.getParcelableExtra(INTENT_EXTRA_DATA_CLIENTE);
		setCliente(cliente);
	}

	/**
	 * Configura o evento de click dos botoes
	 */
	@Override
	public void onClick(View v) {
		if(v == btSalvar){
			Cliente cliente = getCliente();
			clienteDao.salvar(cliente);
			
			Toast.makeText(this,
					cliente.getName() + " salvo com sucesso!!!", 
					Toast.LENGTH_SHORT)
					.show();			
			finish();
			
		//} else if(v == ivContato){
			//Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
			//startActivityForResult(it, INTENT_RESULT_DATA_CAMERA);
		}
	}

	/*
	@Override
	public boolean onLongClick(View v) {
		if(v == ivContato){
			Bitmap photo = ((BitmapDrawable) ivContato.getDrawable()).getBitmap();
			Bitmap newPhoto = ImageManipulation.rotate(photo, -90);
			
			ivContato.setImageBitmap(newPhoto);
		}
		
		return true;
	}
	*/
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {    	
		switch (requestCode) {
			case INTENT_RESULT_DATA_CAMERA:
				if(data != null){
					Bundle bundle = data.getExtras();
					
					if(bundle != null){
						/*
						// Recupera o bitmap retornado pela camera
						Bitmap bitmap = (Bitmap) bundle.get("data");

						// Redimensiona a imagem						
						int newH = 72;
						int newW = (int) (bitmap.getWidth() / (bitmap.getWidth()/72));
						Bitmap resize = ImageManipulation.resize(bitmap, newH, newW);
						
						// Atualiza a imagem na ImageView
						ivContato.setImageBitmap(resize);
						*/
					}
				}
				
				break;
		}
	}

	/**
	 * Cria um objeto Cliente a partir dos componentes da Tela
	 * 
	 * @return cliente
	 */
	private Cliente getCliente() {
		String id = "";
		
		try {
			id = tvId.getText().toString();
		} catch (NumberFormatException e) {
			//id = Cliente.getId();
		}
		
		//Bitmap foto = ((BitmapDrawable) ivContato.getDrawable()).getBitmap();
		String nome = etNome.getText().toString();
		String telefone = etTelefone.getText().toString();
		String endereco = etEndereco.getText().toString();
		String cidade = etCidade.getText().toString();
		String estado = etEstado.getText().toString();
		
		Cliente cliente = new Cliente(id, nome, telefone, endereco, cidade, estado);
		return cliente;
	}
	
	/**
	 * Seta os campos da Tela a partir de um objeto Cliente
	 * 
	 * @param cliente
	 */
	private void setCliente(Cliente cliente) {
		if(cliente != null){
			tvId.setText(String.valueOf(cliente.getId()));
			//ivContato.setImageBitmap(aluno.getFoto());
			etNome.setText(cliente.getName());
			etTelefone.setText(cliente.getPhone());
			etEndereco.setText(cliente.getStreet());
			etCidade.setText(cliente.getCity());
			etEstado.setText(cliente.getState());
		}
	}
	
	/**
	 * Retorna o indice do cliente
	 * 
	 * @param cliente
	 * @return i
	 */
	/*
	private int getPosition(String cliente) {
		for (int i = 0; i < cliente.length; i++) {
			String item = cliente[i];
			if(cliente.equalsIgnoreCase(item)){
				return i;
			}
		}
		return 0;
	}
	*/
	
	/**
	 * Ler os dados do cliente com o id passado e preenche na tela
	 * 
	 * @param id_cliente
	 */
	/*
	@SuppressWarnings("unused")
	private void carregaDadosPeloId(long id) {
		Cliente cliente = clienteDao.buscar(id);
		
		if(cliente != null){
			setCliente(cliente);
		} else{
			Toast.makeText(this, "O cliente de ID="+id+" não foi encontrado!",
					Toast.LENGTH_LONG).show();
			finish();
		}
	}
	*/
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);		
		//outState.putParcelable(SAVE_STATE, getCliente());
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		Cliente cliente = savedInstanceState.getParcelable(SAVE_STATE);
		setCliente(cliente);
	}
}
