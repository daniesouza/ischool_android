package br.com.marrs.ischool.entidades;

import java.util.Date;
import java.util.List;



import br.com.marrs.ischool.util.Constantes;

/**
 * @author Daniel Souza de lima e-mail:daniesouza@gmail.com
 * 
 */


public class Evento implements Entidade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8848895107385196042L;


	private Long idEvento;

	private String codigoEvento;

	private String nome;

	private String icone;

	private Date dataCadastro;

	private List<EventoExecutado> eventosExecutados;


	private Cliente cliente;

	private Boolean ativo;

	private Boolean preCadastro;

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

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
		Evento other = (Evento) obj;
		if (idEvento == null) {
			if (other.idEvento != null)
				return false;
		} else if (!idEvento.equals(other.idEvento))
			return false;
		return true;
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

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getCodigoEvento() {
		return codigoEvento;
	}

	public void setCodigoEvento(String codigoEvento) {
		this.codigoEvento = codigoEvento;
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

	public Boolean getPreCadastro() {
		return preCadastro;
	}

	public void setPreCadastro(Boolean preCadastro) {
		this.preCadastro = preCadastro;
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

	public int getTipoInformativo() {
		return Constantes.TIPO_INFORMATIVO;
	}

	public int getTipoInicioFim() {
		return Constantes.TIPO_INICIO_FIM;
	}

	public int getTipoAlimento() {
		return Constantes.TIPO_ALIMENTO;
	}

	public int getTipoEvacuacao() {
		return Constantes.TIPO_EVACUACAO;
	}

	public int getTipoEnviar() {
		return Constantes.TIPO_LEMBRETE;
	}
	
	public int getTipoMedicamento() {
		return Constantes.TIPO_MEDICAMENTO;
	}
}
