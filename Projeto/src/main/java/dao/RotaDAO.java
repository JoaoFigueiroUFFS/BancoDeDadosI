package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import model.Cidade;
import model.Rota;
import model.TipoPermissao;
import model.Usuario;
import util.DbUtil;
import util.Permissao;

public class RotaDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private DataSource ds;

	public Rota findById(Integer id) {
		Rota r = new Rota();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement("SELECT r.id_rota, r.nm_rota, r.dh_inicio, r.dh_fim, r.cd_vendedor FROM rota r WHERE r.id_rota = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {	
				Usuario u = new Usuario();
				UsuarioDAO ud = new UsuarioDAO();
				
				u.setId(ud.findById(rs.getInt("cd_vendedor")).getId());
				u.setUsuario(ud.findById(rs.getInt("cd_vendedor")).getUsuario());
				u.setSenha(ud.findById(rs.getInt("cd_vendedor")).getSenha());
				u.setEmail(ud.findById(rs.getInt("cd_vendedor")).getEmail());
				u.setPermissoes(ud.findById(rs.getInt("cd_vendedor")).getPermissoes());
				
				r.setIdRota(rs.getInt("id_rota"));
				r.setNmRota(rs.getString("nm_rota"));
				r.setDtInicio(rs.getString("dh_inicio"));
				r.setDtFim(rs.getString("dh_fim"));
				r.setCdVendedor(u);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return r;
	}

	public Rota findByName(String name) {
		Rota r = new Rota();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement(
					"SELECT r.id_rota, r.nm_rota, r.dh_inicio, r.dh_fim, r.cd_vendedor FROM rota r WHERE r.nm_rota ILIKE '%?%'");
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (rs.next()) {
				Usuario u = new Usuario();
				UsuarioDAO ud = new UsuarioDAO();
				
				u.setId(ud.findById(rs.getInt("cd_vendedor")).getId());
				u.setUsuario(ud.findById(rs.getInt("cd_vendedor")).getUsuario());
				u.setSenha(ud.findById(rs.getInt("cd_vendedor")).getSenha());
				u.setEmail(ud.findById(rs.getInt("cd_vendedor")).getEmail());
				u.setPermissoes(ud.findById(rs.getInt("cd_vendedor")).getPermissoes());
				
				r.setIdRota(rs.getInt("id_rota"));
				r.setNmRota(rs.getString("nm_rota"));
				r.setDtInicio(rs.getString("dh_inicio"));
				r.setDtFim(rs.getString("dh_fim"));
				r.setCdVendedor(u);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return r;
	}

	public List<Rota> listAll() {
		List<Rota> Rotas = new ArrayList<Rota>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement("SELECT r.id_rota, r.nm_rota, r.dh_inicio, r.dh_fim, r.cd_vendedor, u.usuario, u.senha, u.email FROM rota r JOIN usuario u ON r.cd_vendedor = u.id_usuario");
			rs = ps.executeQuery();
			while (rs.next()) {
				Usuario u = new Usuario();
				u.setId(rs.getInt("cd_vendedor"));
				u.setUsuario(rs.getString("usuario"));
				u.setEmail(rs.getString("email"));
				u.setSenha(rs.getString("senha"));
				
				Rota r = new Rota();
				r.setIdRota(rs.getInt("id_rota"));
				r.setNmRota(rs.getString("nm_rota"));
				r.setDtInicio(rs.getString("dh_inicio"));
				r.setDtFim(rs.getString("dh_fim"));
				r.setCdVendedor(u);
				
				Rotas.add(r);
			}
			con.close();
		} catch (SQLException r) {
			r.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return Rotas;
	}

	public Boolean insert(Rota r) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("INSERT INTO public.rota(nm_rota, dh_inicio, dh_fim, cd_vendedor) VALUES (?, ?, ?, ?) RETURNING id_rota");
				ps.setString(1, r.getNmRota());
				ps.setString(2, r.getDtInicio());
				ps.setString(3, r.getDtFim());
				ps.setInt(4, r.getCdVendedor().getId());
				
				rs = ps.executeQuery();
				rs.next();
				r.setIdRota(rs.getInt("id_rota"));
				con.commit();
				resultado = true;
			} catch (SQLException ex) {
				ex.printStackTrace();
				con.rollback();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return resultado;
	}

	public Boolean update(Rota r) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("UPDATE public.rota SET nm_rota=?, dh_inicio=?, dh_fim=?, cd_vendedor=? WHERE id_rota = ?");
				ps.setString(1, r.getNmRota());
				ps.setString(2, r.getDtInicio());
				ps.setString(3, r.getDtFim());
				ps.setInt(4, r.getCdVendedor().getId());
				ps.setInt(5, r.getIdRota());

				ps.execute();
				con.commit();
				resultado = true;
			} catch (SQLException ex) {
				ex.printStackTrace();
				con.rollback();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return resultado;
	}

	public Boolean delete(Rota r) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("DELETE FROM rota WHERE id_rota = ?");
				ps.setInt(1, r.getIdRota());
				ps.execute();
				con.commit();
				resultado = true;
			} catch (SQLException ex) {
				ex.printStackTrace();
				con.rollback();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return resultado;
	}
}
