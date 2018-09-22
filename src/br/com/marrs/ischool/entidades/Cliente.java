package br.com.marrs.ischool.entidades;

import java.util.Date;
import java.util.List;



/**
 * @author Daniel Souza de lima e-mail:daniesouza@gmail.com
 * 
 */


public class Cliente implements Entidade{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1654440848232809511L;
	

    private Long idCliente;
	
	private Long cnpj;
    
	private String razaoSocial;
	
	private String nome;
	
	private String endereco;
	
	private String telefone;

	private Date dtCad;
	
	private String email;
	
	private Boolean ativo;
	
	private List<Mensagem> mensagems;

	private List<Usuario> usuarios;
	
	private List<Aluno> alunos;
	
	private List<Evento> eventos;
	
	private List<Classe> classes;

	private List<EventoExecutado> eventosExecutados;

	@Override
	public Long getId() {
		
		return this.getIdCliente();
	}

	@Override
	public void setId(Long id) {
		
		this.setIdCliente(id);	
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
		result = prime * result
				+ ((idCliente == null) ? 0 : idCliente.hashCode());
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
		Cliente other = (Cliente) obj;
		if (cnpj == null) {
			if (other.cnpj != null)
				return false;
		} else if (!cnpj.equals(other.cnpj))
			return false;
		if (idCliente == null) {
			if (other.idCliente != null)
				return false;
		} else if (!idCliente.equals(other.idCliente))
			return false;
		return true;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	public List<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	public List<Classe> getClasses() {
		return classes;
	}

	public void setClasses(List<Classe> classes) {
		this.classes = classes;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getCnpj() {
		return cnpj;
	}

	public void setCnpj(Long cnpj) {
		this.cnpj = cnpj;
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

	public Date getDtCad() {
		return dtCad;
	}

	public void setDtCad(Date dtCad) {
		this.dtCad = dtCad;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<EventoExecutado> getEventosExecutados() {
		return eventosExecutados;
	}

	public void setEventosExecutados(List<EventoExecutado> eventosExecutados) {
		this.eventosExecutados = eventosExecutados;
	}

	public List<Mensagem> getMensagems() {
		return mensagems;
	}

	public void setMensagems(List<Mensagem> mensagems) {
		this.mensagems = mensagems;
	}

	

}
