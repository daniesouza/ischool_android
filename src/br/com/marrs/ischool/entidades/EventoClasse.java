package br.com.marrs.ischool.entidades;



public class EventoClasse implements Entidade{
	



	/**
	 * 
	 */
	private static final long serialVersionUID = -6339846591884324007L;


    private Long idEventoClasse;
	

	private Evento evento;


	private Classe classe;
	
	private Integer periodoEvento;

	
	public Long getIdEventoClasse() {
		return idEventoClasse;
	}

	public void setIdEventoClasse(Long idEventoClasse) {
		this.idEventoClasse = idEventoClasse;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}


	@Override
	public Long getId() {
		
		return this.getIdEventoClasse();
	}

	@Override
	public void setId(Long id) {
		this.setIdEventoClasse(id);		
	}

	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getPeriodoEvento() {
		return periodoEvento;
	}

	public void setPeriodoEvento(Integer periodoEvento) {
		this.periodoEvento = periodoEvento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classe == null) ? 0 : classe.hashCode());
		result = prime * result + ((evento == null) ? 0 : evento.hashCode());
		result = prime * result
				+ ((periodoEvento == null) ? 0 : periodoEvento.hashCode());
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
		EventoClasse other = (EventoClasse) obj;
		if (classe == null) {
			if (other.classe != null)
				return false;
		} else if (!classe.equals(other.classe))
			return false;
		if (evento == null) {
			if (other.evento != null)
				return false;
		} else if (!evento.equals(other.evento))
			return false;
		if (periodoEvento == null) {
			if (other.periodoEvento != null)
				return false;
		} else if (!periodoEvento.equals(other.periodoEvento))
			return false;
		return true;
	}



}
