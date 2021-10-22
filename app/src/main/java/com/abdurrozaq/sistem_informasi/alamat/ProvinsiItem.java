package com.abdurrozaq.sistem_informasi.alamat;

import com.google.gson.annotations.SerializedName;

public class ProvinsiItem {

    @SerializedName("nama")
    private String nama;

    @SerializedName("id")
    private String id;


	@SerializedName("dataid")
    private String dataid;

	public String getDataid() {
		return dataid;
	}

	public void setDataid(String dataid) {
		this.dataid = dataid;
	}

	public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}