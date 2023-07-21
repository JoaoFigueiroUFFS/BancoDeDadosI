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

import dao.CidadeDAO;
import model.Cidade;


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
public class CadastroCidadeController implements Serializable {
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
	private CidadeDAO cidadeDAO;
	
	private Cidade cidade;
		
	private List<Cidade> listaCidades;

	@PostConstruct
	public void init() {
		this.listaCidades = cidadeDAO.listAll();
	}

	public void novaCidade() {
		this.cidade = new Cidade();
	}

	public void salvar() {	
		if (this.cidade.getIdCidade() == null) {
			if (this.cidadeDAO.insert(this.cidade)) {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Cidade Criada", null));
				PrimeFaces.current().executeScript("PF('cidadeDialog').hide()");
				PrimeFaces.current().ajax().update("form:messages", "form:dt-cidade");
			} else {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Criar Cidade", null));
			}
		} else {
			if (this.cidadeDAO.update(this.cidade)) {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Cidade Atualizada", null));
				PrimeFaces.current().executeScript("PF('cidadeDialog').hide()");
				PrimeFaces.current().ajax().update("form:messages", "form:dt-cidade");
			} else {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Atualizar Cidade", null));
			}
		}
		this.listaCidades = cidadeDAO.listAll();	
	}

	// Chamado pelo botão remover da tabela
	public void remover() {
		if (this.cidadeDAO.delete(this.cidade))
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cidade Removido", null));
		else
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Remover Cidade", null));
		// Após excluir cidade é necessário recarregar lista que popula tabela com os
		// novos dados
		this.listaCidades = cidadeDAO.listAll();
		// Limpa seleção de cidade
		this.cidade = null;
		PrimeFaces.current().ajax().update("form:messages", "form:dt-cidade");
	}

	// GETs e SETs
	// GETs são necessários para elementos visíveis em tela
	// SETs são necessários para elementos que serão editados em tela
	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public List<Cidade> getlistaCidades() {
		return listaCidades;
	}

	public void setlistaCidades(List<Cidade> listaCidades) {
		this.listaCidades = listaCidades;
	}
	
}
