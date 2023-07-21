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
import model.Cliente;
import model.Estado;
import model.Venda;
import util.DbUtil;

public class VendaDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private DataSource ds;

	public Venda findById(Integer id) {
		Venda v = new Venda();
		ClienteDAO c = new ClienteDAO();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement("SELECT v.id_nota, v.dt_nota, v.vl_total, v.cd_cliente, v.cd_cidade_rota FROM venda v WHERE v.id_nota = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				v.setIdNota(rs.getInt("id_nota"));
				v.setDtVenda(rs.getString("dt_nota"));
				v.setVlTotal(rs.getDouble("vl_total"));
				v.setCdCliente(c.findById(rs.getInt("cd_cliente")));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return v;
	}

	public List<Venda> listAll() {
		List<Venda> Vendas = new ArrayList<Venda>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement("SELECT v.id_nota, v.dt_nota, v.vl_total, v.cd_cliente, c.nm_cliente, v.cd_cidade_rota, cid.nm_cidade, uf.sg_uf  FROM venda v JOIN cliente c ON v.cd_cliente = c.id_cliente JOIN cidades_rota cr ON v.cd_cidade_rota = cr.id_cidade_rota JOIN cidade cid ON cr.cd_cidade = cid.id_cidade JOIN estado uf ON cid.cd_uf = uf.id_uf");
			rs = ps.executeQuery();
			while (rs.next()) {
				Estado e = new Estado();
				e.setSgUf(rs.getString("sg_uf"));
				
				Cidade ci = new Cidade();
				ci.setNomeCidade(rs.getString("nm_cidade"));
				ci.setEstado(e);
				
				Cliente cli = new Cliente();
				cli.setIdCliente(rs.getInt("cd_cliente"));
				cli.setNomeCliente(rs.getString("nm_cliente"));
				
				CidadesRota c = new CidadesRota();
				c.setIdCidadeRota(rs.getInt("cd_cidade_rota"));
				c.setCidade(ci);
				
				Venda v = new Venda();
				v.setIdNota(rs.getInt("id_nota"));
				v.setDtVenda(rs.getString("dt_nota"));
				v.setVlTotal(rs.getDouble("vl_total"));
				v.setCdCliente(cli);
				v.setCidadesRota(c);  
				Vendas.add(v);
			}
			con.close();
		} catch (SQLException v) {
			v.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return Vendas;
	}

	public Boolean insert(Venda v) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement(
						"INSERT INTO venda (dt_nota, vl_total, cd_cliente, cd_cidade_rota) VALUES (?, ?, ?, ?) RETURNING id_nota");
				ps.setString(1, v.getDtVenda());
				ps.setDouble(2, v.getVlTotal());
				ps.setInt(3, v.getCdCliente().getIdCliente());
				ps.setInt(4, v.getCidadesRota().getIdCidadeRota());
				
				rs = ps.executeQuery();
				rs.next();
				v.setIdNota(rs.getInt("id_nota"));
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

	public Boolean update(Venda v) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("UPDATE venda SET dt_nota = ?, vl_total = ?, cd_cliente = ?, cd_cidade_rota = ? WHERE id_nota = ?");
				ps.setString(1, v.getDtVenda());
				ps.setDouble(2, v.getVlTotal());
				ps.setInt(3, v.getCdCliente().getIdCliente());
				ps.setInt(4, v.getCidadesRota().getIdCidadeRota());
				ps.setInt(5, v.getIdNota());

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

	public Boolean delete(Venda v) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("DELETE FROM venda WHERE id_nota = ?");
				ps.setInt(1, v.getIdNota());
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
