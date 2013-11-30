package br.com.mvendas.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Contato implements Parcelable {

	private String id;
	private Bitmap foto;
	private String name;
	private String lastName;
	private String cargo;
	private String depto;
	private String street;
	private String city;
	private String state;
	private String phone;
	private String email;
	
	public Contato(String id, Bitmap foto, String nome, String lastName, String cargo, String depto, 
			String telefone, String endereco, String cidade, String estado) {
		setId(id);
		setName(nome);
		setLastName(lastName);
		setCargo(cargo);
		setDepto(depto);
		setPhone(telefone);
		setStreet(endereco);
		setCity(cidade);
		setState(estado);
	}
	
	public Contato(Parcel in) {
		readFromParcel(in);
	}
	
	public Contato(String contato) {
		
		String[] linhas = contato.split(";");
		
		for (int i = 0; i < linhas.length; i++) {
			String [] split = linhas[i].toString().split("=");
			String chave = split[0];
			String valor = split[1];
			if(chave.equalsIgnoreCase("id")){
				setId(valor);
			} if(chave.equalsIgnoreCase("first_name")){
				setName(valor);
			} if(chave.equalsIgnoreCase("last_name")){
				setLastName(valor);
			} if(chave.equalsIgnoreCase("title")){
				setCargo(valor);
			} if(chave.equalsIgnoreCase("department")){
				setDepto(valor);
			} if(chave.equalsIgnoreCase("primary_address_street")){
				setStreet(valor);
			} if(chave.equalsIgnoreCase("primary_address_city")){
				setCity(valor);
			} if(chave.equalsIgnoreCase("primary_address_state")){
				setState(valor);
			} if(chave.equalsIgnoreCase("phone_mobile")){
				setPhone(valor);
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Bitmap getFoto() {
		return foto;
	}

	public void setFoto(Bitmap foto) {
		this.foto = foto;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getDepto() {
		return depto;
	}

	public void setDepto(String depto) {
		this.depto = depto;
	}	

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	//////////////////////////////////////////////////////////////
	// Parcelable
	
	public int describeContents() {
		return 0;
	}
	
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeParcelable(foto, flags);
		dest.writeString(name);
		dest.writeString(lastName);
		dest.writeString(cargo);
		dest.writeString(depto);
		dest.writeString(phone);
		dest.writeString(street);
		dest.writeString(city);
		dest.writeString(state);
	}
	
	private void readFromParcel(Parcel in) {
		id = in.readString();
		foto = in.readParcelable(Bitmap.class.getClassLoader());
		name = in.readString();
		lastName = in.readString();
		cargo = in.readString();
		depto = in.readString();
		phone = in.readString();
		street = in.readString();
		city = in.readString();
		state = in.readString();
	}

	public static final Parcelable.Creator<Contato> CREATOR = new Parcelable.Creator<Contato>() {
		public Contato createFromParcel(Parcel in) {
			return new Contato(in);
		}

		public Contato[] newArray(int size) {
			return new Contato[size];
		}
	};

}
