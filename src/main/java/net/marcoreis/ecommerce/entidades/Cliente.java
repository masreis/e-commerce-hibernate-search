package net.marcoreis.ecommerce.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
<<<<<<< HEAD
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
=======
import org.hibernate.search.annotations.EncodingType;
import org.hibernate.search.annotations.Field;
>>>>>>> 6eb07e45198f33a0f11dcdd98a755324151e7d53
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

<<<<<<< HEAD
@Entity
@NamedQuery(name = "usuario.consultaAcessoDia", query = "select c from Cliente c where c.ultimoLogin = :data")
@Indexed
public class Cliente implements Serializable {
	private static final long serialVersionUID = 5073165906294726127L;
=======
import net.marcoreis.ecommerce.util.IPersistente;

@Entity
@Indexed
@NamedQuery(name = "usuario.consultaAcessoDia",
		query = "select c from Cliente c where c.ultimoLogin = :data")
public class Cliente implements IPersistente {
	private static final long serialVersionUID =
			5073165906294726127L;
>>>>>>> 6eb07e45198f33a0f11dcdd98a755324151e7d53

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
<<<<<<< HEAD
	@Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
	private String email;
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	private String nome;
	@Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
	@DateBridge(resolution = Resolution.MINUTE)
	private Date ultimoLogin;
	@Column(unique = true, nullable = false)
	@Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
=======

	@Field(analyze = Analyze.NO, store = Store.YES)
	private String email;

	@Field(store = Store.YES)
	private String nome;

	@Field(analyze = Analyze.NO, store = Store.YES)
	@DateBridge(resolution = Resolution.SECOND,
			encoding = EncodingType.STRING)
	private Date ultimoLogin;

	@Column(unique = true, nullable = false)
	@Field(store = Store.YES)
>>>>>>> 6eb07e45198f33a0f11dcdd98a755324151e7d53
	private String cpfCnpj;

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setUltimoLogin(Date ultimoLogin) {
		this.ultimoLogin = ultimoLogin;
	}

	public Date getUltimoLogin() {
		return ultimoLogin;
	}
}
