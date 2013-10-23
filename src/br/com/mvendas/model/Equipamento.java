package br.com.mvendas.model;

import java.util.List;

public class Equipamento {

	private String id;
	private String sitio;
	private String name;
	private String status;
	private String endereco;
	
	public Equipamento(String[] campos, List<String> valores) {
		String chave;
		String valor;
		for (int i = 0; i < campos.length; i++) {
			chave = campos[i];
			valor = valores.get(i);
			if(chave.equalsIgnoreCase("id")){
				setId(valor);
			} if(chave.equalsIgnoreCase("name")){
				setName(valor);
			} if(chave.equalsIgnoreCase("status")){
				setStatus(valor);
			} if(chave.equalsIgnoreCase("endereco")){
				setEndereco(valor);
			}			
		}
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSitio() {
		return sitio;
	}
	public void setSitio(String sitio) {
		this.sitio = sitio;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
		
}
