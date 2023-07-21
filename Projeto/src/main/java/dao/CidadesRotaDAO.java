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
import model.CidadesRota;
import model.Estado;
import model.Rota;
import util.DbUtil;

public class CidadesRotaDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private DataSource ds;

	public CidadesRota findById(Integer id) {
		CidadesRota c = new CidadesRota();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement("SELECT c.id_cidade_rota, c.cd_rota, r.nm_rota, c.cd_cidade, ci.nm_cidade, e.sg_uf FROM cidades_rota c JOIN rota r ON c.cd_rota = r.id_rota JOIN cidade ci ON c.cd_cidade = ci.id_cidade JOIN estado e ON ci.cd_uf = e.id_uf WHERE c.id_cidade_rota = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				Estado e = new Estado();
				e.setSgUf(rs.getString("sg_uf"));
				
				Cidade ci = new Cidade();
				ci.setIdCidade(rs.getInt("cd_cidade"));
				ci.setNomeCidade(rs.getString("nm_cidade"));
				ci.setEstado(e);
				
				Rota r = new Rota();
				r.setIdRota(rs.getInt("cd_rota"));
				r.setNmRota(rs.getString("nm_rota"));

				c.setIdCidadeRota(rs.getInt("id_cidade_rota"));
				c.setRota(r);
				c.setCidade(ci);  
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

	public List<CidadesRota> listAll() {
		List<CidadesRota> CidadesRotas = new ArrayList<CidadesRota>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement("SELECT c.id_cidade_rota, c.cd_rota, r.nm_rota, c.cd_cidade, ci.nm_cidade, e.sg_uf FROM cidades_rota c JOIN rota r ON c.cd_rota = r.id_rota JOIN cidade ci ON c.cd_cidade = ci.id_cidade JOIN estado e ON ci.cd_uf = e.id_uf");
			rs = ps.executeQuery();
			while (rs.next()) {
				Estado e = new Estado();
				e.setSgUf(rs.getString("sg_uf"));
				
				Cidade ci = new Cidade();
				ci.setIdCidade(rs.getInt("cd_cidade"));
				ci.setNomeCidade(rs.getString("nm_cidade"));
				ci.setEstado(e);
				
				Rota r = new Rota();
				r.setIdRota(rs.getInt("cd_rota"));
				r.setNmRota(rs.getString("nm_rota"));

				CidadesRota c = new CidadesRota();
				c.setIdCidadeRota(rs.getInt("id_cidade_rota"));
				c.setRota(r);
				c.setCidade(ci);  
				CidadesRotas.add(c);
			}
			con.close();
		} catch (SQLException b) {
			b.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return CidadesRotas;
	}

	public Boolean insert(CidadesRota c) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement(
						"INSERT INTO cidades_rota (cd_rota, cd_cidade) VALUES (?, ?) RETURNING id_cidade_rota");
				ps.setInt(1, c.getRota().getIdRota());
				ps.setInt(2, c.getCidade().getIdCidade());
				
				rs = ps.executeQuery();
				rs.next();
				c.setIdCidadeRota(rs.getInt("id_cidade_rota"));
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

	public Boolean update(CidadesRota c) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("UPDATE cidades_rota SET cd_rota = ?, cd_cidade = ? WHERE id_cidade_rota = ?");
				ps.setInt(1, c.getRota().getIdRota());
				ps.setInt(2, c.getCidade().getIdCidade());
				ps.setInt(3, c.getIdCidadeRota());

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

	public Boolean delete(CidadesRota c) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("DELETE FROM cidades_rota WHERE id_cidade_rota = ?");
				ps.setInt(1, c.getIdCidadeRota());
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
