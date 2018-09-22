package br.com.marrs.ischool.entidades;

import java.util.Date;

import br.com.marrs.ischool.util.Constantes;



public class EventoExecutado implements Entidade{



	/**
	 * 
	 */
	private static final long serialVersionUID = -8184498228538335153L;
	

    private Long idEventoExecutado;
	

	private Evento evento;
	

	private Cliente cliente;
	

	private Aluno aluno;
	

	private Usuario usuario;
	

	private Classe classe;
	
	private String observacoes;
	
	private Long quantidade;
	
	private Date dataCadastro;
	
	private Integer periodoEvento;

	private Integer tipo;
	
	private Date dataInicio;
	
	private Date dataFim;
	
	private Integer avaliacaoEvento;
	
	private Boolean tomouBanho;

	private String medicamentos;
	
	private Boolean enviarFralda;
	
	private Boolean enviarLencos;
	
	private Boolean enviarPomada;
	
	private Boolean enviarLeite;
	
	private String enviarOutros;
	
	private String icone;
	
    private Boolean lidoDevice;
    
    private Integer statusEventoExecutado;
    
    private Long dataAtualizacao;


	@Override
	public Long getId() {
		return this.getIdEventoExecutado();
	}
	@Override
	public void setId(Long id) {
		this.setIdEventoExecutado(id);
		
	}
	public Long getIdEventoExecutado() {
		return idEventoExecutado;
	}
	public void setIdEventoExecutado(Long idEventoExecutado) {
		this.idEventoExecutado = idEventoExecutado;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Aluno getAluno() {
		return aluno;
	}
	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Classe getClasse() {
		return classe;
	}
	public void setClasse(Classe classe) {
		this.classe = classe;
	}
	public String getObservacoes() {
		return observacoes;
	}
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	public Long getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Date getDataFim() {
		return dataFim;
	}
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}	
	public Boolean getLidoDevice() {
		return lidoDevice;
	}
	public void setLidoDevice(Boolean lidoDevice) {
		this.lidoDevice = lidoDevice;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idEventoExecutado == null) ? 0 : idEventoExecutado
						.hashCode());
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
		EventoExecutado other = (EventoExecutado) obj;
		if (idEventoExecutado == null) {
			if (other.idEventoExecutado != null)
				return false;
		} else if (!idEventoExecutado.equals(other.idEventoExecutado))
			return false;
		return true;
	}
	
	public  int getPeriodoManha() {
		return Constantes.PERIODO_MANHA;
	}

	public  int getPeriodoTarde() {
		return Constantes.PERIODO_TARDE;
	}
	public  int getPeriodoOutros() {
		return Constantes.PERIODO_OUTROS;
	}
	
	public String getIconeOk(){
		return Constantes.ICONE_OK;
	}
	
	public String getIconeWarning(){
		return Constantes.ICONE_WARNING;
	}

	public String getIconeNaoOk(){
		return Constantes.ICONE_NAO_OK;
	}
	
	public int getTipoInformativo(){
		return Constantes.TIPO_INFORMATIVO;
	}
	
	public int getTipoInicioFim(){
		return Constantes.TIPO_INICIO_FIM;
	}
	
	public int getTipoAlimento(){
		return Constantes.TIPO_ALIMENTO;
	}
	
	public int getTipoEvacuacao(){
		return Constantes.TIPO_EVACUACAO;
	}
	
	public int getTipoMedicamento(){
		return Constantes.TIPO_MEDICAMENTO;
	}
	
	public int getTipoLembrete(){
		return Constantes.TIPO_LEMBRETE;
	}
	
	public int getStatusCancelado(){
		return Constantes.STATUS_CANCELADO;
	}
	
	public int getStatusOk(){
		return Constantes.STATUS_OK;
	}
	
	public Integer getPeriodoEvento() {
		return periodoEvento;
	}
	public void setPeriodoEvento(Integer periodoEvento) {
		this.periodoEvento = periodoEvento;
	}
	public Integer getAvaliacaoEvento() {
		return avaliacaoEvento;
	}
	public void setAvaliacaoEvento(Integer avaliacaoEvento) {
		this.avaliacaoEvento = avaliacaoEvento;
	}
	public Boolean getTomouBanho() {
		return tomouBanho;
	}
	public void setTomouBanho(Boolean tomouBanho) {
		this.tomouBanho = tomouBanho;
	}
	public String getMedicamentos() {
		return medicamentos;
	}
	public void setMedicamentos(String medicamentos) {
		this.medicamentos = medicamentos;
	}
	public Boolean getEnviarFralda() {
		return enviarFralda;
	}
	public void setEnviarFralda(Boolean enviarFralda) {
		this.enviarFralda = enviarFralda;
	}
	public Boolean getEnviarLencos() {
		return enviarLencos;
	}
	public void setEnviarLencos(Boolean enviarLencos) {
		this.enviarLencos = enviarLencos;
	}
	public Boolean getEnviarPomada() {
		return enviarPomada;
	}
	public void setEnviarPomada(Boolean enviarPomada) {
		this.enviarPomada = enviarPomada;
	}
	public Boolean getEnviarLeite() {
		return enviarLeite;
	}
	public void setEnviarLeite(Boolean enviarLeite) {
		this.enviarLeite = enviarLeite;
	}
	public String getEnviarOutros() {
		return enviarOutros;
	}
	public void setEnviarOutros(String enviarOutros) {
		this.enviarOutros = enviarOutros;
	}
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	public String getIcone() {
		return icone;
	}
	public void setIcone(String icone) {
		this.icone = icone;
	}
	public Integer getStatusEventoExecutado() {
		return statusEventoExecutado;
	}
	public void setStatusEventoExecutado(Integer statusEventoExecutado) {
		this.statusEventoExecutado = statusEventoExecutado;
	}
	public Long getDataAtualizacao() {
		return dataAtualizacao;
	}
	public void setDataAtualizacao(Long dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	
	
	
}
