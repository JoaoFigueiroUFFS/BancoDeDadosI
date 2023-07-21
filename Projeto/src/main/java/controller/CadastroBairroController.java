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

import dao.BairroDAO;
import model.Bairro;

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
public class CadastroBairroController implements Serializable {
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
	private BairroDAO bairroDAO;

	private Bairro bairro;

	private List<Bairro> listaBairros;

	@PostConstruct
	public void init() {
		this.listaBairros = bairroDAO.listAll();
	}

	public void novoBairro() {
		this.bairro = new Bairro();
	}

	public void salvar() {
			
		if (this.bairro.getIdBairro() == null) {
			if (this.bairroDAO.insert(this.bairro)) {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Bairro Criado", null));
				PrimeFaces.current().executeScript("PF('bairroDialog').hide()");
				PrimeFaces.current().ajax().update("form:messages", "form:dt-bairro");
			} else {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Criar Bairro", null));
			}
		} else {
			if (this.bairroDAO.update(this.bairro)) {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Bairro Atualizado", null));
				PrimeFaces.current().executeScript("PF('bairroDialog').hide()");
				PrimeFaces.current().ajax().update("form:messages", "form:dt-bairro");
			} else {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Atualizar Bairro", null));
			}
		}
		this.listaBairros = bairroDAO.listAll();	
	}

	// Chamado pelo botão remover da tabela
	public void remover() {
		if (this.bairroDAO.delete(this.bairro))
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bairro Removido", null));
		else
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Remover Bairro", null));
		// Após excluir bairro é necessário recarregar lista que popula tabela com os
		// novos dados
		this.listaBairros = bairroDAO.listAll();
		// Limpa seleção de bairro
		this.bairro = null;
		PrimeFaces.current().ajax().update("form:messages", "form:dt-bairro");
	}

	// GETs e SETs
	// GETs são necessários para elementos visíveis em tela
	// SETs são necessários para elementos que serão editados em tela
	public Bairro getBairro() {
		return bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}

	public List<Bairro> getlistaBairros() {
		return listaBairros;
	}

	public void setlistaBairros(List<Bairro> listaBairros) {
		this.listaBairros = listaBairros;
	}

}
