package controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

import org.primefaces.PrimeFaces;

import dao.TipoPermissaoDAO;
import dao.UsuarioDAO;
import model.TipoPermissao;
import model.Usuario;

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
public class CadastroUsuarioController implements Serializable {
	private static final long serialVersionUID = 1L;

	//Anotação que marca atributo para ser gerenciado pelo CDI
	//O CDI criará uma instância do objeto automaticamente quando necessário
	@Inject
	private FacesContext facesContext;
	
	//atributos que não podem ser serializáveis (normalmente dependências externas) devem ser marcados como transient 
	//(eles são novamente criados a cada nova requisição independente do escopo da classe)
	@Inject
    transient private Pbkdf2PasswordHash passwordHash;
	
	@Inject
    private UsuarioDAO usuarioDAO;
	
	@Inject
    private TipoPermissaoDAO tipoPermissaoDAO;
	
	private Usuario usuario;
	
	private List<Usuario> listaUsuarios;
	
	private List<SelectItem> permissoes;
	
	private List<Integer> permissoesSelecionadas;
	
	//Anotação que força execução do método após o construtor da classe ser executado
    @PostConstruct
    public void init() {
    	//Verifica se usuário está autenticado e possui a permissão adequada
    	if (!this.facesContext.getExternalContext().isUserInRole("ADMINISTRADOR")) {
    		try {
				this.facesContext.getExternalContext().redirect("login-error.xhtml");
			} catch (IOException e) {e.printStackTrace();}
    	}
    	//Inicializa elementos importantes
    	this.permissoesSelecionadas = new ArrayList<Integer>();
    	this.listaUsuarios = usuarioDAO.listAll();
    	//O elemento de checkbox da tela usa uma lista do tipo SelectItem
    	this.permissoes = new ArrayList<SelectItem>();
    	//É necessário mapear a lista de permissões manualmente para o tipo SelectItem
    	for (TipoPermissao p: this.tipoPermissaoDAO.listAll()) {
    		//O primeiro elemento é a chave (oculta) e o segundo a descrição que aparecerá para o usuário em tela
    		SelectItem i = new SelectItem(p.getPermissao().id, p.getPermissao().name());		
    		this.permissoes.add(i);
    	}
    }
	
    //Chamado pelo botão novo
	public void novoCadastro() {
        this.usuario = new Usuario();
        this.permissoesSelecionadas.clear();
    }
	
	//Chamado ao salvar cadastro de usuário (novo ou edição)
	public void salvar() {
		//Chama método de verificação se usuário é válido (regras negociais)
        if (usuarioValido()) {
        	//Limpa lista de permissões de usuário (é mais simples limpar e adicionar todas novamente depois)
    		this.usuario.getPermissoes().clear();
    		//Adiciona todas as permissões selecionadas em tela
    		for (Integer id: this.permissoesSelecionadas) {
    			TipoPermissao permissao = tipoPermissaoDAO.findById(id);
    			//Chama método que adiciona o usuário para a permissão e vice-versa (necessário em relacionamento ManyToMany)
    			this.usuario.getPermissoes().add(permissao);
    		}
    		//Aplica Hash na senha
    		this.usuario.setSenha(this.passwordHash.generate(this.usuario.getSenha().toCharArray()));
	        //Verifica se é um novo cadastro ou é a alteração de um cadastro
    		if (this.usuario.getId() == null) {
	        	if (this.usuarioDAO.insert(this.usuario))
	        		this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário Criado", null));
	        	else
	        		this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Criar Usuário", null));
	        } else {
	        	if (this.usuarioDAO.update(this.usuario))
	        		this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário Atualizado", null));
	        	else
	        		this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Atualizar Usuário", null));
	        }
    		//Após salvar usuário é necessário recarregar lista que popula tabela com os novos dados
	        this.listaUsuarios = usuarioDAO.listAll();
	        //Atualiza e executa elementos Javascript na tela assincronamente
		    PrimeFaces.current().executeScript("PF('usuarioDialog').hide()");
		    PrimeFaces.current().ajax().update("form:messages", "form:dt-usuarios");
        }
	}	
	
	//Realiza validações adicionais (não relizadas no modelo) e/ou complexas/interdependentes
	private boolean usuarioValido() {
		if (this.permissoesSelecionadas.isEmpty()) {
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Selecione ao menos uma permissão para o novo usuário.", null));
			return false;
		}			
		if (this.usuario.getId() == null && this.usuarioDAO.findByName(this.usuario.getUsuario()).getId() != null) {
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Este nome de usuário já está em uso.", null));
			return false;
		}
		return true;
	}
	
	//Chamado pelo botão remover da tabela
	public void remover() {
		if (this.usuarioDAO.delete(this.usuario))
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário Removido", null));
		else 
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Remover Usuário", null));
		//Após excluir usuário é necessário recarregar lista que popula tabela com os novos dados
		this.listaUsuarios = usuarioDAO.listAll();
        //Limpa seleção de usuário
		this.usuario = null;
        PrimeFaces.current().ajax().update("form:messages", "form:dt-usuarios");
	}
	
	//Chamado pelo botão alterar da tabela
	public void alterar() {
		this.permissoesSelecionadas.clear();
		for (TipoPermissao p: this.usuario.getPermissoes())
			this.permissoesSelecionadas.add(p.getId());
		this.usuario.setSenha("");
	}
	
	//GETs e SETs
	//GETs são necessários para elementos visíveis em tela
	//SETs são necessários para elementos que serão editados em tela
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public List<SelectItem> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<SelectItem> permissoes) {
		this.permissoes = permissoes;
	}

	public List<Integer> getPermissoesSelecionadas() {
		return permissoesSelecionadas;
	}

	public void setPermissoesSelecionadas(List<Integer> permissoesSelecionadas) {
		this.permissoesSelecionadas = permissoesSelecionadas;
	}

}
