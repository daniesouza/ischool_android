package br.com.marrs.ischool.entidades;

import java.util.Date;

import android.graphics.Bitmap;
import br.com.marrs.ischool.model.ViewHolder;



public class Mensagem  implements Entidade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6055130210134711847L;


    private Long idDeviceMensagem;
    
    private Long idWebMensagem;
	 
	private Cliente cliente;
    
	private Aluno aluno;
    
    private Usuario usuario;
        
    private String mensagem;
    
    private Date dataCadastro;       
    
    private Long tamanhoBytes;
    
    private String hashArquivo;
        
    private String caminhoArquivo;
    
    private int tipoAcao; // 1 upload, 2 download 
    
    private int statusAcao; // 1 - completo , 2- incompleto, 0- cancelado
    
    // iTEMS ABAIXO SAO USADOS NAS VIEWS
   
	public int tipo_balao;
	public int iconFileVisible;
	public int alinhamento;
	public int fontColor;
	public String textoHeader;
	public int headerVisible;
	public int progressVisible;
	public int nuvemVisible;
	public int thumbArquivoVisible;
	public String nuvemText;
	public String nomeArquivo;
	public int progressDownloadVisible;	
	public int mensagemArquivoVisible;
	public int mensagemVisible;
	public String textoArquivo;
	
	public boolean acaoCancelada = false;
	public String titleProgress = "0%";
	public String subTitleProgress = "n/a";
	public int progress;
	

	public ViewHolder holder;
	
	public Bitmap thumbImage;

	
	

	@Override
	public Long getId() {
		
		return this.getIdDeviceMensagem();
	}

	@Override
	public void setId(Long id) {
		this.setIdDeviceMensagem(id);		
	}
	
	
	public Long getIdDeviceMensagem() {
		return idDeviceMensagem;
	}

	public void setIdDeviceMensagem(Long idDeviceMensagem) {
		this.idDeviceMensagem = idDeviceMensagem;
	}

	public Long getIdWebMensagem() {
		return idWebMensagem;
	}

	public void setIdWebMensagem(Long idWebMensagem) {
		this.idWebMensagem = idWebMensagem;
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

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Long getTamanhoBytes() {
		return tamanhoBytes;
	}

	public void setTamanhoBytes(Long tamanhoBytes) {
		this.tamanhoBytes = tamanhoBytes;
	}

	public String getHashArquivo() {
		return hashArquivo;
	}

	public void setHashArquivo(String hashArquivo) {
		this.hashArquivo = hashArquivo;
	}

	public int getTipo_balao() {
		return tipo_balao;
	}

	public void setTipo_balao(int tipo_balao) {
		this.tipo_balao = tipo_balao;
	}

	public int getAlinhamento() {
		return alinhamento;
	}

	public void setAlinhamento(int alinhamento) {
		this.alinhamento = alinhamento;
	}

	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	public String getTextoHeader() {
		return textoHeader;
	}

	public void setTextoHeader(String textoHeader) {
		this.textoHeader = textoHeader;
	}

	public int getHeaderVisible() {
		return headerVisible;
	}

	public void setHeaderVisible(int headerVisible) {
		this.headerVisible = headerVisible;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idDeviceMensagem == null) ? 0 : idDeviceMensagem.hashCode());
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
		Mensagem other = (Mensagem) obj;
		if (idDeviceMensagem == null) {
			if (other.idDeviceMensagem != null)
				return false;
		} else if (!idDeviceMensagem.equals(other.idDeviceMensagem))
			return false;
		return true;
	}


	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public int getTipoAcao() {
		return tipoAcao;
	}

	public void setTipoAcao(int tipoAcao) {
		this.tipoAcao = tipoAcao;
	}

	public int getStatusAcao() {
		return statusAcao;
	}

	public void setStatusAcao(int statusAcao) {
		this.statusAcao = statusAcao;
	}
	
 
	

}


