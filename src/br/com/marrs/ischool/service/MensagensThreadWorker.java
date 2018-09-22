package br.com.marrs.ischool.service;

import android.view.View;
import br.com.marrs.ischool.MainActivity;
import br.com.marrs.ischool.entidades.Mensagem;
import br.com.marrs.ischool.util.Constantes;
import br.com.marrs.ischool.util.Crypto;
import br.com.marrs.ischool.util.DadosUtil;
import br.com.marrs.ischool.view.mensagens.MensagemFragment;
import br.com.marrs.ischool.webservice.IschoolWebServiceImplService;
import br.com.marrs.ischool.webservice.MensagemCrypt;
import br.com.marrs.ischool.webservice.VectorByte;

public class MensagensThreadWorker implements Runnable {
	

	
	@Override
	public void run() {
		
	//	while (!Thread.currentThread().isInterrupted()) {
				

        	while(!Constantes.listaEnviarMensagens.isEmpty()){
	        	
				try {			
					
					Mensagem msg = Constantes.listaEnviarMensagens.get(0);
						
					
					while (DadosUtil.statusLogin == Constantes.STATUS_LOGIN_EM_PROCESSO || DadosUtil.statusLogin  == Constantes.STATUS_LOGIN_NAO_LOGADO) {
						continue;
					}
							
					MensagemCrypt mensagem = new MensagemCrypt();
					
					mensagem.idUsuario 			= new VectorByte(Crypto.encrypt(msg.getUsuario().getId().toString().getBytes("UTF-8")));
					mensagem.idAluno   			= new VectorByte(Crypto.encrypt(msg.getAluno().getId().toString().getBytes("UTF-8")));
					mensagem.idCliente 			= new VectorByte(Crypto.encrypt(msg.getCliente().getId().toString().getBytes("UTF-8")));
					mensagem.mensagem  			= new VectorByte(Crypto.encrypt(msg.getMensagem().getBytes("UTF-8")));
					mensagem.idDeviceMensagem	= new VectorByte(Crypto.encrypt(msg.getIdDeviceMensagem().toString().getBytes("UTF-8")));
					
					byte[] keyhash				= Crypto.encrypt(DadosUtil.usuarioLogado.getSecurityHashKey().getBytes("UTF-8"));
					byte[] encDataAtualizacao	= Crypto.encrypt(DadosUtil.usuarioLogado.getDataAtualizacao().toString().getBytes("UTF-8"));

					
					
					VectorByte  idMensagem = IschoolWebServiceImplService.getInstance().enviarMensagemWeb(mensagem.idUsuario,
																										  mensagem,
																										  new VectorByte(encDataAtualizacao),
																										  new VectorByte(keyhash));

					// TODO REVER AS DATAS						
					if(idMensagem != null && !idMensagem.isEmpty()){
						
						String[] retorno = new String(Crypto.decrypt(idMensagem.toBytes())).split(";");
						
						msg.setIdWebMensagem(Long.valueOf(retorno[0]));
						msg.setDataCadastro(Constantes.dateFormat.parse(retorno[1]));
						
						MainActivity.instance.dbMensagemAdapter.storeMensagemInDataBase(msg);
						msg.getAluno().setMensagemDescricaoMensagem(msg.getMensagem());
						msg.getAluno().setQuantidadeMensagensNovas(0);
		    			MainActivity.instance.dbAlunoAdapter.storeAlunoNewMensagens(msg.getAluno());
		    			
		    			MensagemFragment.instance.listaBanco.add(msg);
										
						msg.progressVisible =  View.GONE;												
						Constantes.listaEnviarMensagens.remove(0);
						MensagemFragment.handler.sendEmptyMessage(0);
						MensagemFragment.instance.idsSync.add(msg.getIdWebMensagem());
						
					}
					

				} catch (Exception e) {
										
					String result = e.getMessage() == null ? "":e.getMessage();
					
					switch (result) {
						
					case Constantes.DATA_HASH_EXPIRADO:	
						MainActivity.instance.automaticLogin();
						break;
						
					case Constantes.HASH_INVALIDO:
						MainActivity.instance.automaticLogin();
						break;
					case Constantes.USUARIO_ATUALIZADO:	
						MainActivity.instance.automaticLogin();
						break;					
						
					default:	
						e.printStackTrace();
					}
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
				}
        	}
		}
	//}
 }


