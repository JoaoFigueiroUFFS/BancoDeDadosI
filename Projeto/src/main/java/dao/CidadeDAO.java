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
import model.Estado;
import util.DbUtil;

public class CidadeDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private DataSource ds;
	

	public Cidade findById(Integer id) {
		Cidade c = new Cidade();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement("SELECT c.id_cidade, c.nm_cidade, e.sg_uf, e.nm_estado FROM cidade c JOIN estado e ON c.cd_uf = e.cd_uf WHERE c.id_cidade = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				c.setIdCidade(rs.getInt("id_cidade"));
				c.setNomeCidade(rs.getString("nm_cidade"));
				c.getEstado().setId(rs.getInt("cd_uf"));
				c.getEstado().setSgUf(rs.getString("sg_uf"));
				c.getEstado().setNomeEstado(rs.getString("nm_estado"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return c;
	}

	public Cidade findByName(String name) {
		Cidade c = new Cidade();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement(
					"SELECT c.id_cidade, c.nm_cidade, e.cd_uf, e.sg_uf, e.nm_estado FROM cidade c JOIN estado e ON c.cd_uf = e.cd_uf WHERE c.nm_cidade ILIKE '%?%'");
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (rs.next()) {
				c.setIdCidade(rs.getInt("id_cidade"));
				c.setNomeCidade(rs.getString("nm_cidade"));
				c.getEstado().setId(rs.getInt("cd_uf"));
				c.getEstado().setSgUf(rs.getString("sg_uf"));
				c.getEstado().setNomeEstado(rs.getString("nm_estado"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return c;
	}

	public List<Cidade> listAll() {
		List<Cidade> Cidades = new ArrayList<Cidade>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement("SELECT c.id_cidade, c.nm_cidade, e.id_uf, e.sg_uf, e.nm_estado FROM cidade c JOIN estado e ON c.cd_uf = e.id_uf");
			rs = ps.executeQuery();
			while (rs.next()) {
				Estado es = new Estado();
				es.setId(rs.getInt("id_cidade"));
				es.setSgUf(rs.getString("sg_uf"));
				es.setNomeEstado(rs.getString("nm_estado"));
				
				Cidade c = new Cidade();
				c.setIdCidade(rs.getInt("id_cidade"));
				c.setNomeCidade(rs.getString("nm_cidade"));
				c.setEstado(es);
				
				Cidades.add(c);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return Cidades;
	}

	public Boolean insert(Cidade c) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement(
						"INSERT INTO cidade (nm_cidade, cd_uf) VALUES (?, ?) RETURNING id_cidade");
				ps.setString(1, c.getNomeCidade());
				ps.setInt(2, c.getEstado().getId());

				rs = ps.executeQuery();
				rs.next();
				c.setIdCidade(rs.getInt("id_cidade"));
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

	public Boolean update(Cidade c) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("UPDATE cidade SET nm_cidade = ?, cd_uf = ? WHERE id_cidade = ?");
				ps.setString(1, c.getNomeCidade());
				ps.setInt(2, c.getEstado().getId());
				ps.setInt(3, c.getIdCidade());

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

	public Boolean delete(Cidade c) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("DELETE FROM cidade WHERE id_cidade = ?");
				ps.setInt(1, c.getIdCidade());
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
