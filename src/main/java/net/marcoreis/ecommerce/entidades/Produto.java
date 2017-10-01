package net.marcoreis.ecommerce.entidades;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.EncodingType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TikaBridge;

import net.marcoreis.ecommerce.util.BigDecimalNumericFieldBridge;
import net.marcoreis.ecommerce.util.IPersistente;

@Indexed
@Entity
@NamedQueries({
		@NamedQuery(name = "produto.consultaTotal",
				query = "select count(p) from Produto p"),
		@NamedQuery(name = "produto.consultaPorDescricao",
				query = "select p from Produto p where p.descricao like :descricaoParcial"),
		@NamedQuery(name = "produto.consultaPorIntervaloPreco",
				query = "select p from Produto p where p.preco >= ?1 and p.preco <= ?2") })
@Analyzer(impl = BrazilianAnalyzer.class)
public class Produto implements IPersistente {
	private static final long serialVersionUID =
			3206252406240046848L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Field(store = Store.YES)
	private String nome;

	@Field(store = Store.YES)
	private String descricao;

	@Column(precision = 10, scale = 2)
	@Field(store = Store.YES)
	@NumericField
	@FieldBridge(impl = BigDecimalNumericFieldBridge.class)
	private BigDecimal preco;

	@Field(store = Store.YES)
	private boolean ativo;

	@Lob
	@Column(length = 1024 * 1024 * 5)
	@Field(store = Store.YES)
	@TikaBridge
	private byte[] especificacaoFabricante;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ProdutoCategoria",
			joinColumns = @JoinColumn(name = "produto_id"),
			inverseJoinColumns = @JoinColumn(
					name = "categoria_id"))
	@IndexedEmbedded
	private Set<Categoria> categorias =
			new HashSet<Categoria>(0);

	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, store = Store.YES)
	@DateBridge(resolution = Resolution.SECOND,
			encoding = EncodingType.STRING)
	private Date dataAtualizacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public byte[] getEspecificacaoFabricante() {
		return especificacaoFabricante;
	}

	public void setEspecificacaoFabricante(
			byte[] especificacaoFabricante) {
		this.especificacaoFabricante = especificacaoFabricante;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public void setCategorias(Set<Categoria> categorias) {
		this.categorias = categorias;
	}

	public Set<Categoria> getCategorias() {
		return categorias;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public boolean isAtivo() {
		return ativo;
	}

}
