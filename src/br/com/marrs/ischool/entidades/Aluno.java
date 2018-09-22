package br.com.marrs.ischool.entidades;

import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;


/**
 * @author Daniel Souza de lima e-mail:daniesouza@gmail.com
 * 
 */

public class Aluno implements Entidade{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4177648884923803942L;

	

    private Long idAluno;
	
	private String codigoAluno;
	
	private Cliente cliente;
		
    private List<Classe> classes;
		
    private Usuario pai;
		
	private List<EventoExecutado> eventosExecutados;
		
	private List<Mensagem> mensagems;
		
	private String nome;
		
	private String rg;
		
	private Long cpf;		
	
	private String endereco;
		
	private String telefone;
		
	private String observacoes;
		
	private Double nota;
	
	private Integer faltas;
	
	private Date dataCadastro;
	
	private Date dataNascimento;
	
	private String email;
	
	private Boolean ativo;
	
	private Bitmap foto;
	
	private String mensagemDescricaoEvento;
	
	private int quantidadeEventosNovos;
	
	private String mensagemDescricaoMensagem;
	
	private int quantidadeMensagensNovas;
	
	private Boolean semClassesAtribuidas;
		
	@Override
	public Long getId() {
		return this.getIdAluno();
	}

	@Override
	public void setId(Long id) {
		this.setIdAluno(id);		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoAluno == null) ? 0 : codigoAluno.hashCode());
		result = prime * result + ((idAluno == null) ? 0 : idAluno.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aluno other = (Aluno) obj;
		if (codigoAluno == null) {
			if (other.codigoAluno != null)
				return false;
		} else if (!codigoAluno.equals(other.codigoAluno))
			return false;
		if (idAluno == null) {
			if (other.idAluno != null)
				return false;
		} else if (!idAluno.equals(other.idAluno))
			return false;
		return true;
	}

	public Long getIdAluno() {
		return idAluno;
	}
	public void setIdAluno(Long idAluno) {
		this.idAluno = idAluno;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Usuario getPai() {
		return pai;
	}

	public void setPai(Usuario pai) {
		this.pai = pai;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCodigoAluno() {
		return codigoAluno;
	}

	public void setCodigoAluno(String codigoAluno) {
		this.codigoAluno = codigoAluno;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Double getNota() {
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}

	public Integer getFaltas() {
		return faltas;
	}

	public void setFaltas(Integer faltas) {
		this.faltas = faltas;
	}

	public List<Classe> getClasses() {
		return classes;
	}

	public void setClasses(List<Classe> classes) {
		this.classes = classes;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<EventoExecutado> getEventosExecutados() {
		return eventosExecutados;
	}

	public void setEventosExecutados(List<EventoExecutado> eventosExecutados) {
		this.eventosExecutados = eventosExecutados;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public List<Mensagem> getMensagems() {
		return mensagems;
	}

	public void setMensagems(List<Mensagem> mensagems) {
		this.mensagems = mensagems;
	}

	public int getQuantidadeEventosNovos() {
		return quantidadeEventosNovos;
	}

	public void setQuantidadeEventosNovos(int quantidadeEventosNovos) {
		this.quantidadeEventosNovos = quantidadeEventosNovos;
	}

	public Bitmap getFoto() {
		return foto;
	}

	public void setFoto(Bitmap foto) {
		this.foto = foto;
	}

	public String getMensagemDescricaoEvento() {
		return mensagemDescricaoEvento;
	}

	public void setMensagemDescricaoEvento(String mensagemDescricaoEvento) {
		this.mensagemDescricaoEvento = mensagemDescricaoEvento;
	}

	public String getMensagemDescricaoMensagem() {
		return mensagemDescricaoMensagem;
	}

	public void setMensagemDescricaoMensagem(String mensagemDescricaoMensagem) {
		this.mensagemDescricaoMensagem = mensagemDescricaoMensagem;
	}

	public int getQuantidadeMensagensNovas() {
		return quantidadeMensagensNovas;
	}

	public void setQuantidadeMensagensNovas(int quantidadeMensagensNovas) {
		this.quantidadeMensagensNovas = quantidadeMensagensNovas;
	}

	public Boolean getSemClassesAtribuidas() {
		return semClassesAtribuidas;
	}

	public void setSemClassesAtribuidas(Boolean semClassesAtribuidas) {
		this.semClassesAtribuidas = semClassesAtribuidas;
	}



	
}
