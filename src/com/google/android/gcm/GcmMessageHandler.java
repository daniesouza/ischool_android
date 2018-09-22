package com.google.android.gcm;

import java.util.Calendar;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import br.com.marrs.ischool.MainActivity;
import br.com.marrs.ischool.R;
import br.com.marrs.ischool.dao.DbAlunoAdapter;
import br.com.marrs.ischool.entidades.Aluno;
import br.com.marrs.ischool.util.Constantes;
import br.com.marrs.ischool.util.DadosUtil;
import br.com.marrs.ischool.view.AgendaFragment;
import br.com.marrs.ischool.view.mensagens.MensagemFragment;

public class GcmMessageHandler extends IntentService{

	DbAlunoAdapter dbAlunoAdapter;

	public GcmMessageHandler(){
		super("GcmMessageHandler");
		dbAlunoAdapter = new DbAlunoAdapter(this);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		
		String serverMsg 	= extras.getString("serverMsg");
		String idEventoExec = extras.getString("idEventoExec");
		String alunoId 		= extras.getString("aluno_id");
		//String usuarioId 	= extras.getString("usuario_id");
		String tipoMsg 		= extras.getString("tipoMsg"); 
		
		GcmReceiver.completeWakefulIntent(intent);
		
		SharedPreferences prefs = getSharedPreferences(Constantes.ISCHOOL_SHARED_PREF, Context.MODE_PRIVATE);
		
		
		Long idUsuario = prefs.getLong(Constantes.ID_LOGGED_USER, 0);
		
		handleNotification(serverMsg, idEventoExec, alunoId,idUsuario,tipoMsg);	
		
    	Log.i("","MENSAGEM RECEBIDA  "+serverMsg);
		
	}


    
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private synchronized void handleNotification(final String serverMsg,String idEventoExec,String alunoId,Long idUsuario,String tipoMsg){

    	Long idALuno	= Long.valueOf(alunoId);
    	Fragment fragment = (Fragment) (MainActivity.instance != null ? MainActivity.instance.getSupportFragmentManager().findFragmentById(R.id.frame_container) : null);

    	if(tipoMsg.equals(Constantes.TIPO_MSG_TEXTO)){   		
			processMensagemNotification(fragment, idALuno, serverMsg, idEventoExec, idUsuario, tipoMsg);
		}else{
			processEventoNotification(fragment, idALuno, serverMsg, idEventoExec, idUsuario, tipoMsg);
		}
    				
    }
    
    
    private synchronized void processEventoNotification(Fragment fragment,Long idALuno,String serverMsg,String idEventoExec,Long idUsuario,final String tipoMsg){
    	
		String frag = "evento";
		String titulo = "Novos Itens na agenda";
		
		if(tipoMsg.equals(Constantes.TIPO_MSG_LEMBRETE)){
			titulo = "Novo lembrete na agenda";
		}else if(tipoMsg.equals(Constantes.TIPO_MSG_EVENTO)){
			titulo = "Novo evento na agenda";
		}else if(tipoMsg.equals(Constantes.TIPO_MSG_EVENTO_CANC)){
			titulo = "Evento cancelado na agenda";
		}else if(tipoMsg.equals(Constantes.TIPO_MSG_LEMBRETE_CANC)){
			titulo = "Lembrete cancelado na agenda";
		}
		

		// SO CAIRA NESTE IF SE A TELA ESTIVER EM USO E SE A TELA FOR A TELA DE EVENTOS E SE O USUARIO ESTIVER LOGADO
		// DO CONTRARIO AUMENTA A QUANTIDADE DE EVENTOS NOVOS E NOTIFICA O PAI
    	if (fragment != null && fragment.isResumed() && fragment instanceof AgendaFragment && DadosUtil.usuarioLogado != null) {

    		// CAI NESTE IF SE O PAI ESTIVER EXATAMENTE VENDO A AGENDA DO FILHO SELECIONADO
    		// DO CONTRARIO AUMENTA A QUANTIDADE DE EVENTOS NOVOS E NOTIFICA O PAI
    		if(DadosUtil.alunoSelecionado != null && DadosUtil.alunoSelecionado.getId().equals(idALuno)){
    			
	    	    final AgendaFragment agendaFragment = (AgendaFragment) fragment;
	    	    agendaFragment.getActivity().runOnUiThread(new Runnable() {
	    	        public void run() {    
    	                try{    	                	    	            					
	            			if(tipoMsg.equals(Constantes.TIPO_MSG_LEMBRETE) || tipoMsg.equals(Constantes.TIPO_MSG_LEMBRETE_CANC)){
	            				agendaFragment.mViewPager.setCurrentItem(1);
	            			}else{
	            				agendaFragment.mViewPager.setCurrentItem(0);
	            			}
	            			DadosUtil.dataSelecionada = Calendar.getInstance();
    	                   	agendaFragment.carregarAgenda();
    	                }
    	                catch(Exception e){}
	    	        }
	    	   });
    			
    		}else{
    					    	   
				for(Aluno aluno:DadosUtil.usuarioLogado.getAlunos()){
	    	    	if(aluno.getId().equals(idALuno)){		    
	    	    		aluno.setQuantidadeMensagensNovas(aluno.getQuantidadeMensagensNovas()+1);
	    	    		aluno.setMensagemDescricaoMensagem(serverMsg);
	    	    		dbAlunoAdapter.storeAlunoNewMensagens(aluno);  
	    	    		
			    		break;
	    	    	}
	    	    }
    			
	    	    gerarNotificacao(titulo,serverMsg,idEventoExec,idALuno,idUsuario,frag,NotificationHelper.eventoNotifications,tipoMsg);    			

    		}

    	}else{
    		
    		// SE O USUARIO ESTIVER LOGADO(APP FUNCIONANDO) IRA PEGAR A LISTA DE ALUNOS NA MEMORIA E AUMENTAR A QUANTIDADE DE EVENTOS E NOTIFICAR
    		// DO CONTRARIO VAI BUSCAR NA BASE O ALUNO PELO ID ALUNO VINDO DO WS
    		if(DadosUtil.usuarioLogado != null){
    			if(DadosUtil.usuarioLogado.getAlunos() != null && !DadosUtil.usuarioLogado.getAlunos().isEmpty()){
		    	    for(Aluno aluno:DadosUtil.usuarioLogado.getAlunos()){
		    	    	if(aluno.getId().equals(idALuno)){		
		    	    		aluno.setQuantidadeMensagensNovas(aluno.getQuantidadeMensagensNovas()+1);
		    	    		aluno.setMensagemDescricaoMensagem(serverMsg);
		    	    		dbAlunoAdapter.storeAlunoNewMensagens(aluno);    	    		
				    		break;
		    	    	}
		    	    }
    			}
    		}else{
    			
    			Aluno alunoNotificar = dbAlunoAdapter.returnById(idALuno);
    			alunoNotificar.setQuantidadeMensagensNovas(alunoNotificar.getQuantidadeMensagensNovas()+1);
	    		alunoNotificar.setMensagemDescricaoMensagem(serverMsg);
	    		dbAlunoAdapter.storeAlunoNewMensagens(alunoNotificar);
    		}

    	 // SO CAIRA NESTE IF SE A TELA ESTIVER EM USO E SE A TELA FOR A TELA DE LISTA DE ALUNOS E SE O USUARIO ESTIVER LOGADO
//    	    if (fragment != null && fragment.isResumed() && fragment instanceof AlunoFragment && DadosUtil.usuarioLogado != null) {
//    	    	
//    	    	  final AlunoFragment alunoFragment = (AlunoFragment) fragment;
//    	    	   alunoFragment.getActivity().runOnUiThread(new Runnable() {
//		    	        public void run() {    
//		    	                try{
//	    		    	    	  if(alunoFragment.adapter != null && !alunoFragment.adapter.isEmpty()){
//	    		    	    		  alunoFragment.adapter.notifyDataSetChanged();
//	    		    	    	  }
//		    	                }
//		    	                catch(Exception e){}
//		    	        }
//    	    	   });
//    	    	  	    	  
//    	    }
    		
    		 gerarNotificacao(titulo,serverMsg,idEventoExec,idALuno,idUsuario,frag,NotificationHelper.eventoNotifications,tipoMsg);
    	}
    }
    
    
    private synchronized void processMensagemNotification(Fragment fragment,Long idALuno,String serverMsg,String idEventoExec,Long idUsuario,String tipoMsg){
    	
		String frag = "mensagem";
		
		String titulo = "Nova Mensagem";
		
		
		// SO CAIRA NESTE IF SE A TELA ESTIVER EM USO E SE A TELA FOR A TELA DE MENSAGENS E SE O USUARIO ESTIVER LOGADO
		// DO CONTRARIO AUMENTA A QUANTIDADE DE EVENTOS NOVOS E NOTIFICA O PAI
    	if (fragment != null && fragment.isResumed() && fragment instanceof MensagemFragment && DadosUtil.usuarioLogado != null) {

    		// CAI NESTE IF SE O PAI ESTIVER EXATAMENTE VENDO O CHAT DO FILHO SELECIONADO
    		// DO CONTRARIO AUMENTA A QUANTIDADE DE EVENTOS NOVOS E NOTIFICA O PAI
    		if(DadosUtil.alunoSelecionado != null && DadosUtil.alunoSelecionado.getId().equals(idALuno)){   					    	   
	    	    if(!MensagemFragment.sincronizando){		    	    	
	    	    	final MensagemFragment mensagensFragment = (MensagemFragment) fragment;
		    	    mensagensFragment.getActivity().runOnUiThread(new Runnable() {
		    	        public void run() {    
		    	                try{
		    	                	mensagensFragment.carregarMensagens();
		    	                }
		    	                catch(Exception e){}
		    	        }
		    	   });
	    	    }

    			
    		}else{
    					    	   
				for(Aluno aluno:DadosUtil.usuarioLogado.getAlunos()){
	    	    	if(aluno.getId().equals(idALuno)){		    
	    	    		aluno.setQuantidadeMensagensNovas(aluno.getQuantidadeMensagensNovas()+1);
	    	    		aluno.setMensagemDescricaoMensagem(serverMsg);
	    	    		dbAlunoAdapter.storeAlunoNewMensagens(aluno);
	    	    		
			    		break;
	    	    	}
	    	    }
    			
	    	    gerarNotificacao(titulo,serverMsg,idEventoExec,idALuno,idUsuario,frag,NotificationHelper.mensagensNotifications,tipoMsg);    			

    		}

    	}else{
    		
    		// SE O USUARIO ESTIVER LOGADO(APP FUNCIONANDO) IRA PEGAR A LISTA DE ALUNOS NA MEMORIA E AUMENTAR A QUANTIDADE DE MENSAGENS E NOTIFICAR
    		// DO CONTRARIO VAI BUSCAR NA BASE O ALUNO PELO ID ALUNO VINDO DO WS
    		if(DadosUtil.usuarioLogado != null){
    			if(DadosUtil.usuarioLogado.getAlunos() != null && !DadosUtil.usuarioLogado.getAlunos().isEmpty()){
		    	    for(Aluno aluno:DadosUtil.usuarioLogado.getAlunos()){
		    	    	if(aluno.getId().equals(idALuno)){		
		    	    		aluno.setQuantidadeMensagensNovas(aluno.getQuantidadeMensagensNovas()+1);
		    	    		aluno.setMensagemDescricaoMensagem(serverMsg);
		    	    		dbAlunoAdapter.storeAlunoNewMensagens(aluno);    	    		
				    		break;
		    	    	}
		    	    }
    			}
    		}else{
    			
    			Aluno alunoNotificar = dbAlunoAdapter.returnById(idALuno);
    			alunoNotificar.setQuantidadeMensagensNovas(alunoNotificar.getQuantidadeMensagensNovas()+1);
    			alunoNotificar.setMensagemDescricaoMensagem(serverMsg);
	    		dbAlunoAdapter.storeAlunoNewMensagens(alunoNotificar);
    		}

    	    
    	 // SO CAIRA NESTE IF SE A TELA ESTIVER EM USO E SE A TELA FOR A TELA DE LISTA DE ALUNOS E SE O USUARIO ESTIVER LOGADO
//    	    if (fragment != null && fragment.isResumed() && fragment instanceof AlunoFragment && DadosUtil.usuarioLogado != null) {
//    	    	
//    	    	  final AlunoFragment alunoFragment = (AlunoFragment) fragment;
//    	    	   alunoFragment.getActivity().runOnUiThread(new Runnable() {
//		    	        public void run() {    
//		    	                try{
//	    		    	    	  if(alunoFragment.adapter != null && !alunoFragment.adapter.isEmpty()){
//	    		    	    		  alunoFragment.adapter.notifyDataSetChanged();
//	    		    	    	  }
//		    	                }
//		    	                catch(Exception e){}
//		    	        }
//    	    	   });
//    	    	  	    	  
//    	    }
    		
    		gerarNotificacao(titulo,serverMsg,idEventoExec,idALuno,idUsuario,frag,NotificationHelper.mensagensNotifications,tipoMsg);
    	}
    }
    
    public void gerarNotificacao(String titulo,String mensagem,String idEventoExec,Long idALuno,Long idUsuario,String frag,SparseArray<String> notificationsList,String tipoMsg){
    	
		Intent resultIntent = new Intent(this, MainActivity.class).
				putExtra("frag", frag).
				putExtra("id_aLuno", idALuno).
				putExtra("tipoMsg",tipoMsg);
		
		//resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    	resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		String msgs = notificationsList.get(idALuno.intValue());
		if(msgs == null){
			msgs = "";
		}
		msgs += mensagem+";";
		
		notificationsList.put(idALuno.intValue(), msgs);
		
		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		inboxStyle.setBigContentTitle(titulo);
		
		String[] events = msgs.split(";");
		
		inboxStyle.setSummaryText(events.length+" eventos");
		
		for (int i=0; i < events.length && i<7 ; i++) {		
		    inboxStyle.addLine(Html.fromHtml(events[i]));
		}
		
		
		Bitmap imagemAluno = dbAlunoAdapter.getAlunoImage(idALuno, idUsuario);
		
		if(imagemAluno == null){
			imagemAluno = BitmapFactory.decodeResource(this.getResources(),R.drawable.icon_filho_no_pic);
		}
		
		NotificationCompat.Builder mBuilder = 
				new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setLargeIcon(imagemAluno)
		        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
		        .setAutoCancel(true)
		        .setVibrate(new long[] {300,300,300,300})  
			    //.setLights(Color.RED, 3000, 3000) //LED    
			    //.setSound(Uri.parse("uri://sadfasdfasdf.mp3"))  //Ton
		        .setPriority(NotificationCompat.PRIORITY_MAX)
		        .setContentTitle(titulo)
		        .setContentText(Html.fromHtml(mensagem))
		        .setStyle(inboxStyle);
		
		int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
		
    	PendingIntent resultPendingIntent = PendingIntent.getActivity(this, iUniqueId ,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
    	
    	

		mBuilder.setContentIntent(resultPendingIntent);

		
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


		mNotificationManager.notify(tipoMsg,Integer.valueOf(idALuno.toString()), mBuilder.build());
	}
    
}
