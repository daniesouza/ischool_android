package br.com.marrs.ischool.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.marrs.ischool.LoginListener;
import br.com.marrs.ischool.MainActivity;
import br.com.marrs.ischool.R;
import br.com.marrs.ischool.entidades.EventoExecutado;
import br.com.marrs.ischool.entidades.Usuario;
import br.com.marrs.ischool.util.Constantes;
import br.com.marrs.ischool.util.Crypto;
import br.com.marrs.ischool.util.DadosUtil;
import br.com.marrs.ischool.view.PagerSlidingTabStrip.IconTabProvider;
import br.com.marrs.ischool.webservice.EventoExeCrypt;
import br.com.marrs.ischool.webservice.IschoolWebServiceImplService;
import br.com.marrs.ischool.webservice.VectorByte;
import br.com.marrs.ischool.webservice.VectoreventoExeCrypt;

import com.google.android.gcm.NotificationHelper;

public class AgendaFragment extends Fragment implements LoginListener{


	public SectionsPagerAdapter mSectionsPagerAdapter;	
	public ViewPager mViewPager;

	public EventoFragment eventoFragment = null;
	public LembreteFragment lembreteFragment = null;
	
	private static final int FRAGMENT_ID = 3;
	private LinearLayout fl;
	private List<EventoExecutado> listaNovosEventos;
	private List<EventoExecutado> listaBanco;
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.US);

	private volatile boolean cancelouExecucao;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		MainActivity.instance.loginManager.removeListeners();
		MainActivity.instance.loginManager.addListener(this, AgendaFragment.FRAGMENT_ID);
 		DadosUtil.dataSelecionada = Calendar.getInstance();

		
		View v = inflater.inflate(R.layout.fragment_tab, container, false);
		mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
		
		mViewPager = (ViewPager) v.findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
	    tabs.setViewPager(mViewPager);
	    
		if(getArguments() != null && getArguments().containsKey("tipoMsg")){
			
			String tipoMsg = getArguments().getString("tipoMsg");
			
			if(tipoMsg.equals(Constantes.TIPO_MSG_LEMBRETE) || tipoMsg.equals(Constantes.TIPO_MSG_LEMBRETE_CANC)){
				mViewPager.setCurrentItem(1);
			}else{
				mViewPager.setCurrentItem(0);
			}
			
		}	   
		
		return v;
	}
	

	
	
	@Override
	public void onResume(){
		super.onResume();	
		
		if(DadosUtil.alunoSelecionado == null){
			return;
		}
	
		MainActivity.instance.dbAlunoAdapter.clearAlunosQuantMensagens(DadosUtil.alunoSelecionado);
		
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager) getActivity().getSystemService(ns);
		
		//TODO TALVEZ UM DIA SEPARAR ESSES CANCELAMENTOS POR ABA SELECIONADA.. LEMBRETE E EVENTOS
		nMgr.cancel(Constantes.TIPO_MSG_EVENTO,DadosUtil.alunoSelecionado.getId().intValue());
		nMgr.cancel(Constantes.TIPO_MSG_EVENTO_CANC,DadosUtil.alunoSelecionado.getId().intValue());
		nMgr.cancel(Constantes.TIPO_MSG_LEMBRETE,DadosUtil.alunoSelecionado.getId().intValue());
		nMgr.cancel(Constantes.TIPO_MSG_LEMBRETE_CANC,DadosUtil.alunoSelecionado.getId().intValue());
		
		// TODO REVER SAPORRA	de notification helper		
		NotificationHelper.eventoNotifications.put(DadosUtil.alunoSelecionado.getId().intValue(), "");
		
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while( (eventoFragment == null || !eventoFragment.carregou)  && (lembreteFragment == null || !lembreteFragment.carregou) ){
					continue;
				}
							
				MainActivity.instance.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						buscarDadosBanco();						
					}
				});
			}
		}).start();

	}
	
	@Override
	public void onLoginProcessStart() {
				
	}

	@Override
	public void onLoginProcessSuccess(Usuario usuarioLogado) {
		
		MainActivity.instance.runOnUiThread(new Runnable() {
		       
		public void run() { 
			
			eventoFragment.stopProgress();
			lembreteFragment.stopProgress();			
			carregarAgenda();
		
		  }
	    });		
	}

	@Override
	public void onLoginProcessFail(String message) {
			
		MainActivity.instance.runOnUiThread(new Runnable() {
		       
			public void run() { 
				eventoFragment.layoutAgendaErro.setVisibility(View.VISIBLE);					
				eventoFragment.layoutAgendaErro.setOnClickListener(new View.OnClickListener(){
			    @Override
			    public void onClick(View v){
			    	eventoFragment.layoutAgendaErro.setVisibility(View.GONE);	
			    	lembreteFragment.layoutAgendaErro.setVisibility(View.GONE);
		    		eventoFragment.startProgress();
		    		lembreteFragment.startProgress();
		    		MainActivity.instance.automaticLogin();
			    }
				});
				
				lembreteFragment.layoutAgendaErro.setVisibility(View.VISIBLE);					
				lembreteFragment.layoutAgendaErro.setOnClickListener(new View.OnClickListener(){
			    @Override
			    public void onClick(View v){
			    	eventoFragment.layoutAgendaErro.setVisibility(View.GONE);	
			    	lembreteFragment.layoutAgendaErro.setVisibility(View.GONE);
		    		eventoFragment.startProgress();
		    		lembreteFragment.startProgress();
		    		MainActivity.instance.automaticLogin();
			    }
				});				
			
				eventoFragment.stopProgress();
				lembreteFragment.stopProgress();

		    }
	    });
				
	}
	
	public void buscarDadosBanco() {	
		
		new AsyncTask<Void, EventoExecutado ,Void>() {
			
			protected void onPreExecute() {
				
		    	if(cancelouExecucao){
		    		return;
		    	}
				
	    		eventoFragment.startProgress();
	    		lembreteFragment.startProgress();
				
				eventoFragment.adapter.clear();
				lembreteFragment.adapter.clear();
			
			};

			@Override
			protected Void doInBackground(Void... params) {
				
		    	if(cancelouExecucao){
		    		return null;
		    	}
		    	
//				List<EventoExecutado> listaLembrete = MainActivity.instance.dbEventoExeAdapter.returnLembretes(DadosUtil.alunoSelecionado.getId());
//				
//            	for(EventoExecutado evt:listaLembrete){	      	        	        	
//    	        	if(cancelouExecucao){
//    	        		break;
//    	        	}
//                	publishProgress(evt);	                		
//            	}  
		    	
				
				listaBanco = MainActivity.instance.dbEventoExeAdapter.returnByDataSelected(DadosUtil.alunoSelecionado.getId(),DadosUtil.dataSelecionada);
				
            	for(EventoExecutado evt:listaBanco){	      	        	        	
    	        	if(cancelouExecucao){
    	        		break;
    	        	}
                	publishProgress(evt);	                		
            	}          	
				
            	return null;
			}
			
			@Override
			protected void onProgressUpdate(EventoExecutado... values) {						
	        	
				if(cancelouExecucao){
	        		return;
	        	}	
				if(values[0].getTipo() == Constantes.TIPO_LEMBRETE || values[0].getTipo() == Constantes.TIPO_LEMBRETE_GEN){
		        	lembreteFragment.adapter.remove(values[0]);				
		        	lembreteFragment.adapter.insert(values[0],0);
		        	lembreteFragment.listAgenda.smoothScrollToPositionFromTop(0, 0, 50);  
				}else{
		        	eventoFragment.adapter.remove(values[0]);				
		        	eventoFragment.adapter.insert(values[0],0);	
		        	eventoFragment.listAgenda.smoothScrollToPositionFromTop(0, 0, 50);  
				}
													
			}
			
			@Override
			protected void onPostExecute(Void result) {
	        	if(cancelouExecucao){
	        		return;
	        	}						
				carregarAgenda();
			
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); 									
	}
	
	
	public void carregarAgenda(){
		
		if(cancelouExecucao){
			return;
		}
					
		eventoFragment.startProgress();
		lembreteFragment.startProgress();
		
		new AsyncTask<Void, EventoExecutado, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				
	        	
				try {
					
    	        	if(cancelouExecucao){
    	        		return null;
    	        	}
					
					String datasAtualizacaoStr = "";
					if(!listaBanco.isEmpty()){
						for(EventoExecutado ids:listaBanco){
							
		    	        	if(cancelouExecucao){
		    	        		return null;
		    	        	}
							
							datasAtualizacaoStr += ids.getDataAtualizacao()+",";
						}
					}

					
					while (DadosUtil.statusLogin == Constantes.STATUS_LOGIN_EM_PROCESSO) {
						continue;
					}
					
					if(DadosUtil.statusLogin  == Constantes.STATUS_LOGIN_NAO_LOGADO){
						return null;
					}
					
    	        	if(cancelouExecucao){
    	        		return null;
    	        	}
					
					byte[] encIdUsuario 		= Crypto.encrypt(DadosUtil.usuarioLogado.getId().toString().getBytes("UTF-8"));
					byte[] encIdAluno   		= Crypto.encrypt(DadosUtil.alunoSelecionado.getId().toString().getBytes("UTF-8"));
					byte[] datasAtualizacao 	= Crypto.encrypt(new String(datasAtualizacaoStr).getBytes("UTF-8"));
					byte[] keyhash				= Crypto.encrypt(DadosUtil.usuarioLogado.getSecurityHashKey().getBytes("UTF-8"));
					byte[] encDataAtualizacaoUsuario	= Crypto.encrypt(DadosUtil.usuarioLogado.getDataAtualizacao().toString().getBytes("UTF-8"));
					
					byte[] encDataPesquisa	= Crypto.encrypt(String.valueOf(DadosUtil.dataSelecionada.getTimeInMillis()).getBytes("UTF-8"));
					
					VectoreventoExeCrypt lista = IschoolWebServiceImplService.getInstance().listarEventosExecutados(new VectorByte(encIdUsuario),
					 new VectorByte(encIdAluno),
					 new VectorByte(datasAtualizacao),
					 new VectorByte(encDataAtualizacaoUsuario),
					 new VectorByte(encDataPesquisa),
					 new VectorByte(keyhash));
					
    	        	if(cancelouExecucao){
    	        		return null;
    	        	}
					
					listaNovosEventos = new ArrayList<EventoExecutado>();
				
					if(lista != null && !lista.isEmpty()){		
											
						for(EventoExeCrypt eventoExtCrypt:lista){
							
							EventoExecutado eventoExecutado = eventoExtCrypt.decriptarEventoExecutado();
							eventoExecutado.setClasse(eventoExtCrypt.classeField.decriptarClasse());
							eventoExecutado.setEvento(eventoExtCrypt.evento.decriptarEvento());
							eventoExecutado.setCliente(DadosUtil.usuarioLogado.getCliente());
							eventoExecutado.setUsuario(DadosUtil.usuarioLogado);
							eventoExecutado.setAluno(DadosUtil.alunoSelecionado);
							
							listaNovosEventos.add(eventoExecutado);														
						}
					}
					
    	        	if(cancelouExecucao){
    	        		return null;
    	        	}

					salvarDadosBanco();

				} catch (Exception e) {
    	        	if(cancelouExecucao){
    	        		return null;
    	        	}
					
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
						
						getActivity().runOnUiThread(new Runnable() {
		    	        public void run() {    
		    	                try{
		    	                	eventoFragment.layoutAgendaErro.setVisibility(View.VISIBLE);					
		    	                	eventoFragment.layoutAgendaErro.setOnClickListener(new View.OnClickListener(){
		    						    @Override
		    						    public void onClick(View v){
		    						    	eventoFragment.layoutAgendaErro.setVisibility(View.GONE);	
		    						    	lembreteFragment.layoutAgendaErro.setVisibility(View.GONE);	
		    						    	
		    						    	if(DadosUtil.statusLogin == Constantes.STATUS_LOGIN_NAO_LOGADO){
		    						    		eventoFragment.startProgress();
		    						    		lembreteFragment.startProgress();
		    						    		MainActivity.instance.automaticLogin();
		    						    	}else{
		    						    		carregarAgenda();
		    						    	}
		    						    }
		    						});
		    	                	
		    	                	lembreteFragment.layoutAgendaErro.setVisibility(View.VISIBLE);					
		    	                	lembreteFragment.layoutAgendaErro.setOnClickListener(new View.OnClickListener(){
		    						    @Override
		    						    public void onClick(View v){
		    						    	eventoFragment.layoutAgendaErro.setVisibility(View.GONE);	
		    						    	lembreteFragment.layoutAgendaErro.setVisibility(View.GONE);	
		    						    	
		    						    	if(DadosUtil.statusLogin == Constantes.STATUS_LOGIN_NAO_LOGADO){
		    						    		eventoFragment.startProgress();
		    						    		lembreteFragment.startProgress();
		    						    		MainActivity.instance.automaticLogin();
		    						    	}else{
		    						    		carregarAgenda();
		    						    	}
		    						    }
		    						});
		    	                	
		    	                	eventoFragment.stopProgress();
		    	                	lembreteFragment.stopProgress();
		    	                }
		    	                catch(Exception e){}
		    	        	}
						});
										
						e.printStackTrace();
						break;	
					}
					
					return null;
				}				
			
				if(listaNovosEventos != null && !listaNovosEventos.isEmpty()){
					for(EventoExecutado evt:listaNovosEventos){
        	        	if(cancelouExecucao){
        	        		break;
        	        	}
        				listaBanco.add(evt);
						publishProgress(evt);
					}
				}

				return null;
			}
		
			
			protected void onProgressUpdate(EventoExecutado... values) {
				
	        	if(cancelouExecucao){
	        		return;
	        	}
				
	        	if(values[0].getTipo() == Constantes.TIPO_LEMBRETE || values[0].getTipo() == Constantes.TIPO_LEMBRETE_GEN){
		        	lembreteFragment.adapter.remove(values[0]);				
		        	lembreteFragment.adapter.insert(values[0],0);
		        	lembreteFragment.listAgenda.smoothScrollToPositionFromTop(0, 0, 50); 
				}else{
		        	eventoFragment.adapter.remove(values[0]);				
		        	eventoFragment.adapter.insert(values[0],0);	
		        	eventoFragment.listAgenda.smoothScrollToPositionFromTop(0, 0, 50); 
				}
				
			}
			
			protected void onPostExecute(Void result) {
	        	if(cancelouExecucao){
	        		return;
	        	}
	        	
	        	
	        	if(eventoFragment.adapter == null || eventoFragment.adapter.isEmpty()){								
					
	        		if(eventoFragment.listAgenda.getEmptyView() == null){
	        			
		        		View convertView = getLayoutInflater(null).inflate(R.layout.evento_item, fl ,false);
		        		
		        		TextView tv_titulo = (TextView) convertView.findViewById(R.id.tv_linha1);
		        		TextView tv_mensagem = (TextView) convertView.findViewById(R.id.tv_linha2);
		        		ImageView icon2 = (ImageView)convertView.findViewById(R.id.iv_agenda_icon);
		        		
		        	
		        		tv_titulo.setText("Não há Eventos"); //TODO colocar no bundle	
		        		tv_mensagem.setText("Aguarde que novos eventos virão");
		        		icon2.setImageResource(R.drawable.icon_alert);		
		        		
		        		((ViewGroup)eventoFragment.listAgenda.getParent()).addView(convertView);        		
		        		eventoFragment.listAgenda.setEmptyView(convertView);
	        		}
	        		
				}	        	
   	        	
	        	if(lembreteFragment.adapter == null || lembreteFragment.adapter.isEmpty()){								
						        		
	        		if(lembreteFragment.listAgenda.getEmptyView() == null){
	        			
		        		View convertView = getLayoutInflater(null).inflate(R.layout.evento_item, fl ,false);
		        		
		        		TextView tv_titulo = (TextView) convertView.findViewById(R.id.tv_linha1);
		        		TextView tv_mensagem = (TextView) convertView.findViewById(R.id.tv_linha2);
		        		ImageView icon2 = (ImageView)convertView.findViewById(R.id.iv_agenda_icon);
		        		
		        	
		        		tv_titulo.setText("Não há Lembretes"); //TODO colocar no bundle	
		        		tv_mensagem.setText("Aguarde novos lembretes");
		        		icon2.setImageResource(R.drawable.icon_alert);		
		        		
		        		((ViewGroup)lembreteFragment.listAgenda.getParent()).addView(convertView);        		
		        		lembreteFragment.listAgenda.setEmptyView(convertView);
	        		}
       		
				}	
     	        	
	        	if(listaNovosEventos != null && !listaNovosEventos.isEmpty()){
	        		listaNovosEventos.clear();
	        	}
	        	lembreteFragment.stopProgress();
	        	eventoFragment.stopProgress();
			};
			 
		 }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); 


	}
	
	private void salvarDadosBanco(){
		if(listaNovosEventos == null || listaNovosEventos.isEmpty()){
			return;
		}
		MainActivity.instance.dbEventoExeAdapter.storeEventosExecutadosInDataBase(listaNovosEventos);		
		
	}
	
	
	public void onDestroyView() {

		cancelouExecucao = true;
		
		super.onDestroyView();
	}
	

	

	public class SectionsPagerAdapter extends FragmentPagerAdapter implements IconTabProvider{

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			
			switch (position) {
			case 0:
				eventoFragment = new EventoFragment();			
				return eventoFragment;
				
			case 1:
				lembreteFragment = new LembreteFragment();				
				return lembreteFragment;		
			}

			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Eventos";//getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return "Lembretes";// getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}

		private int tabIcons[] = {R.drawable.icon_agenda_tab, R.drawable.icon_lembrete_tab};
		
		@Override
		public int getPageIconResId(int position) {
			return tabIcons[position];
		}
		
				
	}


	
}
