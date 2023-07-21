package model;

import java.util.ArrayList;
import java.util.List;

public class CidadesRota {

	private Integer idCidadeRota;
    
    private Rota rota;
    
    private Cidade cidade;
    
    private List<CidadesRota> cidadesRotas = new ArrayList<CidadesRota>();
    
	public Integer getIdCidadeRota() {
		return idCidadeRota;
	}

	public void setIdCidadeRota(Integer idCidadeRota) {
		this.idCidadeRota = idCidadeRota;
	}

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public List<CidadesRota> getCidadesRotas() {
		return cidadesRotas;
	}

	public void setCidadesRotas(List<CidadesRota> cidadesRotas) {
		this.cidadesRotas = cidadesRotas;
	}

}
    
    