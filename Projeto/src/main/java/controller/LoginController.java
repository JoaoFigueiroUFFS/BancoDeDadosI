package controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

import model.Usuario;
import dao.UsuarioDAO;

//Controlador da página de Login
@Named
@RequestScoped
public class LoginController {

	@Inject
	transient private Pbkdf2PasswordHash passwordHash;

	@Inject
	private FacesContext facesContext;

	private Usuario usuario;

	@Inject
	private UsuarioDAO usuario_banco;

	@PostConstruct
	public void inicializarUsuario() {
		usuario = new Usuario();
	}

	public void login() throws IOException {

		if (facesContext.getExternalContext().getAuthType() != null) {
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Existe um usuário autenticado! Use a opção logout primeiro.", ""));
		} else if (usuario.getUsuario() == usuario_banco.findByName(usuario.getUsuario()).getUsuario()
				&& this.passwordHash.verify(usuario.getSenha().toCharArray(), usuario_banco.findByName(usuario.getUsuario()).getSenha())) { 
			facesContext.getExternalContext().redirect("cadastro_usuario.xhtml");
		} else {
			usuario = new Usuario();
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Inválido!", "Usuário ou senha incorretos."));
		}
	}

	public void logout() throws IOException {
		facesContext.getExternalContext().invalidateSession();
		facesContext.getExternalContext().redirect("logout.xhtml");
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
