package br.com.marrs.ischool.entidades;




public class Notificacao  implements Entidade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2590585362255340649L;


    private Long idNotificacao;
	

    private Usuario usuario;
	
 
    private Aluno aluno;
    
	private Integer quantidadeNotificacoes;
	
	private String nomeClasse;
	
	private String nomeRemetente;

	@Override
	public Long getId() {
		return this.getIdNotificacao();
	}

	@Override
	public void setId(Long id) {
		this.setIdNotificacao(id);	
	}


	public Long getIdNotificacao() {
		return idNotificacao;
	}


	public void setIdNotificacao(Long idNotificacao) {
		this.idNotificacao = idNotificacao;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public Aluno getAluno() {
		return aluno;
	}


	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}


	public Integer getQuantidadeNotificacoes() {
		return quantidadeNotificacoes;
	}


	public void setQuantidadeNotificacoes(Integer quantidadeNotificacoes) {
		this.quantidadeNotificacoes = quantidadeNotificacoes;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getNomeClasse() {
		return nomeClasse;
	}

	public void setNomeClasse(String nomeClasse) {
		this.nomeClasse = nomeClasse;
	}
	
	public String getNomeRemetente() {
		return nomeRemetente;
	}

	public void setNomeRemetente(String nomeRemetente) {
		this.nomeRemetente = nomeRemetente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idNotificacao == null) ? 0 : idNotificacao.hashCode());
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
		Notificacao other = (Notificacao) obj;
		if (idNotificacao == null) {
			if (other.idNotificacao != null)
				return false;
		} else if (!idNotificacao.equals(other.idNotificacao))
			return false;
		return true;
	}

	



}


