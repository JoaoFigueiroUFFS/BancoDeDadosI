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

import dao.EstadoDAO;
import model.Estado;

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
public class CadastroEstadoController implements Serializable {
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
	private EstadoDAO estadoDAO;

	private Estado estado;

	private List<Estado> listaEstados;

	@PostConstruct
	public void init() {
		this.listaEstados = estadoDAO.listAll();
	}

	public void novoEstado() {
		this.estado = new Estado();
	}

	public void salvar() {
			
		if (this.estado.getId() == null) {
			if (this.estadoDAO.insert(this.estado)) {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Estado Criado", null));
				PrimeFaces.current().executeScript("PF('estadoDialog').hide()");
				PrimeFaces.current().ajax().update("form:messages", "form:dt-estado");
			} else {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Criar Estado", null));
			}
		} else {
			if (this.estadoDAO.update(this.estado)) {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Estado Atualizado", null));
				PrimeFaces.current().executeScript("PF('estadoDialog').hide()");
				PrimeFaces.current().ajax().update("form:messages", "form:dt-estado");
			} else {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Atualizar Estado", null));
			}
		}
		this.listaEstados = estadoDAO.listAll();	
	}

	// Chamado pelo botão remover da tabela
	public void remover() {
		if (this.estadoDAO.delete(this.estado))
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Estado Removido", null));
		else
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Remover Estado", null));
		// Após excluir estado é necessário recarregar lista que popula tabela com os
		// novos dados
		this.listaEstados = estadoDAO.listAll();
		// Limpa seleção de estado
		this.estado = null;
		PrimeFaces.current().ajax().update("form:messages", "form:dt-estado");
	}

	// GETs e SETs
	// GETs são necessários para elementos visíveis em tela
	// SETs são necessários para elementos que serão editados em tela
	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Estado> getlistaEstados() {
		return listaEstados;
	}

	public void setlistaEstados(List<Estado> listaEstados) {
		this.listaEstados = listaEstados;
	}

}
