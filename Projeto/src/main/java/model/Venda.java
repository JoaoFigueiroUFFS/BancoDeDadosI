package model;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class Venda {

	private Integer idNota;
    
    private String dtVenda;
    
    private Double vlTotal;
    
    private Cliente cdCliente; 
    
    private CidadesRota cidadesRota;
    
    private List<Venda> vendas = new ArrayList<Venda>();
    
	public Integer getIdNota() {
		return idNota;
	}

	public void setIdNota(Integer idNota) {
		this.idNota = idNota;
	}

	public Double getVlTotal() {
		return vlTotal;
	}

	public void setVlTotal(Double vlTotal) {
		this.vlTotal = vlTotal;
	}

	public Cliente getCdCliente() {
		return cdCliente;
	}

	public void setCdCliente(Cliente cdCliente) {
		this.cdCliente = cdCliente;
	}

	public CidadesRota getCidadesRota() {
		return cidadesRota;
	}

	public void setCidadesRota(CidadesRota cidadesRota) {
		this.cidadesRota = cidadesRota;
	}

	public List<Venda> getVendas() {
		return vendas;
	}

	public void setVendas(List<Venda> vendas) {
		this.vendas = vendas;
	}

	public String getDtVenda() {
		return dtVenda;
	}

	public void setDtVenda(String dtVenda) {
		this.dtVenda = dtVenda;
	}


}
    
    