package br.com.mvendas.view;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.example.mvendas.R;
import br.com.mvendas.dao.ContatoDao;
import br.com.mvendas.model.Contato;
import br.com.mvendas.utils.ImageManipulation;

import com.google.inject.Inject;

@ContentView(R.layout.activity_contatos_form)
public class ContatosFormActivity extends RoboActivity implements OnClickListener, OnLongClickListener {

	public static final String SAVE_STATE = "state_contato";
	public static final String INTENT_EXTRA_DATA_ID = "_id_";
	public static final String INTENT_EXTRA_DATA_CONTATO = "_contato_";
	
	private static final int INTENT_RESULT_DATA_CAMERA = 101;	
	

	@Inject
	private ContatoDao contatoDao;
	
	@InjectView(R.id.tvId)
	private TextView tvId;

	@InjectView(R.id.ivContato)
	private ImageView ivContato;

	@InjectView(R.id.etNome)
	private EditText etNome;

	@InjectView(R.id.etSobrenome)
	private EditText etSobrenome;

	@InjectView(R.id.etCargo)
	private EditText etCargo;

	@InjectView(R.id.etDepto)
	private EditText etDepto;

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
	
		// Habilita o Click sobre a imagem do contato
		ivContato.setOnClickListener(this);
		ivContato.setOnLongClickListener(this);
		
		// Habilita o Click sobre botao Salvar 
		btSalvar.setOnClickListener(this);
		
		// Obtem os dados da Activity que a chamou
		Intent it = getIntent();
		Contato contato = it.getParcelableExtra(INTENT_EXTRA_DATA_CONTATO);
		setContato(contato);
	}

	/**
	 * Configura o evento de click dos botoes
	 */
	@Override
	public void onClick(View v) {
		if(v == btSalvar){
			Contato contato = getContato();
			contatoDao.salvar(contato);
			
			Toast.makeText(this, contato.getName() + " salvo com sucesso!!!", 
					Toast.LENGTH_SHORT).show();			
			finish();
			
		} else if(v == ivContato){
			Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
			startActivityForResult(it, INTENT_RESULT_DATA_CAMERA);
		}
	}
	
	@Override
	public boolean onLongClick(View v) {
		if(v == ivContato){
			Bitmap photo = ((BitmapDrawable) ivContato.getDrawable()).getBitmap();
			Bitmap newPhoto = ImageManipulation.rotate(photo, -90);
			
			ivContato.setImageBitmap(newPhoto);
		}		
		return true;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {    	
		switch (requestCode) {
			case INTENT_RESULT_DATA_CAMERA:
				if(data != null){
					Bundle bundle = data.getExtras();
					
					if(bundle != null){
						// Recupera o bitmap retornado pela camera
						Bitmap bitmap = (Bitmap) bundle.get("data");

						// Redimensiona a imagem						
						int newH = 72;
						int newW = (int) (bitmap.getWidth() / (bitmap.getWidth()/72));
						Bitmap resize = ImageManipulation.resize(bitmap, newH, newW);
						
						// Atualiza a imagem na ImageView
						ivContato.setImageBitmap(resize);
					}
				}				
				break;
		}
	}

	/**
	 * Cria um objeto Contato a partir dos componentes da Tela
	 * 
	 * @return contato
	 */
	private Contato getContato() {
		String id = "";
		
		try {
			id = tvId.getText().toString();
		} catch (NumberFormatException e) {
			//id = Contato.getId();
		}
		
		Bitmap foto = ((BitmapDrawable) ivContato.getDrawable()).getBitmap();
		String nome = etNome.getText().toString();
		String sobrenome = etSobrenome.getText().toString();
		String cargo = etCargo.getText().toString();
		String depto = etDepto.getText().toString();
		String telefone = etTelefone.getText().toString();
		String endereco = etEndereco.getText().toString();
		String cidade = etCidade.getText().toString();
		String estado = etEstado.getText().toString();
		
		Contato contato = new Contato(id, foto, nome, sobrenome, cargo, depto, telefone, endereco, cidade, estado);
		return contato;
	}
	
	/**
	 * Seta os campos da Tela a partir de um objeto Contato
	 * 
	 * @param contato
	 */
	private void setContato(Contato contato) {
		if(contato != null){
			tvId.setText(String.valueOf(contato.getId()));
			//ivContato.setImageBitmap(contato.getFoto());
			etNome.setText(contato.getName());
			etSobrenome.setText(contato.getLastName());
			etCargo.setText(contato.getCargo());
			etDepto.setText(contato.getDepto());
			etTelefone.setText(contato.getPhone());
			etEndereco.setText(contato.getStreet());
			etCidade.setText(contato.getCity());
			etEstado.setText(contato.getState());
		}
	}
	
	/**
	 * Ler os dados do contato com o id passado e preenche na tela
	 * 
	 * @param id_contato
	 */
	/*
	@SuppressWarnings("unused")
	private void carregaDadosPeloId(long id) {
		Contato contato = contatoDao.buscar(id);
		
		if(contato != null){
			setContato(contato);
		} else{
			Toast.makeText(this, "O contato de ID="+id+" não foi encontrado!",
					Toast.LENGTH_LONG).show();
			finish();
		}
	}
	*/
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);		
		outState.putParcelable(SAVE_STATE, getContato());
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);		
		Contato contato = savedInstanceState.getParcelable(SAVE_STATE);
		setContato(contato);
	}
}
