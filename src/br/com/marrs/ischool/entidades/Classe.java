package br.com.marrs.ischool.entidades;

import java.util.Date;
import java.util.List;


/**
 * @author Daniel Souza de lima e-mail:daniesouza@gmail.com
 * 
 */


public class Classe implements Entidade{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6544992143608965070L;
	

    private Long idClasse;
	
	private String codigoClasse;	

	private String nome;
	
	private String turma;
	

	private Cliente cliente;
	
	
	private List<EventoExecutado> eventosExecutados;
	
	private Date dataCadastro;
	
	private Integer ano;

	private List<Aluno> alunos;
	
	private List<Usuario> usuarios;	
	
	private Boolean ativo;
	

	@Override
	public Long getId() {
		return getIdClasse();
	}

	@Override
	public void setId(Long id) {
		this.setIdClasse(id);		
	}
	
	public Long getIdClasse() {
		return idClasse;
	}

	public void setIdClasse(Long idClasse) {
		this.idClasse = idClasse;
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

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}


	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTurma() {
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}
	public String getCodigoClasse() {
		return codigoClasse;
	}

	public void setCodigoClasse(String codigoClasse) {
		this.codigoClasse = codigoClasse;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoClasse == null) ? 0 : codigoClasse.hashCode());
		result = prime * result
				+ ((idClasse == null) ? 0 : idClasse.hashCode());
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
		Classe other = (Classe) obj;
		if (codigoClasse == null) {
			if (other.codigoClasse != null)
				return false;
		} else if (!codigoClasse.equals(other.codigoClasse))
			return false;
		if (idClasse == null) {
			if (other.idClasse != null)
				return false;
		} else if (!idClasse.equals(other.idClasse))
			return false;
		return true;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public List<EventoExecutado> getEventosExecutados() {
		return eventosExecutados;
	}

	public void setEventosExecutados(List<EventoExecutado> eventosExecutados) {
		this.eventosExecutados = eventosExecutados;
	}


	
	
}
