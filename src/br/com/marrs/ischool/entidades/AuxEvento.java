package br.com.marrs.ischool.entidades;


/**
 * @author Daniel Souza de lima e-mail:daniesouza@gmail.com
 * 
 */


public class AuxEvento implements Entidade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1175466417750556003L;


	private Long idEvento;

	private String codigoEvento;

	private String nome;

	private String icone;

	private Integer tipo;

	private String unidadeMedida;
	
	@Override
	public Long getId() {

		return this.getIdEvento();
	}

	@Override
	public void setId(Long id) {
		this.setIdEvento(id);
	}

	public Long getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(Long idEvento) {
		this.idEvento = idEvento;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCodigoEvento() {
		return codigoEvento;
	}

	public void setCodigoEvento(String codigoEvento) {
		this.codigoEvento = codigoEvento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIcone() {
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idEvento == null) ? 0 : idEvento.hashCode());
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
		AuxEvento other = (AuxEvento) obj;
		if (idEvento == null) {
			if (other.idEvento != null)
				return false;
		} else if (!idEvento.equals(other.idEvento))
			return false;
		return true;
	}

	
}
