package br.com.mvendas.adapter;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.example.mvendas.R;
import br.com.mvendas.model.Contato;

public class ContatosListAdapter extends RoboBaseAdapter {
	
	@InjectView(R.id.adapter_contato_item_tvItem)
	private TextView adapter_contato_item_tvItem;

	private List<Contato> contatos;

	public ContatosListAdapter(Context context, int layoutId, List<Contato> contatos) {
		super(context, layoutId);
		
		this.contatos = (contatos == null) ? new ArrayList<Contato>() : contatos;
	}
	
	@Override
	public int getCount() {
		return contatos.size();
	}

	@Override
	public Object getItem(int position) {
		return contatos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public List<Contato> getContatos() {
		return this.contatos;
	}
	
	public void add(Contato contatos) {
		this.contatos.add(contatos);
	}
	
	public void addAll(List<Contato> contatos) {
		this.contatos = (contatos == null) ? new ArrayList<Contato>() : contatos;
	}
	
	public void clear() {
		this.contatos.clear();
	}
	
	public void newList(List<Contato> contatos) {
		clear();
		addAll(contatos);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		
		Contato contato = (Contato) getItem(position);
		adapter_contato_item_tvItem.setText(contato.getName() + contato.getCargo());
		
		return view;
	}

}
