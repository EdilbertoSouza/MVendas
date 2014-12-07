package br.com.mvendas.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Contato implements Parcelable {

	private long idlocal;
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
	
	public Contato(long idlocal, String id, String nome, String lastName, String cargo, String depto, 
		String endereco, String cidade, String estado, String telefone, Bitmap foto) {
		setIdLocal(idlocal);
		setId(id);
		setName(nome);
		setLastName(lastName);
		setCargo(cargo);
		setDepto(depto);
		setStreet(endereco);
		setCity(cidade);
		setState(estado);
		setPhone(telefone);
	}
	
	public Contato(Parcel in) {
		readFromParcel(in);
	}
	
	public Contato(String name_values) {
		
		String[] columns = name_values.split(";");
		
		for (int i = 0; i < columns.length; i++) {

			String [] name_value = columns[i].toString().split("=");
			String name  = name_value[0];
			String value = name_value.length > 1 ? name_value[1] : "";

			if(name.equalsIgnoreCase("id")){
				setId(value);
			} if(name.equalsIgnoreCase("first_name")){
				setName(value);
			} if(name.equalsIgnoreCase("last_name")){
				setLastName(value);
			} if(name.equalsIgnoreCase("title")){
				setCargo(value);
			} if(name.equalsIgnoreCase("department")){
				setDepto(value);
			} if(name.equalsIgnoreCase("primary_address_street")){
				setStreet(value);
			} if(name.equalsIgnoreCase("primary_address_city")){
				setCity(value);
			} if(name.equalsIgnoreCase("primary_address_state")){
				setState(value);
			} if(name.equalsIgnoreCase("phone_mobile")){
				setPhone(value);
			}
		}
	}

	public Contato() {
	}

	public void setIdLocal(long idlocal) {
		this.idlocal = idlocal;
	}	

	public Long getIdLocal() {
		return idlocal;
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
		dest.writeLong(idlocal);
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
		idlocal = in.readLong();
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
