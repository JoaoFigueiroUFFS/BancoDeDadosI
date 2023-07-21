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

import dao.VendaDAO;
import model.Cliente;
import model.Venda;

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
public class CadastroVendaController implements Serializable {
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
	private VendaDAO vendaDAO;

	private Venda venda;
	
	private Cliente cliente; 
	
	private List<Venda> listaVendas;

	@PostConstruct
	public void init() {
		this.listaVendas = vendaDAO.listAll();
	}

	public void novaVenda() {
		this.venda = new Venda();
	}

	public void salvar() {
			
		if (this.vendaDAO.insert(this.venda)) {
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Venda Criada", null));
			PrimeFaces.current().executeScript("PF('vendaDialog').hide()");
			PrimeFaces.current().ajax().update("form:messages", "form:dt-venda");
		} else {
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Criar Venda", null));
		}

		if (this.vendaDAO.update(this.venda)) {
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Venda Atualizada", null));
			PrimeFaces.current().executeScript("PF('vendaDialog').hide()");
			PrimeFaces.current().ajax().update("form:messages", "form:dt-venda");
		} else {
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Atualizar Venda", null));
		}
		this.listaVendas = vendaDAO.listAll();	
	}

	public void remover() {
		if (this.vendaDAO.delete(this.venda))
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Venda Removida", null));
		else
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Remover Venda", null));
		this.listaVendas = vendaDAO.listAll();
		this.venda = null;
		PrimeFaces.current().ajax().update("form:messages", "form:dt-venda");
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public List<Venda> getlistaVendas() {
		return listaVendas;
	}

	public void setlistaVendas(List<Venda> listaVendas) {
		this.listaVendas = listaVendas;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
