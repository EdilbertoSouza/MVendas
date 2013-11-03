package br.com.mvendas.adapter;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.example.mvendas.R;
import br.com.mvendas.model.Cliente;

public class ClientesListAdapter extends RoboBaseAdapter {
	
	@InjectView(R.id.adapter_cliente_item_tvItem)
	private TextView adapter_cliente_item_tvItem;

	private List<Cliente> clientes;

	public ClientesListAdapter(Context context, int layoutId, List<Cliente> clientes) {
		super(context, layoutId);
		
		this.clientes = (clientes == null) ? new ArrayList<Cliente>() : clientes;
	}
	
	@Override
	public int getCount() {
		return clientes.size();
	}

	@Override
	public Object getItem(int position) {
		return clientes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public List<Cliente> getClientes() {
		return this.clientes;
	}
	
	public void add(Cliente clientes) {
		this.clientes.add(clientes);
	}
	
	public void addAll(List<Cliente> clientes) {
		this.clientes = (clientes == null) ? new ArrayList<Cliente>() : clientes;
	}
	
	public void clear() {
		this.clientes.clear();
	}
	
	public void newList(List<Cliente> clientes) {
		clear();
		addAll(clientes);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		
		Cliente cliente = (Cliente) getItem(position);
		adapter_cliente_item_tvItem.setText(cliente.getName());
		
		return view;
	}

}
