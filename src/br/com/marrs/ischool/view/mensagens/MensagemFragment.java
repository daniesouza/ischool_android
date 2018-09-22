package br.com.marrs.ischool.view.mensagens;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.marrs.ischool.LoginListener;
import br.com.marrs.ischool.MainActivity;
import br.com.marrs.ischool.R;
import br.com.marrs.ischool.entidades.Mensagem;
import br.com.marrs.ischool.entidades.Usuario;
import br.com.marrs.ischool.model.CircularProgressBar;
import br.com.marrs.ischool.model.ViewHolder;
import br.com.marrs.ischool.service.MensagensThreadWorker;
import br.com.marrs.ischool.util.Constantes;
import br.com.marrs.ischool.util.Crypto;
import br.com.marrs.ischool.util.DadosUtil;
import br.com.marrs.ischool.util.ImagemUtils;
import br.com.marrs.ischool.util.MD5CheckSum;
import br.com.marrs.ischool.webservice.IschoolWebServiceImplService;
import br.com.marrs.ischool.webservice.MensagemCrypt;
import br.com.marrs.ischool.webservice.VectorByte;
import br.com.marrs.ischool.webservice.VectormensagemCrypt;

import com.google.android.gcm.NotificationHelper;

public class MensagemFragment extends Fragment implements LoginListener{
	private RelativeLayout fl;
	private ListView listMensagens;
	private static MensagemArrayAdapter adapter;
	private static final int FRAGMENT_ID = 6;
	private long idUsuarioUltimaMensagem = 0l;
	private ProgressBar progress;
	private LinearLayout layoutMensagemErro;	
	
	private List<Mensagem> listaNovasMensagens;
	public  List<Mensagem> listaBanco;
	private SimpleDateFormat sdf;
	
	private boolean carregarMensagens;
	
	private volatile boolean cancelouExecucao;
	private static Thread threadEnviarMsg;
	public List<Long> idsSync;
	

    private Uri uriFileOriginal;
    
    private long lastIdHistorico = 0;

	
	public static MensagemFragment instance;
	
    public static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			adapter.notifyDataSetChanged();
		}

	};

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		instance = this;
		if(fl == null){
			fl = (RelativeLayout)inflater.inflate(R.layout.fragment_mensagens, container, false);
		}
		setHasOptionsMenu(true);
		cancelouExecucao = false;
		
		sdf = new SimpleDateFormat("dd/MM/yyy - HH:mm",Locale.ROOT);
		
		listMensagens = (ListView)fl.findViewById(R.id.lv_mensagens);
		
		adapter = new MensagemArrayAdapter(MainActivity.instance);
		listMensagens.setAdapter(adapter);
		
		
		// TODO SETAR animacao na lista
	//	listMensagens.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listMensagens.setStackFromBottom(true);
			
		progress = (ProgressBar) fl.findViewById(R.id.progress_mensagens);
		layoutMensagemErro = (LinearLayout) fl.findViewById(R.id.layout_mensagem_erro);	
		
		MainActivity.instance.loginManager.removeListeners();
		MainActivity.instance.loginManager.addListener(this, MensagemFragment.FRAGMENT_ID);
		
				
		ImageButton botaoEnviar = (ImageButton) fl.findViewById(R.id.ib_envia_mensagem);
		botaoEnviar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				EditText textoMensagem = (EditText) fl.findViewById(R.id.texto_mensagem);
				
				String texto = textoMensagem.getText().toString();
				sendMensagem(texto);
				textoMensagem.setText("");

				
			}
		});
		
		EditText textoMensagem = (EditText) fl.findViewById(R.id.texto_mensagem);
		
		textoMensagem.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				listMensagens.setSelection(adapter.getCount() - 1);					
			}
		});
				
		return fl;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		if(DadosUtil.alunoSelecionado == null){
			return;
		}
		
		ImageView icon = (ImageView) fl.findViewById(R.id.iv_aluno_icon);
		
		if(DadosUtil.alunoSelecionado.getFoto() != null){						
			icon.setImageBitmap(DadosUtil.alunoSelecionado.getFoto());
		}else{
			icon.setImageResource(R.drawable.icon_filho_no_pic);
		}
		
		idsSync = MainActivity.instance.dbMensagemAdapter.getIdsSincronizar(DadosUtil.alunoSelecionado.getId());
		
		MainActivity.instance.dbAlunoAdapter.clearAlunosQuantMensagens(DadosUtil.alunoSelecionado);
		
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager) MainActivity.instance.getSystemService(ns);
		nMgr.cancel(Constantes.TIPO_MSG_TEXTO,DadosUtil.alunoSelecionado.getId().intValue());
		
		// TODO REVER SAPORRA	de notification helper		
		NotificationHelper.mensagensNotifications.put(DadosUtil.alunoSelecionado.getId().intValue(), "");
		
		buscarDadosBanco(0);	
		
		handeEnviarMensagens();
	}
	
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.enviar, menu);	
    	super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        case R.id.menu_galeria:
        	
        	Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        	intent.setType("image/*, video/*, audio/*");     	
        	startActivityForResult(intent, Constantes.SELECT_FILE);


            return true;
            
        case R.id.menu_foto:
        	
            if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Toast.makeText(MainActivity.instance, "Erro ao acessar diretorio. Não e possivel tirar foto.", Toast.LENGTH_SHORT).show();
            	return true;
            }
            
            File rootPath = new File(Environment.getExternalStorageDirectory()+File.separator+Constantes.DIRETORIO_ROOT+File.separator+Constantes.DIRETORIO_MEDIA,Constantes.DIRETORIO_IMAGES);
            if(!rootPath.exists()) {
                if(!rootPath.mkdirs()){                  	
                    Toast.makeText(MainActivity.instance, "Erro ao acessar diretorio. Não e possivel tirar foto.", Toast.LENGTH_SHORT).show();
                	return true;
                }
            }
		
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            uriFileOriginal = Uri.fromFile(new File(rootPath, String.valueOf("foto"+System.currentTimeMillis())+".jpg"));

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFileOriginal);	                        
            startActivityForResult(cameraIntent, Constantes.TAKE_PICTURE_VIDEO);	
             
            return true;  
            
        case R.id.menu_audio:

        	Intent intentAudio = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        	startActivityForResult(intentAudio, Constantes.SELECT_FILE);
        	
            return true;             
            
        case R.id.menu_video:
        	
            if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Toast.makeText(MainActivity.instance, "Erro ao acessar diretorio. Não e possivel tirar foto.", Toast.LENGTH_SHORT).show();
            	return true;
            }
            
            File rootPathV = new File(Environment.getExternalStorageDirectory()+File.separator+Constantes.DIRETORIO_ROOT+File.separator+Constantes.DIRETORIO_MEDIA,Constantes.DIRETORIO_VIDEOS);
            if(!rootPathV.exists()) {
                if(!rootPathV.mkdirs()){                  	
                    Toast.makeText(MainActivity.instance, "Erro ao acessar diretorio. Não e possivel tirar foto.", Toast.LENGTH_SHORT).show();
                	return true;
                }
            }
        	
            Intent intentVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            uriFileOriginal = Uri.fromFile(new File(rootPathV, String.valueOf("video"+System.currentTimeMillis())+".mp4"));
          //  Toast.makeText(MainActivity.instance, uriFileOriginal.toString(), Toast.LENGTH_LONG).show();
            intentVideo.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // 0 low quality , 1 high quality

            intentVideo.putExtra(MediaStore.EXTRA_SIZE_LIMIT, Constantes.TRINTA_MEGAS);
           // intentVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20);

            intentVideo.putExtra(MediaStore.EXTRA_OUTPUT, uriFileOriginal); 
            startActivityForResult(intentVideo, Constantes.TAKE_PICTURE_VIDEO);
        	
            return true;              
        default:
        	return super.onOptionsItemSelected(item);
        }
    	
    	
    }
    
    
	public void buscarDadosBanco(final int limitCount) {	
		
		startProgress();
		adapter.clear();
	    Runnable runnable = new Runnable() {
	        @Override
	        public void run() {       			
	        	
	        	if(cancelouExecucao){
	        		return;
	        	}
	        		        	
	        	listaBanco = MainActivity.instance.dbMensagemAdapter.returnByoffSet(DadosUtil.alunoSelecionado.getId(),limitCount);
	        	Collections.reverse(listaBanco);
	        	
	        	new AsyncTask<Void, Mensagem ,Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						
	                	for(Mensagem msg:listaBanco){	      	        	        	
	        	        	if(cancelouExecucao){
	        	        		break;
	        	        	}
		                	publishProgress(msg);	                		
	                	}
						
	                	return null;
					}
					
					@Override
					protected void onProgressUpdate(Mensagem... values) {						
        	        	if(cancelouExecucao){
        	        		return;
        	        	}						
        	        	buildMensagem(values[0],false);		
					}
					
					@Override
					protected void onPostExecute(Void result) {
        	        	if(cancelouExecucao){
        	        		return;
        	        	}
        	        	
        	        	if(!listaBanco.isEmpty()){
        	        		
        	        		lastIdHistorico = listaBanco.get(0).getId();
 	        	           
            	        	final Button btnTag = new Button(getActivity());
            	            btnTag.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            	            btnTag.setText("Carregar Mensagens Anteriores");
            	            if(listaBanco.size()>=Constantes.ITENS_PAGINACAO){
            	            	if(listMensagens.getHeaderViewsCount()<1){
            	            		listMensagens.addHeaderView(btnTag);
            	            	}
            	            }
            	    		
            	    		
            	    		btnTag.setOnClickListener(new View.OnClickListener() {
    							
    							@Override
    							public void onClick(View v) {
    									        	    																			
    								List<Mensagem> listaBancoTemp = MainActivity.instance.dbMensagemAdapter.returnByoffSet(DadosUtil.alunoSelecionado.getId(),lastIdHistorico);
    								
    								if(listaBancoTemp.isEmpty()){
    									listMensagens.removeHeaderView(btnTag);
    									return;
    								}
        								
									if(listaBancoTemp.size() < Constantes.ITENS_PAGINACAO){
    									listMensagens.removeHeaderView(btnTag);
    								}
									
									
    								lastIdHistorico = listaBancoTemp.get(listaBancoTemp.size()-1).getId();							
    								
    								listaBanco.addAll(listaBancoTemp);
    								for(Mensagem msg : listaBancoTemp){
    									buildMensagem(msg, true);   									
    								}

    		        	    		listMensagens.setSelection(listaBancoTemp.size());    									
    								
   								
    							}
    						});
        	        	}
        	        	
						carregarMensagens();
					
					}
				}.execute();  
			
        	}		
	        
	      };
	      Thread buscaThread = new Thread(runnable);
	      buscaThread.start();									
	}
	


	public void handeEnviarMensagens(){
		
		if((threadEnviarMsg == null || !threadEnviarMsg.isAlive()) && !Constantes.listaEnviarMensagens.isEmpty()){
			threadEnviarMsg = new Thread(new MensagensThreadWorker());
			threadEnviarMsg.setName("ThreadEnviaMensagem");
			threadEnviarMsg.start();
		}	
	}

	public static boolean sincronizando = false;
	public synchronized void carregarMensagens(){
		
		if(cancelouExecucao){
			return;
		}
		
		sincronizando = true;
					
		startProgress();
		
	    Runnable runnable = new Runnable() {
	        @Override
	        public void run() {     
	        		        	
				try {
					
    	        	if(cancelouExecucao){
    	        		return;
    	        	}
					
					String encIdsMensagens = "";
					
					
					
					if(!idsSync.isEmpty()){
						for(Long ids:idsSync){
	        	        	if(cancelouExecucao){
	        	        		return;
	        	        	}
							encIdsMensagens += ids+",";							
						}
					}

					
					while (DadosUtil.statusLogin == Constantes.STATUS_LOGIN_EM_PROCESSO && !cancelouExecucao) {
						continue;
					}
					
					if(DadosUtil.statusLogin  == Constantes.STATUS_LOGIN_NAO_LOGADO){
						return;
					}
					
    	        	if(cancelouExecucao){
    	        		return;
    	        	}
					
					byte[] encIdUsuario 		= Crypto.encrypt(DadosUtil.usuarioLogado.getId().toString().getBytes("UTF-8"));
					byte[] encIdAluno   		= Crypto.encrypt(DadosUtil.alunoSelecionado.getId().toString().getBytes("UTF-8"));
					byte[] encIdsMensagen 		= Crypto.encrypt(new String(encIdsMensagens).getBytes("UTF-8"));
					byte[] keyhash				= Crypto.encrypt(DadosUtil.usuarioLogado.getSecurityHashKey().getBytes("UTF-8"));
					byte[] encDataAtualizacao	= Crypto.encrypt(DadosUtil.usuarioLogado.getDataAtualizacao().toString().getBytes("UTF-8"));
					
					
					VectormensagemCrypt lista = IschoolWebServiceImplService.getInstance().listarMensagens(new VectorByte(encIdUsuario),
																										   new VectorByte(encIdAluno),
																										   new VectorByte(encIdsMensagen),
																										   new VectorByte(encDataAtualizacao),
																										   new VectorByte(keyhash));
					
					sincronizando = false;
    	        	if(cancelouExecucao){
    	        		return;
    	        	}
					
					if(lista != null && !lista.isEmpty()){
						
						if(listaNovasMensagens == null){
							listaNovasMensagens = new Vector<Mensagem>();
						}
											
						for(MensagemCrypt mensagemCrypt:lista){
							
							Mensagem mensagem = mensagemCrypt.decriptarMensagem();
							mensagem.setCliente(DadosUtil.usuarioLogado.getCliente());
							mensagem.setAluno(DadosUtil.alunoSelecionado);
							
							if(mensagem.getHashArquivo() != null){
								mensagem.setTipoAcao(Constantes.ACAO_DOWNLOAD);
							}
							
							listaNovasMensagens.add(mensagem);	
							idsSync.add(mensagem.getIdWebMensagem());
						
						}
					}
					
    	        	if(cancelouExecucao){
    	        		return;
    	        	}

					salvarDadosBanco();
					

				} catch (Exception e) {
					
					sincronizando = false;
    	        	if(cancelouExecucao){
    	        		return;
    	        	}
					
    	        	String result = e.getMessage() == null ? "":e.getMessage();
					carregarMensagens = true;
					
					switch (result) {
						
					case Constantes.DATA_HASH_EXPIRADO:							
						MainActivity.instance.automaticLogin();
						break;						
					case Constantes.HASH_INVALIDO:
						MainActivity.instance.automaticLogin();
						break;
					case Constantes.USUARIO_ATUALIZADO:	
						carregarMensagens = true;
						MainActivity.instance.automaticLogin();
						break;						
						
					default:
						
						MainActivity.instance.runOnUiThread(new Runnable() {
		    	        public void run() {    
		    	                try{
		    						layoutMensagemErro.setVisibility(View.VISIBLE);					
		    						layoutMensagemErro.setOnClickListener(new View.OnClickListener(){
		    						    @Override
		    						    public void onClick(View v){
		    						    	layoutMensagemErro.setVisibility(View.GONE);	
		    						    	
		    						    	if(DadosUtil.statusLogin == Constantes.STATUS_LOGIN_NAO_LOGADO){
		    						    		startProgress();
		    						    		MainActivity.instance.automaticLogin();
		    						    	}else{
		    						    		carregarMensagens();
		    						    	}
		    						    }
		    						});
		    						stopProgress();
		    	                }
		    	                catch(Exception e){}
		    	        }
						});
										
						e.printStackTrace();
						break;	
					}
					
					return;
										
				}
			
				new AsyncTask<Void, Mensagem, Void>() {

						@Override
						protected Void doInBackground(Void... params) {
						
							if(listaNovasMensagens != null && !listaNovasMensagens.isEmpty()){
								for(Mensagem mensagem:listaNovasMensagens){
			        	        	if(cancelouExecucao){
			        	        		break;
			        	        	}
									publishProgress(mensagem);
								}
							}

							return null;
						}
						
						protected void onProgressUpdate(Mensagem... values) {
							
	        	        	if(cancelouExecucao){
	        	        		return;
	        	        	}
							
							buildMensagem(values[0],false);
							listaBanco.add(values[0]);
							

						}
						
						protected void onPostExecute(Void result) {
	        	        	if(cancelouExecucao){
	        	        		return;
	        	        	}
	        	        	if(listaNovasMensagens != null && !listaNovasMensagens.isEmpty()){
		        	        	listaNovasMensagens.clear();
	        	        	}
							carregarMensagens = false;
							listMensagens.setSelection(adapter.getCount() - 1);	
							stopProgress();
						};
						 
					 }.execute();

	        }
	      };
	      
	      Thread carregarMsg = new Thread(runnable);
	      carregarMsg.start();	
		
	}
	
	
	private void salvarDadosBanco(){
		if(listaNovasMensagens == null || listaNovasMensagens.isEmpty()){
			return;
		}
		MainActivity.instance.dbMensagemAdapter.storeMensagensInDataBase(listaNovasMensagens);
		
		DadosUtil.alunoSelecionado.setMensagemDescricaoMensagem(listaNovasMensagens.get(listaNovasMensagens.size()-1).getMensagem());
		DadosUtil.alunoSelecionado.setQuantidadeMensagensNovas(0);
		MainActivity.instance.dbAlunoAdapter.storeAlunoNewMensagens(DadosUtil.alunoSelecionado);

	}

	
	public void sendMensagem(String texto){
						
		if(!texto.trim().isEmpty()){
			
			final Mensagem msg = new Mensagem();
			msg.setUsuario(DadosUtil.usuarioLogado);
			msg.setAluno(DadosUtil.alunoSelecionado);
			msg.setCliente(DadosUtil.usuarioLogado.getCliente());
			msg.setMensagem(texto);
			msg.setDataCadastro(new Date());
					
			buildMensagem(msg,false);

			MainActivity.instance.dbMensagemAdapter.storeMensagemInDataBase(msg);
			
			handeEnviarMensagens();
			
			listMensagens.setSelection(adapter.getCount() - 1);	
					
		}
	
	}
	
	
	public void sendFile(Uri urlFile){
		
		String url = DadosUtil.getRealPathFromURI(urlFile,MainActivity.instance);
		
		File dataFile = new File(url);
		
		if(dataFile.length()>Constantes.TRINTA_MEGAS){
			Toast.makeText(MainActivity.instance, "Arquivo muito grande. (máx 30mb).", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Mensagem mensagem = new Mensagem();
		mensagem.setTipoAcao(Constantes.ACAO_UPLOAD);
		mensagem.setStatusAcao(Constantes.STATUS_ACAO_INCOMPLETO);
		mensagem.setUsuario(DadosUtil.usuarioLogado);
		mensagem.setAluno(DadosUtil.alunoSelecionado);
		mensagem.setCliente(DadosUtil.usuarioLogado.getCliente());
		mensagem.setDataCadastro(new Date());		
		mensagem.setCaminhoArquivo(url);
		
		
		if(!mensagem.getUsuario().getId().equals(idUsuarioUltimaMensagem)){
			mensagem.textoHeader = mensagem.getUsuario().getNome()+ " - "+sdf.format(mensagem.getDataCadastro());
			mensagem.headerVisible =  View.VISIBLE;
		}else{
			mensagem.headerVisible =  View.GONE;
		}
				

		mensagem.alinhamento = Gravity.CENTER;
		mensagem.fontColor = Color.DKGRAY;
		mensagem.tipo_balao = R.drawable.chat_balao_arquivo;
		mensagem.progressVisible =  View.GONE;
		mensagem.iconFileVisible = View.VISIBLE;
		mensagem.mensagemArquivoVisible = View.VISIBLE;
		mensagem.mensagemVisible = View.GONE;
		mensagem.nuvemVisible = View.GONE;
		mensagem.progressDownloadVisible = View.VISIBLE;
		mensagem.thumbArquivoVisible = View.GONE;
		
				
        
        
        mensagem.setMensagem(dataFile.getName());  
        mensagem.setTamanhoBytes(dataFile.length());    
        mensagem.textoArquivo = "<b>"+mensagem.getUsuario().getNome()+"</b><br/><u>Adicionou um arquivo:<br/>"+mensagem.getMensagem()+"</u>";

		mensagem.nuvemText = DadosUtil.convertHumanReadableByteCount(dataFile.length(),false);
    	
		try {
			String hashArquivo = MD5CheckSum.getMD5Checksum(dataFile.getAbsolutePath());
			mensagem.setHashArquivo(hashArquivo);
		} catch (Exception e) {
			Toast.makeText(MainActivity.instance, "Arquivo corrompido. por favor tente novamente.", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
				
		MainActivity.instance.dbMensagemAdapter.storeMensagemInDataBase(mensagem);
				
		idUsuarioUltimaMensagem = mensagem.getUsuario().getId();
		
		adapter.add(mensagem);	
		
				
		new UploadFileToURL(mensagem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	
	}
	
	
	public synchronized void buildMensagem(Mensagem mensagem,boolean topo){
				
		if(mensagem.getTipoAcao() != Constantes.ACAO_UPLOAD){
			
			if(mensagem.getUsuario().getId().equals(DadosUtil.usuarioLogado.getId())){				
				mensagem.tipo_balao = R.drawable.chat_balao_direita;
				mensagem.alinhamento = Gravity.RIGHT;
				mensagem.fontColor = Color.WHITE;
				
			}else{
				mensagem.tipo_balao = R.drawable.chat_balao_esquerda;
				mensagem.alinhamento = Gravity.LEFT;
				mensagem.fontColor = Color.DKGRAY;
			}
			
			if(!mensagem.getUsuario().getId().equals(idUsuarioUltimaMensagem)){
				mensagem.textoHeader = mensagem.getUsuario().getNome()+ " - "+sdf.format(mensagem.getDataCadastro());
				mensagem.headerVisible =  View.VISIBLE;
			}else{
				mensagem.headerVisible =  View.GONE;
			}
			
			if(mensagem.getIdWebMensagem() != null && mensagem.getIdWebMensagem() != 0l){
				mensagem.progressVisible =  View.GONE;
			}else{
				mensagem.progressVisible =  View.VISIBLE;
			}
			
			// SE EXISTIR UM HASH E PORQUE EXISTE ARQUIVO
			if(mensagem.getHashArquivo() == null){
				mensagem.nuvemVisible = View.GONE;
				mensagem.iconFileVisible = View.GONE;
				mensagem.progressDownloadVisible = View.GONE;
				mensagem.thumbArquivoVisible = View.GONE;
				mensagem.mensagemArquivoVisible = View.GONE;
				mensagem.mensagemVisible = View.VISIBLE;
			}else{
				mensagem.nomeArquivo = mensagem.getMensagem();
				mensagem.tipo_balao = R.drawable.chat_balao_arquivo;
				mensagem.iconFileVisible = View.VISIBLE;
				mensagem.mensagemArquivoVisible = View.VISIBLE;
				mensagem.mensagemVisible = View.GONE;
				mensagem.alinhamento = Gravity.CENTER;
				mensagem.fontColor = Color.DKGRAY;
				
				mensagem.textoArquivo = "<b>"+mensagem.getUsuario().getNome()+"</b><br/><u>Adicionou um arquivo:<br/>"+mensagem.getMensagem()+"</u>";
				
				File dataFile = mensagem.getCaminhoArquivo() == null ? null : new File(mensagem.getCaminhoArquivo());
	                        
	            // CHECA SE DOWNLOAD FOI CONCLUIDO
	            if(dataFile != null && dataFile.exists() && (dataFile.length() == mensagem.getTamanhoBytes().longValue())){
	            	
	    			mensagem.nuvemVisible = View.GONE;
	    			mensagem.progressDownloadVisible = View.GONE;
	    			mensagem.thumbArquivoVisible = View.VISIBLE;
	
	        		setImagemThumbnail(mensagem,dataFile.getAbsolutePath());
					
	            }else{
	        		
	    			mensagem.nuvemVisible = View.VISIBLE;
	    			mensagem.progressDownloadVisible = View.GONE;
	    			mensagem.thumbArquivoVisible = View.GONE;
	    			mensagem.nuvemText = DadosUtil.convertHumanReadableByteCount(mensagem.getTamanhoBytes(),false);
	        	}
				
				
				if(mensagem.getTipoAcao() == Constantes.ACAO_DOWNLOAD){
					if(Constantes.listaMensagensDownload.contains(mensagem)){
						mensagem = Constantes.listaMensagensDownload.get(Constantes.listaMensagensDownload.indexOf(mensagem));
					}
				}
			}
			
			
			if(mensagem.getIdWebMensagem() == null || mensagem.getIdWebMensagem() == 0l){
				if(!Constantes.listaEnviarMensagens.contains(mensagem)){
					Constantes.listaEnviarMensagens.add(mensagem);
				}
			}
						
			idUsuarioUltimaMensagem = mensagem.getUsuario().getId();
			
			int position = adapter.getPosition(mensagem);
			if(position == -1){	
				if(!topo){
					adapter.add(mensagem);	
				}else{
					adapter.insert(mensagem, 0);	
				}	
			}

			
		}else{
			
			mensagem.alinhamento = Gravity.CENTER;
			mensagem.fontColor = Color.DKGRAY;
			mensagem.tipo_balao = R.drawable.chat_balao_arquivo;
			mensagem.iconFileVisible = View.VISIBLE;
			mensagem.mensagemArquivoVisible = View.VISIBLE;
			mensagem.mensagemVisible = View.GONE;
			mensagem.progressVisible =  View.GONE;

						
			if(!mensagem.getUsuario().getId().equals(idUsuarioUltimaMensagem)){
				mensagem.textoHeader = mensagem.getUsuario().getNome()+ " - "+sdf.format(mensagem.getDataCadastro());
				mensagem.headerVisible =  View.VISIBLE;
			}else{
				mensagem.headerVisible =  View.GONE;
			}
			
			mensagem.nomeArquivo = mensagem.getMensagem();		
			mensagem.textoArquivo = "<b>"+mensagem.getUsuario().getNome()+"</b><br/><u>Adicionou um arquivo:<br/>"+mensagem.getMensagem()+"</u>";
			
            File dataFile = new File(mensagem.getCaminhoArquivo());
                        
            if(dataFile.exists() && mensagem.getStatusAcao() == Constantes.STATUS_ACAO_COMPLETO){
            	
    			mensagem.nuvemVisible = View.GONE;
    			mensagem.progressDownloadVisible = View.GONE;
    			mensagem.thumbArquivoVisible = View.VISIBLE;

        		setImagemThumbnail(mensagem,dataFile.getAbsolutePath());
        		
    			if(mensagem.getIdWebMensagem() == null || mensagem.getIdWebMensagem() == 0l){
    				if(!Constantes.listaEnviarMensagens.contains(mensagem)){
    					Constantes.listaEnviarMensagens.add(mensagem);
    				}				
    			}
				
            }else{
        		
    			mensagem.nuvemVisible = View.VISIBLE;
    			mensagem.progressDownloadVisible = View.GONE;
    			mensagem.thumbArquivoVisible = View.GONE;
    			mensagem.nuvemText = DadosUtil.convertHumanReadableByteCount(mensagem.getTamanhoBytes(),false);
        	}
            
			if(Constantes.listaMensagensUpload.contains(mensagem)){
				mensagem = Constantes.listaMensagensUpload.get(Constantes.listaMensagensUpload.indexOf(mensagem));
			}			
						
			idUsuarioUltimaMensagem = mensagem.getUsuario().getId();
			if(!topo){
				adapter.add(mensagem);	
			}else{
				adapter.insert(mensagem, 0);	
			}
			
		
		}
		
	}
	
	
	public void onDestroyView() {

		cancelouExecucao = true;
		
		super.onDestroyView();
	}
	
	private class MensagemArrayAdapter extends ArrayAdapter<Mensagem>{
		public MensagemArrayAdapter(Context context){
			super(context, 0);
		}
		

		public View getView(int position, View convertView, ViewGroup parent){		
			
			final Mensagem mensagem = adapter.getItem(position);
			
			if(convertView == null){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mensagem, parent,false);
				
				mensagem.holder = new ViewHolder();
		
				mensagem.holder.mensagem_layout = (LinearLayout)convertView.findViewById(R.id.tv_layout);
				
				mensagem.holder.nome_hora = (TextView)convertView.findViewById(R.id.nome_hora);
				
				mensagem.holder.progressEnvio = (ProgressBar)convertView.findViewById(R.id.progress_msgEnvio);
				
				mensagem.holder.balao = (LinearLayout)convertView.findViewById(R.id.mensagem_balao);		
				
				mensagem.holder.mensagem_text = (TextView)convertView.findViewById(R.id.mensagem_text);
				
				mensagem.holder.mensagem_text_arquivo = (TextView)convertView.findViewById(R.id.mensagem_text_arquivo);
													
				mensagem.holder.thumbDownload = (ImageView)convertView.findViewById(R.id.abrir_download); 
				
				mensagem.holder.iconFile = (ImageView)convertView.findViewById(R.id.icon_file); 
				
				mensagem.holder.downloadProgressBar = (CircularProgressBar) convertView.findViewById(R.id.circularprogressbar);
				
				mensagem.holder.nuvemAcao = (TextView)convertView.findViewById(R.id.nuvem); 
				
				convertView.setTag(mensagem.holder);
				
			}else{
				mensagem.holder = (ViewHolder) convertView.getTag();
			}
			
			if(mensagem.getTipoAcao() == Constantes.ACAO_UPLOAD){
				mensagem.holder.nuvemAcao.setBackgroundResource(R.drawable.icon_upload);
				mensagem.holder.downloadProgressBar.setProgressColor(MainActivity.instance.getResources().getColor(R.color.progressLaranja));
				mensagem.holder.nuvemAcao.setPadding(0, ((int)ImagemUtils.convertDpToPixel(35f, MainActivity.instance)), 0, 0);
			}else{
				mensagem.holder.nuvemAcao.setBackgroundResource(R.drawable.icon_download);
				mensagem.holder.downloadProgressBar.setProgressColor(MainActivity.instance.getResources().getColor(R.color.progressVerde));
				mensagem.holder.nuvemAcao.setPadding(0, 0, 0, ((int)ImagemUtils.convertDpToPixel(5f, MainActivity.instance)));
			}

			
			mensagem.holder.mensagem_layout.setGravity(mensagem.alinhamento);									
			
			mensagem.holder.nome_hora.setText(mensagem.textoHeader);
			mensagem.holder.nome_hora.setVisibility(mensagem.headerVisible);			
			
			mensagem.holder.progressEnvio.setVisibility(mensagem.progressVisible);		
			mensagem.holder.iconFile.setVisibility(mensagem.iconFileVisible);		
		
			mensagem.holder.balao.setBackgroundResource(mensagem.tipo_balao);			
			
			mensagem.holder.mensagem_text.setText(mensagem.getMensagem());						
			mensagem.holder.mensagem_text.setTextColor(mensagem.fontColor);
			mensagem.holder.mensagem_text.setVisibility(mensagem.mensagemVisible);		

			if(mensagem.textoArquivo != null){
				mensagem.holder.mensagem_text_arquivo.setText(Html.fromHtml(mensagem.textoArquivo));									
			}
			mensagem.holder.mensagem_text_arquivo.setTextColor(mensagem.fontColor);
			mensagem.holder.mensagem_text_arquivo.setVisibility(mensagem.mensagemArquivoVisible);	
			
			mensagem.holder.thumbDownload.setVisibility(mensagem.thumbArquivoVisible);
			mensagem.holder.thumbDownload.setImageBitmap(mensagem.thumbImage);
			
			mensagem.holder.nuvemAcao.setText(mensagem.nuvemText);
			mensagem.holder.nuvemAcao.setVisibility(mensagem.nuvemVisible);
			
			mensagem.holder.downloadProgressBar.setVisibility(mensagem.progressDownloadVisible);
			mensagem.holder.downloadProgressBar.setTitleProgress("X");
		//	mensagem.holder.downloadProgressBar.setTitleProgress(mensagem.titleProgress);
		//	mensagem.holder.downloadProgressBar.setSubTitleProgress(mensagem.subTitleProgress);
		//	mensagem.holder.downloadProgressBar.setSubTitleProgress("X");
			mensagem.holder.downloadProgressBar.setProgress(mensagem.progress);
			
			mensagem.holder.nuvemAcao.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mensagem.nuvemVisible = View.GONE;
					mensagem.holder.nuvemAcao.setVisibility(mensagem.nuvemVisible);					
					mensagem.progressDownloadVisible = View.VISIBLE;
					mensagem.holder.downloadProgressBar.setVisibility(mensagem.progressDownloadVisible);
					
					if(mensagem.getTipoAcao() == Constantes.ACAO_DOWNLOAD){
						new DownloadFileFromURL(mensagem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}
					else if(mensagem.getTipoAcao() == Constantes.ACAO_UPLOAD){
						new UploadFileToURL(mensagem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}
					handler.sendEmptyMessage(0);
			
				}
			});


			mensagem.holder.thumbDownload.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					File dataFile = new File(mensagem.getCaminhoArquivo());
										
					String type = DadosUtil.getExtensionFromUrl(dataFile.getAbsolutePath());
					
				    if (type == null)
				        type = "*/*";

				    Intent intent = new Intent(Intent.ACTION_VIEW);
				    Uri data = Uri.fromFile(dataFile);

				    intent.setDataAndType(data, type);

				    startActivity(intent);
				
				}
			});
            
            mensagem.holder.downloadProgressBar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mensagem.acaoCancelada = true;
				}
			});
            			
			return convertView;
		}
	}
	

	@Override
	public void onLoginProcessStart() {		
	}

	@Override
	public void onLoginProcessSuccess(Usuario usuarioLogado) {
		
		MainActivity.instance.runOnUiThread(new Runnable() {
		       
		public void run() { 
				
			stopProgress();	
			
			if(carregarMensagens){
				carregarMensagens();
			}
				
		  }
	    });
	}

	@Override
	public void onLoginProcessFail(String message) {
		
		MainActivity.instance.runOnUiThread(new Runnable() {
       
			public void run() { 
			layoutMensagemErro.setVisibility(View.VISIBLE);					
			layoutMensagemErro.setOnClickListener(new View.OnClickListener(){
			    @Override
			    public void onClick(View v){
			    	layoutMensagemErro.setVisibility(View.GONE);	
		    		startProgress();
		    		MainActivity.instance.automaticLogin();
			    }
			});
			
			stopProgress();	

		    }
	    });
				
	}
	
	
	public void startProgress(){
		TextView aluno = (TextView) fl.findViewById(R.id.aluno_select_nome);					
		aluno.setText("Aguarde...");
		progress.setVisibility(View.VISIBLE);

	}
	
	public void stopProgress(){		
		TextView aluno = (TextView) fl.findViewById(R.id.aluno_select_nome);
		aluno.setText(DadosUtil.alunoSelecionado.getNome());
		progress.setVisibility(View.GONE);
	}
	
	
    public void setImagemThumbnail(Mensagem mensagem,String urlPath){
    		
		String type = DadosUtil.getExtensionFromUrl(urlPath) == null ? "" : DadosUtil.getExtensionFromUrl(urlPath);

		if(type.startsWith("video")){
			mensagem.thumbImage = ThumbnailUtils.createVideoThumbnail(urlPath, MediaStore.Video.Thumbnails.MICRO_KIND);
		}else if(type.startsWith("image")){
			mensagem.thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(urlPath), 64, 64);
		}else if(type.startsWith("audio")){
			mensagem.thumbImage = BitmapFactory.decodeResource(MainActivity.instance.getResources(),R.drawable.icon_music);
		}else{
			mensagem.thumbImage = BitmapFactory.decodeResource(MainActivity.instance.getResources(),R.drawable.icon_open);
		}
		
    }
	

    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {

            if (requestCode == Constantes.TAKE_PICTURE_VIDEO) {           	
            	
            	sendFile(uriFileOriginal);
            	      
            } else if (requestCode == Constantes.SELECT_FILE) {                 
            	uriFileOriginal = data.getData();            	
               	sendFile(uriFileOriginal);
            }

            DadosUtil.scanFile(DadosUtil.getRealPathFromURI(uriFileOriginal,MainActivity.instance));
        }
    }
    

    class DownloadFileFromURL extends AsyncTask<Void, String, String> {
 
    	private Mensagem mensagem;

    	public DownloadFileFromURL(Mensagem mensagem) {		    		
    		super();
    		this.mensagem = mensagem;
    		mensagem.acaoCancelada = false;
		}
    	
    	public DownloadFileFromURL(){
    		super();
    	}
    	
        @Override
        protected void onPreExecute() {
        	Constantes.listaMensagensDownload.add(mensagem);
            super.onPreExecute();     
        }
 
        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(Void... args) {
   
            long inicio = System.currentTimeMillis();
            Socket sock = null;
            OutputStream output = null;
           
                  
            Log.i("","INICIO DOWNLOAD SOCKET");
            try {
            	
                if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(MainActivity.instance, "Erro ao acessar diretorio. O arquivo não pode ser salvo.", Toast.LENGTH_SHORT).show();
                	return null;
                }
                
                File rootPath = new File(Environment.getExternalStorageDirectory()+File.separator+Constantes.DIRETORIO_ROOT+File.separator+Constantes.DIRETORIO_MEDIA);

                if(!rootPath.exists()) {
                    if(!rootPath.mkdirs()){                  	
                        Toast.makeText(MainActivity.instance, "Erro ao acessar diretorio. O arquivo não pode ser salvo.", Toast.LENGTH_SHORT).show();
                    	return null;
                    }
                }
                
        		String type = DadosUtil.getExtensionFromUrl(mensagem.nomeArquivo) == null ? "" : DadosUtil.getExtensionFromUrl(mensagem.nomeArquivo);
      			
        		if(type.startsWith("video")){
					
    				rootPath = new File(rootPath,Constantes.DIRETORIO_VIDEOS);
                    if(!rootPath.exists()) {
                        if(!rootPath.mkdirs()){                  	
                            Toast.makeText(MainActivity.instance, "Erro ao acessar diretorio. O arquivo não pode ser salvo.", Toast.LENGTH_SHORT).show();
                        	return null;
                        }
                    }
					
        		}else if(type.startsWith("image")){
					
				
					
    				rootPath = new File(rootPath,Constantes.DIRETORIO_IMAGES);
                    if(!rootPath.exists()) {
                        if(!rootPath.mkdirs()){                  	
                            Toast.makeText(MainActivity.instance, "Erro ao acessar diretorio. O arquivo não pode ser salvo.", Toast.LENGTH_SHORT).show();
                        	return null;
                        }
                    }
					
					
					
        		}else if(type.startsWith("audio")){
					
    				rootPath = new File(rootPath,Constantes.DIRETORIO_AUDIO);
                    if(!rootPath.exists()) {
                        if(!rootPath.mkdirs()){                  	
                            Toast.makeText(MainActivity.instance, "Erro ao acessar diretorio. O arquivo não pode ser salvo.", Toast.LENGTH_SHORT).show();
                        	return null;
                        }
                    }
                    
        		}else{
     			
    				rootPath = new File(rootPath,Constantes.DIRETORIO_GENERICO);
                    if(!rootPath.exists()) {
                        if(!rootPath.mkdirs()){                  	
                            Toast.makeText(MainActivity.instance, "Erro ao acessar diretorio. O arquivo não pode ser salvo.", Toast.LENGTH_SHORT).show();
                        	return null;
                        }
                    }
					
				}
        			
        		     		
        		File dataFile = new File(rootPath,mensagem.nomeArquivo);
        		
                long totalRead = 0;
                
                if(dataFile.exists()){
                	totalRead = dataFile.length();
                }
            	
                SocketAddress sockaddr = new InetSocketAddress(Constantes.IP_CONNECT, 8886);
            	sock = new Socket();
            	sock.connect(sockaddr, 15000);
            	
            	String dados = mensagem.getTipoAcao()+";"+totalRead+";"+DadosUtil.usuarioLogado.getId().toString()+";"+
            	mensagem.getIdWebMensagem()+";"+
            	DadosUtil.usuarioLogado.getDataAtualizacao().toString()+";"+
            	DadosUtil.usuarioLogado.getSecurityHashKey();
            	
            	byte[] byteDados =  Crypto.encrypt(dados.getBytes("UTF-8"));
            	
            	DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            	
            	out.writeInt(byteDados.length);
            	out.write(byteDados);                 
            	out.flush();
                
                int bytesRead;

                DataInputStream input = new DataInputStream(sock.getInputStream());

                String result = input.readUTF();
                
                if(!result.equals("OK")){
                    sock.shutdownInput();
                    sock.shutdownOutput();
                    sock.close();
                	return null;
                }
                
           
                byte[] buffer = new byte[Constantes.TAMANHO_BUFFER];              
                long readSecond = 0;
                long size = input.readLong();
                long originalSize = size;         
                long dataInicioAtualiza = System.currentTimeMillis();    
                long tempoDiferenca;               

                size = size - totalRead;
                
                output = (totalRead==0) ? new FileOutputStream(dataFile) : new FileOutputStream(dataFile,true);
         
                
                while (size > 0 && (bytesRead = input.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                	
                	if(!mensagem.acaoCancelada){
                        output.write(buffer, 0, bytesRead);
                        size -= bytesRead;
                        totalRead += bytesRead;
                        readSecond += bytesRead;
      
                        long dataFimAtualiza = System.currentTimeMillis();
                        
                        tempoDiferenca = dataFimAtualiza - dataInicioAtualiza;
                        
                        if(tempoDiferenca> 1000){//1segundo
                  			 
                  			Log.i("","Total bytes "+DadosUtil.convertHumanReadableByteCount(readSecond,false) +" /segundo");
                  			
                  			publishProgress(""+(int)((totalRead*100)/originalSize),""+totalRead,""+originalSize,""+readSecond);
                  			dataInicioAtualiza = System.currentTimeMillis();
                  			readSecond = 0;
                  			
                        }
                	}else{
                		break;
                	}

                }

                output.flush();
                
                long fim  = System.currentTimeMillis();
                
                if(!mensagem.acaoCancelada){
        			String hashArquivo = MD5CheckSum.getMD5Checksum(dataFile.getAbsolutePath());

        			if(hashArquivo.equals(mensagem.getHashArquivo())){
        				Log.i("","Download Concluido");
        				
        				mensagem.setStatusAcao(Constantes.STATUS_ACAO_COMPLETO);
        				
        				return dataFile.getAbsolutePath();
        			}else{       			
        				mensagem.setStatusAcao(Constantes.STATUS_ACAO_INCOMPLETO);
        				output.close();
        				dataFile.delete();
                    	MainActivity.instance.runOnUiThread(new Runnable() {					
        					@Override
        					public void run() {
                            	Toast.makeText(MainActivity.instance, "Arquivo corrompido. por favor tente novamente.", Toast.LENGTH_SHORT).show();
        					}
        				});
        			}
                }else{
                	mensagem.setStatusAcao(Constantes.STATUS_ACAO_CANCELADO);
                }
                
                Log.i("","TEMPO DOWNLOAD SOCKET = "+ (fim - inicio)/1000 +" Segundos");
              
               
                
            } catch (IOException e) {
            	MainActivity.instance.runOnUiThread(new Runnable() {					
					@Override
					public void run() {
		            	Toast.makeText(MainActivity.instance, "Falha conectando-se ao servidor. por favor tenta novamente mais tarde.", Toast.LENGTH_SHORT).show();						
					}
				});
             } catch (Exception e) {
            	MainActivity.instance.runOnUiThread(new Runnable() {					
					@Override
					public void run() {
		            	Toast.makeText(MainActivity.instance, "Aconteceu uma falha inesperada. Por favor reporte o erro a equipe ischool.", Toast.LENGTH_SHORT).show();						
					}
				});
            }finally{
            	try {            		
    				if(sock != null){
                        sock.shutdownInput();
                        sock.shutdownOutput();
                        sock.close();
    				} 
    				if(output != null){
    					output.close();
    				}
    			} catch (IOException e) {
    				e.printStackTrace();
    			}

            }
 
            return "";
        }
 
        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {

        	//mensagem.titleProgress = Integer.parseInt(progress[0]) + "%";        	
        	//mensagem.subTitleProgress = DadosUtil.convertHumanReadableByteCount(Long.valueOf(progress[3]),true)+"/s";
        	mensagem.progress = Integer.parseInt(progress[0]);
        	
        	mensagem.holder.downloadProgressBar.setTitleProgress("X");
           // mensagem.holder.downloadProgressBar.setTitleProgress(mensagem.titleProgress);
          //  mensagem.holder.downloadProgressBar.setSubTitleProgress(mensagem.subTitleProgress);
         //   mensagem.holder.downloadProgressBar.setSubTitleProgress("X");
            mensagem.holder.downloadProgressBar.setProgress(mensagem.progress);
            if(!isAdded()){
            	Log.i("", "NAO ATACHADO");
            	 handler.sendEmptyMessage(0);
            }else{
            	Log.i("", "ATACHADO");
            }
           
       }
 
        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
        	        	
        	if(mensagem.getStatusAcao() == Constantes.STATUS_ACAO_COMPLETO){
        		setImagemThumbnail(mensagem,file_url);
    			mensagem.nuvemVisible = View.GONE;
    			mensagem.progressDownloadVisible = View.GONE;
    			mensagem.thumbArquivoVisible = View.VISIBLE;
    			
            	mensagem.setCaminhoArquivo(file_url);    			
    			handler.sendEmptyMessage(0);
    			
    			DadosUtil.scanFile(file_url);
    			
    			
        	}else{
				mensagem.nuvemVisible = View.VISIBLE;
				mensagem.holder.nuvemAcao.setVisibility(mensagem.nuvemVisible);
				
				mensagem.progressDownloadVisible = View.GONE;
				mensagem.holder.downloadProgressBar.setVisibility(mensagem.progressDownloadVisible);
				handler.sendEmptyMessage(0);
        	}
        	
        	if(mensagem.getStatusAcao() != Constantes.STATUS_ACAO_INCOMPLETO){
            	MainActivity.instance.dbMensagemAdapter.storeMensagemInDataBase(mensagem);
        	}
        	
        	Constantes.listaMensagensDownload.remove(mensagem);
        }
 
    }
    

    class UploadFileToURL extends AsyncTask<Void, String, String> {
    	 
    	private Mensagem mensagem;

    	public UploadFileToURL(Mensagem mensagem) {		    		
    		super();
    		this.mensagem = mensagem;
    		mensagem.acaoCancelada = false;
		}
    	
    	public UploadFileToURL(){
    		super();
    	}
    	
        @Override
        protected void onPreExecute() {
        	Constantes.listaMensagensUpload.add(mensagem);
        	mensagem.setStatusAcao(Constantes.STATUS_ACAO_INCOMPLETO);
            super.onPreExecute();     
        }
 
        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(Void... args) {
   
            long inicio = System.currentTimeMillis();
            
            Socket sock = null;
            
            Log.i("","INICIO UPLOAD SOCKET");
            try {
                              	
                SocketAddress sockaddr = new InetSocketAddress(Constantes.IP_CONNECT, 8886);
            	sock = new Socket();
            	sock.connect(sockaddr, 15000);
            	
            	String dados = mensagem.getTipoAcao()+";"+DadosUtil.alunoSelecionado.getId()+";"+DadosUtil.usuarioLogado.getId().toString()+";"+
            	mensagem.getId()+";"+
            	DadosUtil.usuarioLogado.getDataAtualizacao().toString()+";"+
            	DadosUtil.usuarioLogado.getSecurityHashKey();
            	
            	byte[] byteDados =  Crypto.encrypt(dados.getBytes("UTF-8"));
            	
            	DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            	
            	out.writeInt(byteDados.length);
            	out.write(byteDados);                 
            	out.flush();
            	

                DataInputStream input = new DataInputStream(sock.getInputStream());

                String result = input.readUTF();
                
                if(!result.equals("OK")){
                    sock.shutdownInput();
                    sock.shutdownOutput();
                    sock.close();
                	return null;
                }
	
            	InputStream fis = null;  
                try{               	                   
                    File dataFile = new File(mensagem.getCaminhoArquivo());   	
                	fis = new FileInputStream(dataFile);              	
                    
                    out.writeUTF(dataFile.getName());
                    out.writeLong(dataFile.length());
                    out.writeUTF(mensagem.getHashArquivo());
                	
                    long positionSend = input.readLong();
                                     
                    fis.skip(positionSend);
                                
                    int bytesSend = 0;
                    byte[] buffer = new byte[Constantes.TAMANHO_BUFFER];
                    long readSecond = 0;
                    long size = dataFile.length();
                    long originalSize = size;         
                    long dataInicioAtualiza = System.currentTimeMillis();    
                    long tempoDiferenca;  
                  
                    while ((bytesSend = fis.read(buffer)) != -1) {
                    	
                    	if(!mensagem.acaoCancelada){
                    		out.write(buffer, 0, bytesSend);
                            size -= bytesSend;
                            positionSend += bytesSend;
                            readSecond += bytesSend;
          
                            long dataFimAtualiza = System.currentTimeMillis();
                            
                            tempoDiferenca = dataFimAtualiza - dataInicioAtualiza;
                            
                            if(tempoDiferenca> 1000){//1segundo
                      			 
                      			Log.i("","Total bytes "+DadosUtil.convertHumanReadableByteCount(readSecond,false) +" /segundo");
                      			
                      			publishProgress(""+(int)((positionSend*100)/originalSize),""+positionSend,""+originalSize,""+readSecond);
                      			dataInicioAtualiza = System.currentTimeMillis();
                      			readSecond = 0;                     			
                            }
                    	}else{
                    		break;
                    	}
                    }
                    
                	
                }catch(FileNotFoundException ex){
                    Toast.makeText(MainActivity.instance, "Erro ao acessar arquivo. O arquivo não existe.", Toast.LENGTH_SHORT).show();
                	return "";
                }finally{
                	fis.close();
                }
                

                if(!mensagem.acaoCancelada){

                	result = input.readUTF();
                	
                    if(result.equals("OK")){
                    	
                    	mensagem.setStatusAcao(Constantes.STATUS_ACAO_COMPLETO);
                    	
                        byte[] buff  = new byte[input.readInt()]; 
                        input.read(buff, 0, buff.length);
                    	String dadosMsg[] = new String(Crypto.decrypt(buff)).split(";");
                    	
						mensagem.setIdWebMensagem(Long.valueOf(dadosMsg[0]));
						mensagem.setDataCadastro(Constantes.dateFormat.parse(dadosMsg[1]));
						
						Log.i("","Upload Concluido");
                    	
                    }else{
                    	mensagem.setStatusAcao(Constantes.STATUS_ACAO_INCOMPLETO);
                    	MainActivity.instance.runOnUiThread(new Runnable() {					
        					@Override
        					public void run() {
                            	Toast.makeText(MainActivity.instance, "Arquivo corrompido. por favor tente novamente.", Toast.LENGTH_SHORT).show();
        					}
        				});
                    }

                }else{
                	mensagem.setStatusAcao(Constantes.STATUS_ACAO_CANCELADO);
                }

                
                long fim  = System.currentTimeMillis();   
                Log.i("","TEMPO UPLOAD SOCKET = "+ (fim - inicio)/1000 +" Segundos");
                

                return mensagem.getCaminhoArquivo();
                
            } catch (IOException e) {
            	MainActivity.instance.runOnUiThread(new Runnable() {					
					@Override
					public void run() {
		            	Toast.makeText(MainActivity.instance, "Falha conectando-se ao servidor. por favor tenta novamente mais tarde.", Toast.LENGTH_SHORT).show();						
					}
				});
             } catch (Exception e) {
            	MainActivity.instance.runOnUiThread(new Runnable() {					
					@Override
					public void run() {
		            	Toast.makeText(MainActivity.instance, "Aconteceu uma falha inesperada. Por favor reporte o erro a equipe ischool.", Toast.LENGTH_SHORT).show();						
					}
				});
            }finally{
            	try {            		
    				if(sock != null){
                        sock.shutdownInput();
                        sock.shutdownOutput();
                        sock.close();
    				}     		   
    			} catch (IOException e) {
    				e.printStackTrace();
    			}

            }
 
            return "";
        }
 
        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {

        	//mensagem.titleProgress = Integer.parseInt(progress[0]) + "%";        	
        	//mensagem.subTitleProgress = DadosUtil.convertHumanReadableByteCount(Long.valueOf(progress[3]),true)+"/s";
        	mensagem.progress = Integer.parseInt(progress[0]);
        	
        	mensagem.holder.downloadProgressBar.setTitleProgress("X");
           // mensagem.holder.downloadProgressBar.setTitleProgress(mensagem.titleProgress);
          //  mensagem.holder.downloadProgressBar.setSubTitleProgress(mensagem.subTitleProgress);
         //   mensagem.holder.downloadProgressBar.setSubTitleProgress("X");
            mensagem.holder.downloadProgressBar.setProgress(mensagem.progress);
            handler.sendEmptyMessage(0);
       }
 

        @Override
        protected void onPostExecute(String file_url) {
        		
        	
        	if(mensagem.getStatusAcao() == Constantes.STATUS_ACAO_COMPLETO){

        		setImagemThumbnail(mensagem,file_url);
    			mensagem.nuvemVisible = View.GONE;
    			mensagem.progressDownloadVisible = View.GONE;
    			mensagem.thumbArquivoVisible = View.VISIBLE;
    			MensagemFragment.instance.listaBanco.add(mensagem);    			
    			handler.sendEmptyMessage(0);

        	}else{
				mensagem.nuvemVisible = View.VISIBLE;
				mensagem.holder.nuvemAcao.setVisibility(mensagem.nuvemVisible);
				
				mensagem.progressDownloadVisible = View.GONE;
				mensagem.holder.downloadProgressBar.setVisibility(mensagem.progressDownloadVisible);
				handler.sendEmptyMessage(0);
        	}
        	
        	if(mensagem.getStatusAcao() != Constantes.STATUS_ACAO_INCOMPLETO){
            	MainActivity.instance.dbMensagemAdapter.storeMensagemInDataBase(mensagem);
        	}
        	        	
        	Constantes.listaMensagensUpload.remove(mensagem);
        	
        }
 
    }

}
