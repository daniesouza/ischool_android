package br.com.marrs.ischool.test;

import java.io.RandomAccessFile;

import br.com.marrs.ischool.util.Constantes;
import br.com.marrs.ischool.util.Crypto;
import br.com.marrs.ischool.util.MD5CheckSum;
import br.com.marrs.ischool.webservice.EventoExeCrypt;
import br.com.marrs.ischool.webservice.IschoolWebServiceImplService;
import br.com.marrs.ischool.webservice.MensagemCrypt;
import br.com.marrs.ischool.webservice.UsuarioCrypt;
import br.com.marrs.ischool.webservice.VectorByte;
import br.com.marrs.ischool.webservice.VectoreventoExeCrypt;
import br.com.marrs.ischool.webservice.VectormensagemCrypt;


public class TestarDevice {
	
	/*
	 * 
	 * AO EXPIRAR O HASH EXECUTAR O METODO LOGINDEVICE.. PEGAR O HASH NOVO E TROCAR O VALOR DA VARIAVEL keyhash.
	 */
	
	static IschoolWebServiceImplService service = new IschoolWebServiceImplService();
	static String keyhash = "99bdb7e2ea355a83f5258ee015d7d56b";
	static String fixSalt = "@#$ ESSA CHave Nao V@i S3r Quebrada nunca joe!! #*&$*#($%$%( @#  ischool e demais!! vai dar certo joe!! #$#$#$354";

	
	public static void loginDevice(){
		
		String senha = "12345678";
		senha = new String(Crypto.encriptSenhaMD5(senha));
		
		String usuario = "pai";
		
		try{
			final VectorByte login = new VectorByte(Crypto.encrypt(usuario.getBytes("UTF-8")));
			final VectorByte pass  = new VectorByte(Crypto.encrypt(new String(Crypto.gerarSalt()+Crypto.encriptSenhaMD5(Constantes.FIXSALT+senha+Constantes.FIXSALT)).getBytes("UTF-8")));		
			final VectorByte deviceRegIdVec = new VectorByte(Crypto.encrypt("2678".toString().getBytes("UTF-8")));
			final VectorByte strDeviceRegVec = new VectorByte(Crypto.encrypt("APA91bFW6yJdmfW7fSbriyRVUj87defbPuIF7P29mQI8M_qSi7cMgOylN49XHUPMO62zFtCZhGVUdaUDXyW6wl4UvFGZIjIl4q5pFvZFyGkAcAwBkc5Hx0Mmp8r0caM28d8N63h3Y0MzhpE9kzLjlFLAq2jAXJdtqQ".getBytes("UTF-8"))); 
			final VectorByte tipoDevice = new VectorByte(Crypto.encrypt(Constantes.DEVICE_ANDROID.toString().getBytes("UTF-8")));
			
			UsuarioCrypt usuarioCrypt = service.loginDevice(login, pass,deviceRegIdVec,strDeviceRegVec,tipoDevice);
							
			System.out.println("ID = "+new String(Crypto.decrypt(usuarioCrypt.idUsuario.toBytes())) +" HASH  = " +new String(Crypto.decrypt(usuarioCrypt.securityHashKey.toBytes())));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		

		
	}
	
	public static void enviarMensagemWeb(){
			
		MensagemCrypt mensagem = new MensagemCrypt();
		
		try {
			mensagem.idAluno   	= new VectorByte(Crypto.encrypt(new String("5").getBytes("UTF-8")));
			mensagem.idCliente 	= new VectorByte(Crypto.encrypt(new String("2").getBytes("UTF-8")));
			mensagem.idUsuario	= new VectorByte(Crypto.encrypt(new String("4").getBytes("UTF-8")));
			mensagem.mensagem  	= new VectorByte(Crypto.encrypt(new String("BOM DIAA").getBytes("UTF-8")));
			byte[] encDataAtualizacao = "12123412332".getBytes("UTF-8");
			
			service.enviarMensagemWeb(new VectorByte(Crypto.encrypt(new String("4").getBytes("UTF-8"))),
					mensagem, 
					new VectorByte(encDataAtualizacao), 
					new VectorByte(Crypto.encrypt(keyhash.getBytes("UTF-8"))));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void baixarArquivo(){
		
		try {
			
			Long offset = 0l;
			String hashArquivoMsg = "73c1d48df8a5029812fe4baba2cf46b0";
			
			byte[] encIdUsuario = Crypto.encrypt(new String("4").getBytes("UTF-8"));
			
			byte[] encIdMensagem = Crypto.encrypt(new String("2744").getBytes("UTF-8"));
			
			byte[] encDataAtualizacao = "1426043043201".getBytes("UTF-8");
			
			long len = 4776627;
			
			        
			 RandomAccessFile file = new RandomAccessFile("C:\\Teste\\dragon.avi", "rw");
			 file.setLength(len);
			 
			 while (offset<len) {
				 					
					VectorByte arquivo = service.carregarArquivo(new VectorByte(encIdUsuario),new VectorByte(encIdMensagem),
							 new VectorByte(Crypto.encrypt(offset.toString().getBytes("UTF-8"))), 
							 new VectorByte(encDataAtualizacao), 
							 new VectorByte(Crypto.encrypt(keyhash.getBytes("UTF-8"))));
					
					 file.seek(offset);
					 file.write(Crypto.decrypt(arquivo.toBytes()));
				
					 offset = offset + 20480; //20kb
			}
			 

	        file.close();
	         
			String hashArquivo = MD5CheckSum.getMD5Checksum("C:\\Teste\\dragon.avi");

			if(hashArquivo.equals(hashArquivoMsg)){
				System.out.println("Download Concluido");
			}else{
				System.out.println("Arquivo corrompido");
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void listarMensagens(){
		
		try {
			byte[] encIdUsuario = Crypto.encrypt(new String("159").getBytes("UTF-8"));
			byte[] encIdAluno 	= Crypto.encrypt(new String("8").getBytes("UTF-8"));
			byte[] encLido 		= Crypto.encrypt(new String("false").getBytes("UTF-8"));
			byte[] encDataAtualizacao = "12123412332".getBytes("UTF-8");
			
			VectormensagemCrypt lista = service.listarMensagens(new VectorByte(encIdUsuario),
																new VectorByte(encIdAluno),new VectorByte(encLido) ,
																new VectorByte(encDataAtualizacao), 
																new VectorByte(Crypto.encrypt(keyhash.getBytes("UTF-8"))));
			
			if(lista != null){
			
				StringBuilder listaIdMsgs = new StringBuilder();
				
				for(MensagemCrypt mensagem:lista){
						
					listaIdMsgs.append(new String(Crypto.decrypt(mensagem.idWebMensagem.toBytes()))+";");
				}
		
				
				System.out.println("FOI");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	
	public static void listarEventosExecutados(){
		
		
		try {
			byte[] encIdUsuario = Crypto.encrypt(new String("159").getBytes("UTF-8"));
			byte[] encIdAluno   = Crypto.encrypt(new String("8").getBytes("UTF-8"));
			byte[] encLido 		= Crypto.encrypt(new String("false").getBytes("UTF-8"));
			byte[] encDataAtualizacao = "12123412332".getBytes("UTF-8");
			
			VectoreventoExeCrypt lista = service.listarEventosExecutados(new VectorByte(encIdUsuario),new VectorByte(encIdAluno) ,
					new VectorByte(encLido) ,
					new VectorByte(encDataAtualizacao), 
					new VectorByte(encDataAtualizacao),
					new VectorByte(Crypto.encrypt(keyhash.getBytes("UTF-8"))));
			
			// AO RECUPERAR OS MENSAGENS CHAMAR NOVAMENTE O SERVICO PASSANDO OS IDS DOS EVENTOS PARA AVISAR QUE FORAM LIDAS.	
			
			
			if(lista != null){
				
				StringBuilder listaIdEvt = new StringBuilder();

				
				for(EventoExeCrypt eventoExe:lista){
					
					listaIdEvt.append(new String(Crypto.decrypt(eventoExe.idEventoExecutado.toBytes()))+";");
				}		
									
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	
	public static void registrarDevice(){
				
		String idUsuario = "159";
		
		try{
			VectorByte encIdUsuario = new VectorByte(Crypto.encrypt(new String(idUsuario).getBytes("UTF-8")));
			
			VectorByte newDeviceRegId = new VectorByte(Crypto.encrypt(new String("APA91bHIjCnlSN7V_urDU4pn79VFkM42Tiw").getBytes("UTF-8")));
			VectorByte oldDeviceRegId = new VectorByte(Crypto.encrypt(new String("4324324324gdfgdfg4354gfdgfd").getBytes("UTF-8")));
			String tipoDevice = "1"; // 1 = IOS , 2  = ANDROID , 3 = WINDOWS
			byte[] encDataAtualizacao = "12123412332".getBytes("UTF-8");
			
			service.registrarDevice(encIdUsuario, oldDeviceRegId, newDeviceRegId, new VectorByte(Crypto.encrypt(tipoDevice.getBytes("UTF-8"))),
					  new VectorByte(encDataAtualizacao), 
					new VectorByte(Crypto.encrypt(keyhash.getBytes("UTF-8"))));
							
		}catch(Exception ex){
			ex.printStackTrace();
		}	
	}

	
	public static void main(String[] args) throws Exception {
		
		System.out.println("EXECUTANDO");

		
		loginDevice();
//		enviarMensagemWeb();
//		listarEventosExecutados();
//		baixarArquivo();
//		listarMensagens();
//		registrarDevice();		
		System.out.println("EXECUTADO");
		
	}

}
