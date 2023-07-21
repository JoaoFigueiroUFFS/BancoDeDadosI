package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.UsuarioDAO;

public class Rota {

	private Integer idRota;
    
    private String nmRota;
    
    private String dtInicio;
    
    private String dtFim; 
    
    private Usuario cdVendedor;
    
    private List<Rota> rotas = new ArrayList<Rota>();
    
	public Integer getIdRota() {
		return idRota;
	}

	public void setIdRota(Integer idRota) {
		this.idRota = idRota;
	}

	public String getNmRota() {
		return nmRota;
	}

	public void setNmRota(String nmRota) {
		this.nmRota = nmRota;
	}

	public List<Rota> getRotas() {
		return rotas;
	}

	public void setRotas(List<Rota> rotas) {
		this.rotas = rotas;
	}

	public String getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(String dtInicio) {
		this.dtInicio = dtInicio;
	}

	public String getDtFim() {
		return dtFim;
	}

	public void setDtFim(String dtFim) {
		this.dtFim = dtFim;
	}

	public Usuario getCdVendedor() {
		return cdVendedor;
	}

	public void setCdVendedor(Usuario cdVendedor) {
		this.cdVendedor = cdVendedor;
	}


}
    
    