package com.abdurrozaq.sistem_informasi.berita.model;

import com.google.gson.annotations.SerializedName;

public class BeritaItem{

	@SerializedName("tulisan_pengguna_id")
	private String tulisanPenggunaId;

	@SerializedName("tulisan_img_slider")
	private String tulisanImgSlider;

	@SerializedName("tulisan_kategori_nama")
	private String tulisanKategoriNama;

	@SerializedName("tulisan_author")
	private String tulisanAuthor;

	@SerializedName("tulisan_judul")
	private String tulisanJudul;

	@SerializedName("tulisan_tanggal")
	private String tulisanTanggal;

	@SerializedName("tulisan_views")
	private String tulisanViews;

	@SerializedName("tulisan_gambar")
	private String tulisanGambar;

	@SerializedName("tulisan_id")
	private String tulisanId;

	@SerializedName("tulisan_isi")
	private String tulisanIsi;

	@SerializedName("tulisan_kategori_id")
	private String tulisanKategoriId;

	public void setTulisanPenggunaId(String tulisanPenggunaId){
		this.tulisanPenggunaId = tulisanPenggunaId;
	}

	public String getTulisanPenggunaId(){
		return tulisanPenggunaId;
	}

	public void setTulisanImgSlider(String tulisanImgSlider){
		this.tulisanImgSlider = tulisanImgSlider;
	}

	public String getTulisanImgSlider(){
		return tulisanImgSlider;
	}

	public void setTulisanKategoriNama(String tulisanKategoriNama){
		this.tulisanKategoriNama = tulisanKategoriNama;
	}

	public String getTulisanKategoriNama(){
		return tulisanKategoriNama;
	}

	public void setTulisanAuthor(String tulisanAuthor){
		this.tulisanAuthor = tulisanAuthor;
	}

	public String getTulisanAuthor(){
		return tulisanAuthor;
	}

	public void setTulisanJudul(String tulisanJudul){
		this.tulisanJudul = tulisanJudul;
	}

	public String getTulisanJudul(){
		return tulisanJudul;
	}

	public void setTulisanTanggal(String tulisanTanggal){
		this.tulisanTanggal = tulisanTanggal;
	}

	public String getTulisanTanggal(){
		return tulisanTanggal;
	}

	public void setTulisanViews(String tulisanViews){
		this.tulisanViews = tulisanViews;
	}

	public String getTulisanViews(){
		return tulisanViews;
	}

	public void setTulisanGambar(String tulisanGambar){
		this.tulisanGambar = tulisanGambar;
	}

	public String getTulisanGambar(){
		return tulisanGambar;
	}

	public void setTulisanId(String tulisanId){
		this.tulisanId = tulisanId;
	}

	public String getTulisanId(){
		return tulisanId;
	}

	public void setTulisanIsi(String tulisanIsi){
		this.tulisanIsi = tulisanIsi;
	}

	public String getTulisanIsi(){
		return tulisanIsi;
	}

	public void setTulisanKategoriId(String tulisanKategoriId){
		this.tulisanKategoriId = tulisanKategoriId;
	}

	public String getTulisanKategoriId(){
		return tulisanKategoriId;
	}
}