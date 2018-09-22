package br.com.marrs.ischool.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.marrs.ischool.MainActivity;
import br.com.marrs.ischool.R;
import br.com.marrs.ischool.entidades.EventoExecutado;
import br.com.marrs.ischool.util.Constantes;
import br.com.marrs.ischool.util.DadosUtil;

import com.infteh.calendarview.CalendarDatePickerDialog;
import com.infteh.calendarview.CalendarDatePickerDialog.OnDateSetListener;
import com.infteh.calendarview.CalendarView;

public class LembreteFragment extends Fragment{
	
	public ProgressBar progress;
	public ListView listAgenda;
	public LinearLayout fl;
	public ArrayAdapter<EventoExecutado> adapter;
	public AlertDialog alertDialogInfo;
	public LinearLayout layoutAgendaErro;	
	public List<EventoExecutado> listaNovosEventos;
	public List<EventoExecutado> listaBanco;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm",Locale.US);
	boolean carregou = false;
	public CalendarDatePickerDialog calendarDiaLog;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
 		if(fl == null){
			fl = (LinearLayout)inflater.inflate(R.layout.fragment_lembrete, container, false);
		}

		progress = (ProgressBar) fl.findViewById(R.id.progress_agenda);
		layoutAgendaErro = (LinearLayout) fl.findViewById(R.id.layout_agenda_erro);	
		listAgenda = (ListView)fl.findViewById(R.id.lv_agenda);
						
		adapter = new EventoExecutadoAdapter(getActivity());			
		
		listAgenda.setAdapter(adapter);
			
		alertDialogInfo = new AlertDialog.Builder(getActivity()).create();		
		alertDialogInfo.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
			    new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			            dialog.dismiss();
			        }
		});
		
		
		Button botao = (Button) fl.findViewById(R.id.botao_data_lembrete);
		
		OnDateSetListener ondateSet = new CalendarDatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(CalendarView view, int year, int monthOfYear,	int dayOfMonth) {
				Toast.makeText(MainActivity.instance,"Selecionado "+ String.format("%02d", (dayOfMonth)) + "/" + String.format("%02d", (monthOfYear+1))+ "/" + year, Toast.LENGTH_LONG).show();						
				DadosUtil.dataSelecionada.set(Calendar.YEAR, year);
				DadosUtil.dataSelecionada.set(Calendar.MONTH, monthOfYear);
				DadosUtil.dataSelecionada.set(Calendar.DAY_OF_MONTH, dayOfMonth);	
				MainActivity.instance.onResume();
			}
		};
		
		calendarDiaLog = new CalendarDatePickerDialog(getActivity(), ondateSet, DadosUtil.dataSelecionada);
		
		botao.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				calendarDiaLog.getCalendarView().setCurrentDay(DadosUtil.dataSelecionada);
				calendarDiaLog.show();		
				MainActivity.instance.onPause();
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
		

		carregou = true;
		
	}
	
	
	
	private class EventoExecutadoAdapter extends ArrayAdapter<EventoExecutado>{
		public EventoExecutadoAdapter(Context context) {
			super(context, 0);
		}

		// TODO UM DIA REFATORAR SAPORRA POR QUE TA UMA COISA HORRIVEL!!
		// TODO DICA.. OLHAR COMO FOI FEITO O MENSAGENSFRAGMENT (metodo buildMensagen) PARA SETAR ESSAS REGRAS 
		public View getView(int position, View convertView, ViewGroup parent){
			if (convertView == null){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.lembrete_item, parent,false);
			}
			
			TextView tv_titulo = (TextView) convertView.findViewById(R.id.tv_titulo);
			TextView tv_mensagem = (TextView) convertView.findViewById(R.id.tv_linha2);
			
			
			final EventoExecutado eventoExecutado = adapter.getItem(position);		
			String codigoEvento = eventoExecutado.getEvento().getCodigoEvento();
			RelativeLayout itemCabecalho = (RelativeLayout) convertView.findViewById(R.id.item_card_cabecalho);	
			Button botaoDetalhes = (Button) convertView.findViewById(R.id.botao_detalhes);	
			//Button botaoFechar = (Button) convertView.findViewById(R.id.botao_fechar);	
			
			switch (codigoEvento) {

				
			case "#FavorEnviar":

				tv_titulo.setText(sdf.format(eventoExecutado.getDataCadastro()));
			
 				final String textoEntrada = "Favor enviar " + (eventoExecutado.getEnviarFralda() ? "fraldas, " : " ")
 											  + (eventoExecutado.getEnviarLeite() ? "leite, " : " ")
 											  + (eventoExecutado.getEnviarLencos() ? "lencos, " : " ")
 											  + (eventoExecutado.getEnviarPomada() ? "pomada, " : " ")
 											  + (!eventoExecutado.getEnviarOutros().trim().isEmpty() ? " "+eventoExecutado.getEnviarOutros() : "");
 				
 				if(textoEntrada.trim().endsWith(",")){
 					tv_mensagem.setText(textoEntrada.trim().substring(0,textoEntrada.trim().length()-1));
 				}else{
 					tv_mensagem.setText(textoEntrada);
 				}
 				
 				
									
				if(eventoExecutado.getStatusEventoExecutado() == Constantes.STATUS_CANCELADO){				
					itemCabecalho.setBackgroundResource(R.drawable.cantos_arred_cinza_forma);
					tv_titulo.setText(sdf.format(eventoExecutado.getDataCadastro())+"(cancelado)");
				}else{
					itemCabecalho.setBackgroundResource(R.drawable.cantos_arred_verde_forma);
				}
				
				botaoDetalhes.setOnClickListener(new View.OnClickListener(){
				    @Override
				    public void onClick(View v){
				    	
				    	String msg = textoEntrada+"\n\nObservações:\n"+eventoExecutado.getObservacoes();
				    	
				    	showEventoDetails("Detalhes do Lembrete", msg, R.drawable.icon_calendario);
				    }
				});
				
//				android.support.v4.view.ViewCompat.setHasTransientState(convertView, true);
//			    ValueAnimator fader = ObjectAnimator.ofFloat(convertView, "alpha", 1, 0);
//			    final AnimatorSet animation = new AnimatorSet();
//			    animation.addListener(new AnimatorListener() {
//			        @Override
//			        public void onAnimationStart(Animator animation) {
//			        	
//			        }
//			        @Override
//			        public void onAnimationRepeat(Animator animation) {
//			        }
//			        @Override
//			        public void onAnimationEnd(Animator animation) {
//			        	adapter.remove(eventoExecutado);
//			        }
//			        @Override
//			        public void onAnimationCancel(Animator animation) {
//			        }
//			    });
//			    ((AnimatorSet) animation).play(fader);
//			    animation.setDuration(500);
//				
//				botaoFechar.setOnClickListener(new View.OnClickListener(){
//				    @Override
//				    public void onClick(View v){
//
//					    animation.start();
//				    }
//				});				
				
				break;
							

			default:
				
				tv_titulo.setText(sdf.format(eventoExecutado.getDataCadastro()));
				tv_mensagem.setText(eventoExecutado.getObservacoes());
				
				if(eventoExecutado.getStatusEventoExecutado() == Constantes.STATUS_CANCELADO){				
					itemCabecalho.setBackgroundResource(R.drawable.cantos_arred_cinza_forma);
					tv_titulo.setText(sdf.format(eventoExecutado.getDataCadastro())+"(cancelado)");
				}else{
					itemCabecalho.setBackgroundResource(R.drawable.cantos_arred_verde_forma);
				}
				
				botaoDetalhes.setOnClickListener(new View.OnClickListener(){
				    @Override
				    public void onClick(View v){
				    	
				    	String msg = eventoExecutado.getObservacoes();
				    	
				    	showEventoDetails("Detalhes do Lembrete", msg, R.drawable.icon_calendario);
				    }
				});
				
				break;
			}
						
			return convertView;
		}

	}
	


	private void showEventoDetails(String titulo,String mensagem, int r_icon){
		
		alertDialogInfo.setTitle(titulo);
		alertDialogInfo.setIcon(r_icon);
		alertDialogInfo.setMessage(mensagem);
		alertDialogInfo.show();
		
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

}
