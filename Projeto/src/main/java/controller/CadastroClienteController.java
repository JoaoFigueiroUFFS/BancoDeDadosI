package controller;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import dao.ClienteDAO;
import model.Cliente;

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
public class CadastroClienteController implements Serializable {
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
	private ClienteDAO clienteDAO;
	
	private Cliente cliente;

	private List<Cliente> listaClientes;

	@PostConstruct
	public void init() {
		this.listaClientes = clienteDAO.listAll();
	}

	public void novoCliente() {
		this.cliente = new Cliente();
	}

	public boolean validaCadastro(Cliente cliente) {

		if (!cliente.getNomeCliente().matches("^[\\p{L} .'-]+$") || cliente.getNomeCliente() == null) {
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Nome do cliente obrigatório e deve possuir apenas letras maiúsculas ou minúsculas!", null));
			return false;
		}

		if (cliente.getCpfCnpj() == null) {
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"CPF ou CNPJ|", null));
			return false;
		}

		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(cliente.getEmail());
		Boolean emailValido = m.matches();
		if (!emailValido) {
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "E-mail inválido!", null));
			return false;
		}

		if (cliente.getTelefone() == null
				|| !cliente.getTelefone().matches("^\\([1-9]{2}\\) (?:[2-8]|9[1-9])[0-9]{3}\\-[0-9]{4}$")) {
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Telefone inválido! Deve ser no formato (00) 00000-0000", null));
			return false;
		}

		return true;

	}

	public void salvar() {
		if (!validaCadastro(cliente)) {
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cadastro do Cliente Inválido", null));
			return; 
		}
			
		if (this.cliente.getIdCliente() == null) {
			if (this.clienteDAO.insert(this.cliente)) {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente Criado", null));
				PrimeFaces.current().executeScript("PF('clienteDialog').hide()");
				PrimeFaces.current().ajax().update("form:messages", "form:dt-cliente");
			} else {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Criar Cliente", null));
			}
		} else {
			if (this.clienteDAO.update(this.cliente)) {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente Atualizado", null));
				PrimeFaces.current().executeScript("PF('clienteDialog').hide()");
				PrimeFaces.current().ajax().update("form:messages", "form:dt-cliente");
			} else {
				this.facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Atualizar Cliente", null));
			}
		}
		this.listaClientes = clienteDAO.listAll();
		PrimeFaces.current().ajax().update("form:messages", "form:dt-cliente");
	}

	// Chamado pelo botão remover da tabela
	public void remover() {
		if (this.clienteDAO.delete(this.cliente))
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente Removido", null));
		else
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Remover Cliente", null));
		// Após excluir cliente é necessário recarregar lista que popula tabela com os
		// novos dados
		this.listaClientes = clienteDAO.listAll();
		this.cliente = null;
		PrimeFaces.current().ajax().update("form:messages", "form:dt-cliente");
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Cliente> getListaClientes() {
		return listaClientes;
	}
}
