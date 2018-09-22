package br.com.marrs.ischool.webservice;

public class WebserviceUtil 
{
	//CONSUMO WEBSERVICE
	public static final String NAMESPACE = "http://webservice.titapodologia.com.br/";
	public static final String URL = "http://ec2-177-71-152-159.sa-east-1.compute.amazonaws.com:8080/tita_app/IntegracaoMobileTitaPodologia?WSDL";
//	public static final String URL = "http://192.168.0.103:8080/tita_app/IntegracaoMobileTitaPodologia?WSDL";
//	public static final String URL = "http://10.0.2.2:8080/tita_app/IntegracaoMobileTitaPodologia?WSDL";
	
	public static final String METHOD_CADASTRA_USUARIO = "cadastraUsuarioSeguro";
	public static final String SOAP_ACTION_CADASTRA_USUARIO = "http://webservice.titapodologia.com.br/IntegracaoMobileTitaPodologia/cadastraUsuarioSeguroRequest";
	
	public static final String METHOD_LOGIN = "loginEncript";
	public static final String SOAP_ACTION_LOGIN = "http://webservice.titapodologia.com.br/IntegracaoMobileTitaPodologia/loginEncriptRequest";
	
	public static final String METHOD_GET_NOTICIAS = "getNoticias";
	public static final String SOAP_ACTION_GET_NOTICIAS = "http://webservice.titapodologia.com.br/IntegracaoMobileTitaPodologia/getNoticiasRequest";
	
	public static final String METHOD_GET_DICAS = "getDicas";
	public static final String SOAP_ACTION_GET_DICAS = "http://webservice.titapodologia.com.br/IntegracaoMobileTitaPodologia/getDicasRequest";
	
	public static final String METHOD_GET_SOLICITA_HORARIO = "solicitaHorario";
	public static final String SOAP_ACTION_SOLICITA_HORARIO  = "http://webservice.titapodologia.com.br/IntegracaoMobileTitaPodologia/solicitaHorarioRequest";
	
	public static final String METHOD_GET_SOLICITA_HORARIO_USUARIO = "getHorariosPorUsuario";
	public static final String SOAP_ACTION_SOLICITA_HORARIO_USUARIO  = "http://webservice.titapodologia.com.br/IntegracaoMobileTitaPodologia/getHorariosPorUsuarioRequest";
	
	public static final String METHOD_CANCELA_HORARIO_USUARIO = "cancelaHorario";
	public static final String SOAP_ACTION_CANCELA_HORARIO = "http://webservice.titapodologia.com.br/IntegracaoMobileTitaPodologia/cancelaHorarioRequest";
	
	public static final String METHOD_ENVIA_MENSAGEM = "enviaMensagemParaTita";
	public static final String SOAP_ACTION_ENVIA_MENSAGEM = "http://webservice.titapodologia.com.br/IntegracaoMobileTitaPodologia/enviaMensagemParaTitaRequest";
	
	public static final String METHOD_RECEBE_MENSAGENS = "clienteRecebeMensagensDaTita";
	public static final String SOAP_ACTION_RECEBE_MENSAGENS = "http://webservice.titapodologia.com.br/IntegracaoMobileTitaPodologia/clienteRecebeMensagensDaTitaRequest";
	
	public static final String METHOD_TROCA_SENHA = "trocaSenha";
	public static final String SOAP_ACTION_TROCA_SENHA = "http://webservice.titapodologia.com.br/IntegracaoMobileTitaPodologia/trocaSenhaRequest";
	
	public static final String METHOD_ATUALIZA_DEVICE = "atualizaDeviceUsuario";
	public static final String SOAP_ACTION_ATUALIZA_DEVICE = "http://webservice.titapodologia.com.br/IntegracaoMobileTitaPodologia/atualizaDeviceUsuarioRequest";
}
