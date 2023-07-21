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
import model.Bairro;
import util.DbUtil;

public class BairroDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private DataSource ds;

	public Bairro findById(Integer id) {
		Bairro b = new Bairro();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement("SELECT b.id_bairro, b.nm_bairro, b.cep, c.id_cidade, c.nm_cidade, e.id_uf, e.nm_estado, e.sg_uf FROM bairro b JOIN cidade c ON b.cd_cidade = c.id_cidade JOIN estado e ON c.cd_uf = e.id_uf WHERE b.id_bairro = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				Estado e = new Estado();
				e.setId(rs.getInt("id_uf"));
				e.setNomeEstado(rs.getString("nm_estado"));
				e.setSgUf(rs.getString("sg_uf"));
				
				Cidade c = new Cidade();
				c.setIdCidade(rs.getInt("id_cidade"));
				c.setNomeCidade(rs.getString("nm_cidade"));
				c.setEstado(e);
				
				b.setIdBairro(rs.getInt("id_bairro"));
				b.setNomeBairro(rs.getString("nm_bairro"));
				b.setCep(rs.getString("cep"));
				b.setCidade(c);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return b;
	}

	public Bairro findByName(String name) {
		Bairro b = new Bairro();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement(
					"SELECT b.id_bairro, b.nm_bairro, b.cep, c.id_cidade, c.nm_cidade, e.id_uf, e.nm_estado, e.sg_uf FROM bairro b JOIN cidade c ON b.cd_cidade = c.id_cidade JOIN estado e ON c.cd_uf = e.id_uf WHERE b.nm_bairro ILIKE '%?%'");
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (rs.next()) {
				Estado e = new Estado();
				e.setId(rs.getInt("id_uf"));
				e.setNomeEstado(rs.getString("nm_estado"));
				e.setSgUf(rs.getString("sg_uf"));
				
				Cidade c = new Cidade();
				c.setIdCidade(rs.getInt("id_cidade"));
				c.setNomeCidade(rs.getString("nm_cidade"));
				c.setEstado(e);
				
				b.setIdBairro(rs.getInt("id_bairro"));
				b.setNomeBairro(rs.getString("nm_bairro"));
				b.setCep(rs.getString("cep"));
				b.setCidade(c);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return b;
	}

	public List<Bairro> listAll() {
		List<Bairro> Bairros = new ArrayList<Bairro>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement("SELECT b.id_bairro, b.nm_bairro, b.cep, c.id_cidade, c.nm_cidade, e.id_uf, e.nm_estado, e.sg_uf FROM bairro b JOIN cidade c ON b.cd_cidade = c.id_cidade JOIN estado e ON c.cd_uf = e.id_uf");
			rs = ps.executeQuery();
			while (rs.next()) {
				Estado e = new Estado();
				e.setId(rs.getInt("id_uf"));
				e.setNomeEstado(rs.getString("nm_estado"));
				e.setSgUf(rs.getString("sg_uf"));
				
				Cidade c = new Cidade();
				c.setIdCidade(rs.getInt("id_cidade"));
				c.setNomeCidade(rs.getString("nm_cidade"));
				c.setEstado(e);
				
				Bairro b = new Bairro();
				b.setIdBairro(rs.getInt("id_bairro"));
				b.setNomeBairro(rs.getString("nm_bairro"));
				b.setCep(rs.getString("cep"));
				b.setCidade(c);
				
				Bairros.add(b);
			}
			con.close();
		} catch (SQLException b) {
			b.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return Bairros;
	}

	public Boolean insert(Bairro b) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement(
						"INSERT INTO bairro (nm_bairro, cep, cd_cidade) VALUES (?, ?, ?) RETURNING id_bairro");
				ps.setString(1, b.getNomeBairro());
				ps.setString(2, b.getCep());
				ps.setInt(3, b.getCidade().getIdCidade());
				
				rs = ps.executeQuery();
				rs.next();
				b.setIdBairro(rs.getInt("id_bairro"));
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

	public Boolean update(Bairro b) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("UPDATE bairro SET nm_bairro = ?, cep = ?, cd_cidade = ? WHERE id_bairro = ?");
				ps.setString(1, b.getNomeBairro());
				ps.setString(2, b.getCep());
				ps.setInt(3, b.getCidade().getIdCidade());
				ps.setInt(4, b.getIdBairro());

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

	public Boolean delete(Bairro b) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("DELETE FROM bairro WHERE id_bairro = ?");
				ps.setInt(1, b.getIdBairro());
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
