package controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import dao.CidadesRotaDAO;
import model.CidadesRota;

//Escopo do objeto da classe (Bean)
//ApplicationScoped é usado quando o objeto é único na aplicação (compartilhado entre usuários), permanece ativo enquanto a aplicação estiver ativa
//SessionScoped é usado quando o objeto é único por usuário, permanece ativo enquanto a sessão for ativa
//ViewScoped é usado quando o objeto permanece ativo enquanto não houver um redirect (acesso a nova página)
//RequestScoped é usado quando o objeto só existe naquela requisição específica
//Quanto maior o escopo, maior o uso de memória no lado do servidor (objeto permanece ativo por mais tempo)
//Escopos maiores que Request exigem que classes sejam serializáveis assim como todos os seus atributos (recurso de segurança)
@ViewScoped
//Torna classe disponível na camada de visão (html) - são chamados de Beans ou ManagedBeans (gerenciados pelo JSF/EJB)
@Named
public class CadastroCidadesRotaController implements Serializable {
	private static final long serialVersionUID = 1L;

	// Anotação que marca atributo para ser gerenciado pelo CDI
	// O CDI criará uma instância do objeto automaticamente quando necessário
	@Inject
	private FacesContext facesContext;

	// atributos que não podem ser serializáveis (normalmente dependências externas)
	// devem ser marcados como transient
	// (eles são novamente criados a cada nova requisição independente do escopo da
	// classe)

	@Inject
	private CidadesRotaDAO cidadesRotaDAO;

	private CidadesRota cidadesRota;

	private List<CidadesRota> listaCidadesRotas;

	@PostConstruct
	public void init() {
		this.listaCidadesRotas = cidadesRotaDAO.listAll();
	}

	public void novaCidadesRota() {
		this.cidadesRota = new CidadesRota();
	}

	public void salvar() {
			
		if (this.cidadesRota.getIdCidadeRota() == null) {
			if (this.cidadesRotaDAO.insert(this.cidadesRota)) {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "CidadesRota Criado", null));
				PrimeFaces.current().executeScript("PF('cidadesRotaDialog').hide()");
				PrimeFaces.current().ajax().update("form:messages", "form:dt-cidadesRota");
			} else {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Criar CidadesRota", null));
			}
		} else {
			if (this.cidadesRotaDAO.update(this.cidadesRota)) {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "CidadesRota Atualizado", null));
				PrimeFaces.current().executeScript("PF('cidadesRotaDialog').hide()");
				PrimeFaces.current().ajax().update("form:messages", "form:dt-cidadesRota");
			} else {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Atualizar CidadesRota", null));
			}
		}
		this.listaCidadesRotas = cidadesRotaDAO.listAll();	
	}

	public void remover() {
		if (this.cidadesRotaDAO.delete(this.cidadesRota))
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "CidadesRota Removido", null));
		else
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Remover CidadesRota", null));
		this.listaCidadesRotas = cidadesRotaDAO.listAll();
		this.cidadesRota = null;
		PrimeFaces.current().ajax().update("form:messages", "form:dt-cidadesRota");
	}

	public CidadesRota getCidadesRota() {
		return cidadesRota;
	}

	public void setCidadesRota(CidadesRota cidadesRota) {
		this.cidadesRota = cidadesRota;
	}

	public List<CidadesRota> getlistaCidadesRotas() {
		return listaCidadesRotas;
	}

	public void setlistaCidadesRotas(List<CidadesRota> listaCidadesRotas) {
		this.listaCidadesRotas = listaCidadesRotas;
	}

}
