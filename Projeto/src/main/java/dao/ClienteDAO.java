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

import model.Bairro;
import model.Cidade;
import model.Cliente;
import model.Estado;
import util.DbUtil;

public class ClienteDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private DataSource ds;

	public Cliente findById(Integer id) {
		Cliente c = new Cliente();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement(
					"SELECT c.id_cliente, c.nm_cliente, c.tp_pessoa, c.cpf_cnpj, c.telefone, c.logradouro, c.nr_residencial, c.email, b.id_bairro, b.nm_bairro, b.cep, ci.id_cidade, ci.nm_cidade, e.id_uf, e.nm_estado, e.sg_uf FROM cliente c JOIN bairro b ON c.cd_bairro = b.id_bairro JOIN cidade ci ON b.cd_cidade = ci.id_cidade JOIN estado e ON ci.cd_uf = e.id_uf WHERE c.id_cliente = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {			
				Estado uf = new Estado();
				uf.setId(rs.getInt("id_uf"));
				uf.setNomeEstado(rs.getString("nm_estado"));
				uf.setSgUf(rs.getString("sg_uf"));
				
				Cidade ci = new Cidade();
				ci.setIdCidade(rs.getInt("id_cidade"));
				ci.setNomeCidade(rs.getString("nm_cidade"));
				ci.setEstado(uf);
				
				Bairro b = new Bairro();
				b.setIdBairro(rs.getInt("id_bairro"));
				b.setNomeBairro(rs.getString("nm_bairro"));
				b.setCep(rs.getString("cep"));
				b.setCidade(ci);				
				
				c.setIdCliente(rs.getInt("id_cliente"));
				c.setNomeCliente(rs.getString("nm_cliente"));
				c.setTpPessoa(rs.getString("tp_pessoa"));
				c.setCpfCnpj(rs.getString("cpf_cnpj"));
				c.setTelefone(rs.getString("telefone"));
				c.setLogradouro(rs.getString("logradouro"));
				c.setNrResidencial(rs.getString("nr_residencial"));
				c.setEmail(rs.getString("email"));
				c.setBairro(b);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return c;
	}

	public Cliente findByName(String name) {
		Cliente c = new Cliente();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement(
					"SELECT c.id_cliente, c.nm_cliente, c.tp_pessoa, c.cpf_cnpj, c.telefone, c.logradouro, c.nr_residencial, c.email, b.id_bairro, b.nm_bairro, b.cep, ci.id_cidade, ci.nm_cidade, e.id_uf, e.nm_estado, e.sg_uf FROM cliente c JOIN bairro b ON c.cd_bairro = b.id_bairro JOIN cidade ci ON b.cd_cidade = ci.id_cidade JOIN estado e ON ci.cd_uf = e.id_uf WHERE c.nm_cliente ILIKE '%?%'");
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (rs.next()) {
				Estado uf = new Estado();
				uf.setId(rs.getInt("id_uf"));
				uf.setNomeEstado(rs.getString("nm_estado"));
				uf.setSgUf(rs.getString("sg_uf"));
				
				Cidade ci = new Cidade();
				ci.setIdCidade(rs.getInt("id_cidade"));
				ci.setNomeCidade(rs.getString("nm_cidade"));
				ci.setEstado(uf);
				
				Bairro b = new Bairro();
				b.setIdBairro(rs.getInt("id_bairro"));
				b.setNomeBairro(rs.getString("nm_bairro"));
				b.setCep(rs.getString("cep"));
				b.setCidade(ci);				
				
				c.setIdCliente(rs.getInt("id_cliente"));
				c.setNomeCliente(rs.getString("nm_cliente"));
				c.setTpPessoa(rs.getString("tp_pessoa"));
				c.setCpfCnpj(rs.getString("cpf_cnpj"));
				c.setTelefone(rs.getString("telefone"));
				c.setLogradouro(rs.getString("logradouro"));
				c.setNrResidencial(rs.getString("nr_residencial"));
				c.setEmail(rs.getString("email"));
				c.setBairro(b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return c;
	}

	public List<Cliente> listAll() {
		List<Cliente> Cliente = new ArrayList<Cliente>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			ps = con.prepareStatement(
					"SELECT c.id_cliente, c.nm_cliente, c.tp_pessoa, c.cpf_cnpj, c.telefone, c.logradouro, c.nr_residencial, c.email, b.id_bairro, b.nm_bairro, b.cep, ci.id_cidade, ci.nm_cidade, e.id_uf, e.nm_estado, e.sg_uf FROM cliente c JOIN bairro b ON c.cd_bairro = b.id_bairro JOIN cidade ci ON b.cd_cidade = ci.id_cidade JOIN estado e ON ci.cd_uf = e.id_uf");
			rs = ps.executeQuery();
			while (rs.next()) {
				Estado uf = new Estado();
				uf.setId(rs.getInt("id_uf"));
				uf.setNomeEstado(rs.getString("nm_estado"));
				uf.setSgUf(rs.getString("sg_uf"));
				
				Cidade ci = new Cidade();
				ci.setIdCidade(rs.getInt("id_cidade"));
				ci.setNomeCidade(rs.getString("nm_cidade"));
				ci.setEstado(uf);
				
				Bairro b = new Bairro();
				b.setIdBairro(rs.getInt("id_bairro"));
				b.setNomeBairro(rs.getString("nm_bairro"));
				b.setCep(rs.getString("cep"));
				b.setCidade(ci);			
				
				Cliente c = new Cliente();
				c.setIdCliente(rs.getInt("id_cliente"));
				c.setNomeCliente(rs.getString("nm_cliente"));
				c.setTpPessoa(rs.getString("tp_pessoa"));
				c.setCpfCnpj(rs.getString("cpf_cnpj"));
				c.setTelefone(rs.getString("telefone"));
				c.setLogradouro(rs.getString("logradouro"));
				c.setNrResidencial(rs.getString("nr_residencial"));
				c.setEmail(rs.getString("email"));
				c.setBairro(b);
				
				Cliente.add(c);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return Cliente;
	}

	public Boolean insert(Cliente c) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("INSERT INTO public.cliente(nm_cliente, tp_pessoa, cpf_cnpj, telefone, logradouro, nr_residencial, cd_bairro, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_cliente");
				ps.setString(1, c.getNomeCliente());
				ps.setString(2, c.getTpPessoa());
				ps.setString(3, c.getCpfCnpj());
				ps.setString(4, c.getTelefone());
				ps.setString(5, c.getLogradouro());
				ps.setString(6, c.getNrResidencial());
				ps.setInt(7, c.getBairro().getIdBairro());
				ps.setString(8, c.getEmail());

				rs = ps.executeQuery();
				rs.next();
				c.setIdCliente(rs.getInt("id_cliente"));
				con.commit();
				resultado = true;
			} catch (SQLException e) {
				e.printStackTrace();
				con.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return resultado;
	}

	public Boolean update(Cliente c) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement(
						"UPDATE public.cliente SET nm_cliente=?, tp_pessoa=?, cpf_cnpj=?, telefone=?, logradouro=?, nr_residencial=?, cd_bairro=?, email=? WHERE id_cliente = ?");
				ps.setString(1, c.getNomeCliente());
				ps.setString(2, c.getTpPessoa());
				ps.setString(3, c.getCpfCnpj());
				ps.setString(4, c.getTelefone());
				ps.setString(5, c.getLogradouro());
				ps.setString(6, c.getNrResidencial());
				ps.setInt(7, c.getBairro().getIdBairro());
				ps.setString(8, c.getEmail());
				ps.setInt(9, c.getIdCliente());

				ps.execute();
				con.commit();
				resultado = true;
			} catch (SQLException e) {
				e.printStackTrace();
				con.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return resultado;
	}

	public Boolean delete(Cliente c) {
		Boolean resultado = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = this.ds.getConnection();
			con.setAutoCommit(false);
			try {
				ps = con.prepareStatement("DELETE FROM cliente WHERE id_cliente = ?");
				ps.setInt(1, c.getIdCliente());
				ps.execute();
				con.commit();
				resultado = true;
			} catch (SQLException e) {
				e.printStackTrace();
				con.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
		return resultado;
	}
}
