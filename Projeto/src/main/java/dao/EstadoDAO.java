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

import model.Estado;
import util.DbUtil;

public class EstadoDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private DataSource ds;

	public Estado findById(Integer id) {
		Estado e = new Estado();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement("SELECT e.id_uf, e.sg_uf, e.nm_estado FROM estado e WHERE e.id_uf = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				e.setId(rs.getInt("id_uf"));
				e.setSgUf(rs.getString("sg_uf"));
				e.setNomeEstado(rs.getString("nm_estado"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return e;
	}

	public Estado findByName(String name) {
		Estado e = new Estado();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement(
					"SELECT e.id_uf, e.sg_uf, e.nm_estado FROM estado e WHERE e.nm_estado ILIKE '%?%'");
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (rs.next()) {
				e.setId(rs.getInt("id_uf"));
				e.setSgUf(rs.getString("sg_uf"));
				e.setNomeEstado(rs.getString("nm_estado"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return e;
	}

	public List<Estado> listAll() {
		List<Estado> Estados = new ArrayList<Estado>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement("SELECT e.id_uf, e.sg_uf, e.nm_estado FROM estado e");
			rs = ps.executeQuery();
			while (rs.next()) {
				Estado e = new Estado();
				e.setId(rs.getInt("id_uf"));
				e.setSgUf(rs.getString("sg_uf"));
				e.setNomeEstado(rs.getString("nm_estado"));
				Estados.add(e);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return Estados;
	}

	public Boolean insert(Estado e) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement(
						"INSERT INTO estado (sg_uf, nm_estado) VALUES (?, ?) RETURNING id_uf");
				ps.setString(1, e.getSgUf());
				ps.setString(2, e.getNomeEstado());

				rs = ps.executeQuery();
				rs.next();
				e.setId(rs.getInt("id_uf"));
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

	public Boolean update(Estado e) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("UPDATE estado SET sg_uf = ?, nm_estado = ? WHERE id_uf = ?");
				ps.setString(1, e.getSgUf());
				ps.setString(2, e.getNomeEstado());
				ps.setInt(3, e.getId());

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

	public Boolean delete(Estado e) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("DELETE FROM estado WHERE id_uf = ?");
				ps.setInt(1, e.getId());
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
