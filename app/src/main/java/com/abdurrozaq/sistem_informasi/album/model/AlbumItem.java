package com.abdurrozaq.sistem_informasi.album.model;

import com.google.gson.annotations.SerializedName;

public class AlbumItem{

	@SerializedName("album_tanggal")
	private String albumTanggal;

	@SerializedName("album_count")
	private String albumCount;

	@SerializedName("album_author")
	private String albumAuthor;

	@SerializedName("album_id")
	private String albumId;

	@SerializedName("album_nama")
	private String albumNama;

	@SerializedName("album_cover")
	private String albumCover;

	@SerializedName("album_pengguna_id")
	private String albumPenggunaId;

	public void setAlbumTanggal(String albumTanggal){
		this.albumTanggal = albumTanggal;
	}

	public String getAlbumTanggal(){
		return albumTanggal;
	}

	public void setAlbumCount(String albumCount){
		this.albumCount = albumCount;
	}

	public String getAlbumCount(){
		return albumCount;
	}

	public void setAlbumAuthor(String albumAuthor){
		this.albumAuthor = albumAuthor;
	}

	public String getAlbumAuthor(){
		return albumAuthor;
	}

	public void setAlbumId(String albumId){
		this.albumId = albumId;
	}

	public String getAlbumId(){
		return albumId;
	}

	public void setAlbumNama(String albumNama){
		this.albumNama = albumNama;
	}

	public String getAlbumNama(){
		return albumNama;
	}

	public void setAlbumCover(String albumCover){
		this.albumCover = albumCover;
	}

	public String getAlbumCover(){
		return albumCover;
	}

	public void setAlbumPenggunaId(String albumPenggunaId){
		this.albumPenggunaId = albumPenggunaId;
	}

	public String getAlbumPenggunaId(){
		return albumPenggunaId;
	}
}