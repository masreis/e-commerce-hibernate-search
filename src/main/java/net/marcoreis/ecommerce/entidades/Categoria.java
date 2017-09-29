package net.marcoreis.ecommerce.entidades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Indexed
@NamedQuery(name = "categoria.consultaPeloNome", query = "select c from Categoria c where c.nome like :nome")
public class Categoria implements Serializable {
	private static final long serialVersionUID = 6833139035296224500L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	private String nome;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		Categoria other = (Categoria) obj;
		return getId() == other.getId();
	}

}
