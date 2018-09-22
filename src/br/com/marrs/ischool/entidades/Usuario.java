package br.com.marrs.ischool.entidades;

import java.util.Date;
import java.util.List;

import br.com.marrs.ischool.util.Constantes;

/**
 * @author Daniel Souza de lima e-mail:daniesouza@gmail.com
 * 
 */
 

public class Usuario implements Entidade{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1611809499291124578L;


    private Long idUsuario;
    
	private String usuario;
	
	private String nome;
	
	private String senha;
	
	private String endereco;
	
	private String autoridade;
	
	private String telefone;

	private Date dtCad;
	
	private String rg;
	
	private Long cpf;
	
	private String email;
	
	private Boolean ativo;
	
	private Date dataUltimoAcesso;
	
	private Date dataAcessoAtual;	
		
	private Date dataSenha;
	
	private Boolean trocarSenha;
	
	private Byte tentativasSenhaIncorreta;
	
	private List<Mensagem> mensagems;
		
	private Cliente cliente;
	
    private List<Classe> classes;
	
	private List<EventoExecutado> eventosExecutados;
	
	private List<Aluno> alunos;
	
	private List<DeviceRegId> devicesRegistrados;
	
	private String securityHashKey;
	
	private Long dataValidadeHash;
	
	private Long dataAtualizacao;
	
	private Long deviceId;
	
	private Boolean semClassesAtribuidas;
	
	@Override
	public Long getId() {
		return this.getIdUsuario();
	}

	@Override
	public void setId(Long id) {
		this.setIdUsuario(id);
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}


	public void setSenha(String senha) {
		this.senha = senha;
	}


	public String getEndereco() {
		return endereco;
	}


	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}


	public String getAutoridade() {
		return autoridade;
	}


	public void setAutoridade(String autoridade) {
		this.autoridade = autoridade;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getSecurityHashKey() {
		return securityHashKey;
	}

	public void setSecurityHashKey(String securityHashKey) {
		this.securityHashKey = securityHashKey;
	}
	
	public Long getDataValidadeHash() {
		return dataValidadeHash;
	}

	public void setDataValidadeHash(Long dataValidadeHash) {
		this.dataValidadeHash = dataValidadeHash;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idUsuario == null) ? 0 : idUsuario.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		Usuario other = (Usuario) obj;
		if (idUsuario == null) {
			if (other.idUsuario != null)
				return false;
		} else if (!idUsuario.equals(other.idUsuario))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

	

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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

	public Date getDataUltimoAcesso() {
		return dataUltimoAcesso;
	}

	public void setDataUltimoAcesso(Date dataUltimoAcesso) {
		this.dataUltimoAcesso = dataUltimoAcesso;
	}

	public Date getDataAcessoAtual() {
		return dataAcessoAtual;
	}

	public void setDataAcessoAtual(Date dataAcessoAtual) {
		this.dataAcessoAtual = dataAcessoAtual;
	}

	public Date getDataSenha() {
		return dataSenha;
	}

	public void setDataSenha(Date dataSenha) {
		this.dataSenha = dataSenha;
	}

	public Boolean getTrocarSenha() {
		return trocarSenha;
	}

	public void setTrocarSenha(Boolean trocarSenha) {
		this.trocarSenha = trocarSenha;
	}

	public Byte getTentativasSenhaIncorreta() {
		return tentativasSenhaIncorreta;
	}

	public void setTentativasSenhaIncorreta(Byte tentativasSenhaIncorreta) {
		this.tentativasSenhaIncorreta = tentativasSenhaIncorreta;
	}

	public  String getAdministrador() {
		return Constantes.ADMINISTRADOR;
	}

	public  String getAdminCliente() {
		return Constantes.ADMIN_CLIENTE;
	}

	public  String getProfessor() {
		return Constantes.PROFESSOR;
	}
	
	public  String getResponsavel() {
		return Constantes.RESPONSAVEL;
	}

	public List<Classe> getClasses() {
		return classes;
	}

	public void setClasses(List<Classe> classes) {
		this.classes = classes;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public List<EventoExecutado> getEventosExecutados() {
		return eventosExecutados;
	}

	public void setEventosExecutados(List<EventoExecutado> eventosExecutados) {
		this.eventosExecutados = eventosExecutados;
	}

	public List<DeviceRegId> getDevicesRegistrados() {
		return devicesRegistrados;
	}

	public void setDevicesRegistrados(List<DeviceRegId> devicesRegistrados) {
		this.devicesRegistrados = devicesRegistrados;
	}

	public List<Mensagem> getMensagems() {
		return mensagems;
	}

	public void setMensagems(List<Mensagem> mensagems) {
		this.mensagems = mensagems;
	}

	public Long getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Long dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Boolean getSemClassesAtribuidas() {
		return semClassesAtribuidas;
	}

	public void setSemClassesAtribuidas(Boolean semClassesAtribuidas) {
		this.semClassesAtribuidas = semClassesAtribuidas;
	}
	
	


}
