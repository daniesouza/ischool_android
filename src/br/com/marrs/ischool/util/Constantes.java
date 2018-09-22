package br.com.marrs.ischool.util;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import br.com.marrs.ischool.entidades.Mensagem;

/**
 * @author Daniel Souza de lima e-mail:daniesouza@gmail.com
 * 
 */
public class Constantes {

	public static final int	   TAMANHO_BUFFER    = 1024; // 2 Kb
	public static final int	   TAMANHO_SENHA	 = 8;
	public static final String ADMINISTRADOR	 = "ROLE_ADMIN";
	public static final String ADMIN_CLIENTE	 = "ROLE_ADMIN_CLIENT";
	public static final String PROFESSOR		 = "ROLE_PROFESSOR";
	public static final String RESPONSAVEL		 = "ROLE_RESPONSAVEL";	
	public static final String IP_CONNECT 		 = "ischool.noip.me";
	
	public static final int ITENS_PAGINACAO = 30;
	
	public static final long  TRINTA_MEGAS 	 = 31457280;
	
	// HORARIOS DE EVENTO
	public static final int    PERIODO_MANHA		= 1; 
	public static final int    PERIODO_TARDE		= 2;
	public static final int    PERIODO_OUTROS		= 3;
	
	//TIPOS DE DEVICE
	
	public static final Integer DEVICE_ANDROID = 1;
	public static final Integer DEVICE_IOS = 2;
	public static final Integer DEVICE_FIREFOXOS = 3;
	
	// TIPOS MENSAGEM
	
	public static final String TIPO_MSG_TEXTO ="TEXTO";
	public static final String TIPO_MSG_EVENTO ="EVENTO";
	public static final String TIPO_MSG_EVENTO_CANC ="EVENTO_CANC";
	public static final String TIPO_MSG_LEMBRETE ="LEMBRETE";
	public static final String TIPO_MSG_LEMBRETE_CANC ="LEMBRETE_CANC";

	
	// TIPOS DE EVENTO
	public static final int TIPO_INFORMATIVO = 1;
	public static final int TIPO_INICIO_FIM  = 2;
	public static final int TIPO_ALIMENTO  	 = 3;
	public static final int TIPO_EVACUACAO   = 4;
	public static final int TIPO_MEDICAMENTO = 5;
	public static final int TIPO_LEMBRETE 	 = 6;
	public static final int TIPO_LEMBRETE_GEN = 7;
	
	// STATUS DO EVENTO
	public static final int STATUS_CANCELADO = 0;
	public static final int STATUS_OK = 1;

	public static final String ICONE_OK 	 = "img-ok-icon.png";
	public static final String ICONE_WARNING = "img-warn-icon.png";
	public static final String ICONE_NAO_OK  = "img-nao-ok-icon.png";
	
	public static final String API_KEY = "AIzaSyB_gtUvfBlV7na12DktZtaWZfe8O9D27Es";
	
	public static final String ASSUNTO_MSG = "Nova Mensagem!";
	
	public static final String LOCAL_SALVAR_ARQUIVO = "C:"+File.separator+"Teste";
	
	public static final int MINUTOSVALIDADEHASH = 240;
	public static final String FIXSALT = "@#$ ESSA CHave Nao V@i S3r Quebrada nunca joe!! #*&$*#($%$%( @#  ischool e demais!! vai dar certo joe!! #$#$#$354";

	

	public final  static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	public static final String ISCHOOL_SHARED_PREF = "ischool_shared_preferences";
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String REMEMBERED_LOGIN = "remembered_login";
	public static final String REMEMBERED_PASS 	= "remembered_pass";
	public static final String DEVICE_ID 		= "device_id";

	public static final String AUTOMATIC_LOGIN 	= "automatic_login";
	public static final String ID_LOGGED_USER	= "id_logged_user";
	
	public static final String SUCCESS = "SUCCESS";
	public static final String FAIL = "FAIL";
	public static final String USUARIO_SENHA_INCORRETO = "USUARIO_SENHA_INCORRETO";
	public static final String USUARIO_DESATIVADO = "USUARIO_DESATIVADO";
	public static final String CLIENTE_DESATIVADO = "CLIENTE_DESATIVADO";
	public static final String USUARIO_SEM_ROLES = "USUARIO_SEM_ROLES";
	public static final String SISTEMA_INDISPONIVEL = "SISTEMA_INDISPONIVEL";
	public static final String SEM_CONEXAO = "SEM_CONEXAO";
	public static final String DATA_HASH_EXPIRADO = "DATA_HASH_EXPIRADO";
	public static final String HASH_INVALIDO = "HASH_INVALIDO";
	public static final String USUARIO_ATUALIZADO = "USUARIO_ATUALIZADO";


	public static final int GOOGLE_PLAY_NAO_SUPORTADO = 2;
	public static final int GOOGLE_PLAY_SUPORTADO = 0;
	public static final int GOOGLE_PLAY_ATUALIZAR = 1;
	
	public static final int ACAO_DOWNLOAD = 2;
	public static final int ACAO_UPLOAD = 1;
	
	public static final int STATUS_ACAO_COMPLETO   = 1;
	public static final int STATUS_ACAO_INCOMPLETO = 2;
	public static final int STATUS_ACAO_CANCELADO  = 0;
	
	public static final int STATUS_LOGIN_NAO_LOGADO = 0;
	public static final int STATUS_LOGIN_LOGADO = 1;
	public static final int STATUS_LOGIN_EM_PROCESSO = 2;
	
	public static final String DIRETORIO_ROOT = "Ischool";
	public static final String DIRETORIO_MEDIA = "Media";
	public static final String DIRETORIO_IMAGES = "Ischool Images";
	public static final String DIRETORIO_VIDEOS = "Ischool Videos";
	public static final String DIRETORIO_AUDIO = "Ischool Audio";
	public static final String DIRETORIO_GENERICO = "Ischool Files";
	
	public static final int TAKE_PICTURE_VIDEO = 1;
	public static final int SELECT_FILE = 2;
	public static final int PIC_CROP = 3;
	
	public static final CopyOnWriteArrayList<Mensagem> listaEnviarMensagens = new CopyOnWriteArrayList<Mensagem>();
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.ROOT);
	
	public static final List<Mensagem> listaMensagensDownload = new ArrayList<Mensagem>();
	public static final List<Mensagem> listaMensagensUpload = new ArrayList<Mensagem>();

	
}
