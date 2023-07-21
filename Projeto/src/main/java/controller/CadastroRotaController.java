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

import dao.RotaDAO;
import model.Rota;

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
public class CadastroRotaController implements Serializable {
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
	private RotaDAO rotaDAO;

	private Rota rota;

	private List<Rota> listaRotas;

	@PostConstruct
	public void init() {
		this.listaRotas = rotaDAO.listAll();
	}

	public void novaRota() {
		this.rota = new Rota();
	}

	public void salvar() {
			
		if (this.rota.getIdRota() == null) {
			if (this.rotaDAO.insert(this.rota)) {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Rota Criada", null));
				PrimeFaces.current().executeScript("PF('rotaDialog').hide()");
				PrimeFaces.current().ajax().update("form:messages", "form:dt-rota");
			} else {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Criar Rota", null));
			}
		} else {
			if (this.rotaDAO.update(this.rota)) {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Rota Atualizado", null));
				PrimeFaces.current().executeScript("PF('rotaDialog').hide()");
				PrimeFaces.current().ajax().update("form:messages", "form:dt-rota");
			} else {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Atualizar Rota", null));
			}
		}
		this.listaRotas = rotaDAO.listAll();	
	}

	public void remover() {
		if (this.rotaDAO.delete(this.rota))
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rota Removida", null));
		else
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Remover Rota", null));
		this.listaRotas = rotaDAO.listAll();
		this.rota = null;
		PrimeFaces.current().ajax().update("form:messages", "form:dt-rota");
	}

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public List<Rota> getlistaRotas() {
		return listaRotas;
	}

	public void setlistaRotas(List<Rota> listaRotas) {
		this.listaRotas = listaRotas;
	}

}
