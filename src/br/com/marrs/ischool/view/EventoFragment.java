package br.com.marrs.ischool.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.infteh.calendarview.CalendarDatePickerDialog;
import com.infteh.calendarview.CalendarView;
import com.infteh.calendarview.CalendarDatePickerDialog.OnDateSetListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;
import br.com.marrs.ischool.MainActivity;
import br.com.marrs.ischool.R;
import br.com.marrs.ischool.entidades.EventoExecutado;
import br.com.marrs.ischool.util.Constantes;
import br.com.marrs.ischool.util.DadosUtil;

public class EventoFragment extends Fragment{
	
	public ProgressBar progress;
	public ListView listAgenda;
	public LinearLayout fl;
	public  ArrayAdapter<EventoExecutado> adapter;
	public AlertDialog alertDialogInfo;
	public LinearLayout layoutAgendaErro;	
	public List<EventoExecutado> listaNovosEventos;
	public List<EventoExecutado> listaBanco;
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.US);	
	boolean carregou = false;
	public CalendarDatePickerDialog calendarDiaLog;
		
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
 		if(fl == null){
			fl = (LinearLayout)inflater.inflate(R.layout.fragment_evento, container, false);
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
		
		Button botao = (Button) fl.findViewById(R.id.botao_data_evento);
		
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
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.evento_item, parent,false);
			}
			
			TextView tv_titulo = (TextView) convertView.findViewById(R.id.tv_linha1);
			TextView tv_mensagem = (TextView) convertView.findViewById(R.id.tv_linha2);
			ImageView icon = (ImageView)convertView.findViewById(R.id.iv_agenda_icon);
			
			
			final EventoExecutado eventoExecutado = adapter.getItem(position);		
			String codigoEvento = eventoExecutado.getEvento().getCodigoEvento();
			LinearLayout itemLayout = (LinearLayout) convertView.findViewById(R.id.ll_item_agenda);	
			
			
			switch (codigoEvento) {
			case "#Entrada":

				tv_titulo.setText("Entrada"); //TODO colocar no bundle	
 				final String textoEntrada ="Entrou às "+sdf.format(eventoExecutado.getDataInicio());			
				tv_mensagem.setText(textoEntrada);
				icon.setImageResource(R.drawable.icon_entrada);
									
				if(eventoExecutado.getStatusEventoExecutado() == Constantes.STATUS_CANCELADO){				
					itemLayout.setBackgroundColor(getResources().getColor(R.color.evento_canc_color));
					tv_titulo.setTextColor(Color.WHITE);
					tv_mensagem.setTextColor(Color.WHITE);
					tv_titulo.setText("Entrada(cancelado)");
				}else{
					tv_titulo.setTextColor(Color.DKGRAY);
					tv_mensagem.setTextColor(Color.DKGRAY);
					itemLayout.setBackgroundColor(Color.WHITE);
				}
				
				convertView.setOnClickListener(new View.OnClickListener(){
				    @Override
				    public void onClick(View v){
				    	
				    	String msg = textoEntrada+"\n\nObservações:\n"+eventoExecutado.getObservacoes();
				    	
				    	showEventoDetails("Detalhes Entrada", msg, R.drawable.icon_entrada);
				    }
				});
				
				break;
				
			case "#Soninho":
				
				tv_titulo.setText("Soninho");//TODO colocar no bundle	
				String textoSoninho ="Iniciou às "+sdf.format(eventoExecutado.getDataInicio());
				if(eventoExecutado.getDataFim() != null){
					textoSoninho += "\nTerminou às "+sdf.format(eventoExecutado.getDataFim());	
				}
				final String textoSoninhoFinal = textoSoninho;
				tv_mensagem.setText(textoSoninho);
				icon.setImageResource(R.drawable.icon_soninho);

				if(eventoExecutado.getStatusEventoExecutado() == Constantes.STATUS_CANCELADO){
					itemLayout.setBackgroundColor(getResources().getColor(R.color.evento_canc_color));
					tv_titulo.setTextColor(Color.WHITE);
					tv_mensagem.setTextColor(Color.WHITE);
					tv_titulo.setText("Soninho(cancelado)");
				}else{
					tv_titulo.setTextColor(Color.DKGRAY);
					tv_mensagem.setTextColor(Color.DKGRAY);
					itemLayout.setBackgroundColor(Color.WHITE);
				}				
				
				convertView.setOnClickListener(new View.OnClickListener(){
				    @Override
				    public void onClick(View v){
				    	
				    	String msg = textoSoninhoFinal+"\n\nObservações:\n"+eventoExecutado.getObservacoes();
				    	
				    	showEventoDetails("Detalhes Soninho", msg, R.drawable.icon_soninho);

				    }
				});
				
				break;
				
			case "#Almoco":
				
				tv_titulo.setText("Almoço");//TODO colocar no bundle
				final String textoALmoco ="Teve um almoço às " +sdf.format(eventoExecutado.getDataInicio());			
				tv_mensagem.setText(textoALmoco);				
				icon.setImageResource(R.drawable.icon_almoco);
				

				if(eventoExecutado.getStatusEventoExecutado() == Constantes.STATUS_CANCELADO){
					itemLayout.setBackgroundColor(getResources().getColor(R.color.evento_canc_color));
					tv_titulo.setTextColor(Color.WHITE);
					tv_mensagem.setTextColor(Color.WHITE);
					tv_titulo.setText("Almoço(cancelado)");
				}else{
					tv_titulo.setTextColor(Color.DKGRAY);
					tv_mensagem.setTextColor(Color.DKGRAY);
					itemLayout.setBackgroundColor(Color.WHITE);
				}					
				
				convertView.setOnClickListener(new View.OnClickListener(){
				    @Override
				    public void onClick(View v){
				    	
				    	
				    	String msg = textoALmoco;
				    	msg += "\nO almoço foi: ";
				    	
				    	switch (eventoExecutado.getAvaliacaoEvento()){
						case 1:
							msg += "Otimo.";
							break;
							
						case 2:
							msg += "Bom.";
							break;
							
						case 3:
							msg += "Ruim.";
							break;
							
						case 4:
							msg += "Não Aceitou.";
							break;							

						default:
							msg += "Não sabe.";
							break;
						}
				    	
				    	msg += "\n\nObservações:\n"+eventoExecutado.getObservacoes();
				    					    	
				    	showEventoDetails("Detalhes Almoço", msg, R.drawable.icon_almoco);

				    }
				});
				
				break;	
				
			case "#Evacuacao":
				
				tv_titulo.setText("Evacuação");//TODO colocar no bundle	
				icon.setImageResource(R.drawable.icon_evacuacao);
				final String textoEvacuacao ="Evacuou às " +sdf.format(eventoExecutado.getDataInicio());			
				tv_mensagem.setText(textoEvacuacao);	
				
				if(eventoExecutado.getStatusEventoExecutado() == Constantes.STATUS_CANCELADO){
					itemLayout.setBackgroundColor(getResources().getColor(R.color.evento_canc_color));
					tv_titulo.setTextColor(Color.WHITE);
					tv_mensagem.setTextColor(Color.WHITE);
					tv_titulo.setText("Evacuação(cancelado)");
				}else{
					tv_titulo.setTextColor(Color.DKGRAY);
					tv_mensagem.setTextColor(Color.DKGRAY);
					itemLayout.setBackgroundColor(Color.WHITE);
				}					
				
				convertView.setOnClickListener(new View.OnClickListener(){
				    @Override
				    public void onClick(View v){
				    	
				    	String msg = textoEvacuacao;
				    	msg += "\nA evacuação foi: "+ (eventoExecutado.getAvaliacaoEvento() == 1 ? "Normal" : "Molinho");
				    	msg += "\n" + (eventoExecutado.getTomouBanho()  ? "Tomou um banho." : "Não tomou banho.");
				    	
				    	msg += "\n\nObservações:\n"+eventoExecutado.getObservacoes();
				    	
				    	showEventoDetails("Detalhes Evacuação", msg, R.drawable.icon_evacuacao);

				    }
				});
				
				break;	
				
			case "#Medicamento":
				
				tv_titulo.setText("Medicamento");//TODO colocar no bundle	
				icon.setImageResource(R.drawable.icon_medicamento);
				
				final String textoMedicamento ="Tomou medicamento às " +sdf.format(eventoExecutado.getDataInicio());			
				tv_mensagem.setText(textoMedicamento);	
				
				if(eventoExecutado.getStatusEventoExecutado() == Constantes.STATUS_CANCELADO){
					itemLayout.setBackgroundColor(getResources().getColor(R.color.evento_canc_color));
					tv_titulo.setTextColor(Color.WHITE);
					tv_mensagem.setTextColor(Color.WHITE);
					tv_titulo.setText("Medicamento(cancelado)");
				}else{
					tv_titulo.setTextColor(Color.DKGRAY);
					tv_mensagem.setTextColor(Color.DKGRAY);
					itemLayout.setBackgroundColor(Color.WHITE);
				}
				
				convertView.setOnClickListener(new View.OnClickListener(){
				    @Override
				    public void onClick(View v){
				    	
				    	String msg = textoMedicamento;
				    	msg += "\nTomou os medicamentos:\n"+ eventoExecutado.getMedicamentos();
				    	msg += "\n\nObservações:\n"+eventoExecutado.getObservacoes();
				    	
				    	showEventoDetails("Detalhes Medicamento", msg, R.drawable.icon_medicamento);

				    }
				});
				
				break;	
				
			case "#Lanche":
				
				tv_titulo.setText("Lanche");//TODO colocar no bundle	
				icon.setImageResource(R.drawable.icon_lanche);
				final String textoLanche ="Teve um lanche às " +sdf.format(eventoExecutado.getDataInicio());			
				tv_mensagem.setText(textoLanche);	
				
				if(eventoExecutado.getStatusEventoExecutado() == Constantes.STATUS_CANCELADO){
					itemLayout.setBackgroundColor(getResources().getColor(R.color.evento_canc_color));
					tv_titulo.setTextColor(Color.WHITE);
					tv_mensagem.setTextColor(Color.WHITE);
					tv_titulo.setText("Lanche(cancelado)");
				}else{
					tv_titulo.setTextColor(Color.DKGRAY);
					tv_mensagem.setTextColor(Color.DKGRAY);
					itemLayout.setBackgroundColor(Color.WHITE);
				}				
				
				convertView.setOnClickListener(new View.OnClickListener(){
				    @Override
				    public void onClick(View v){
				    	
				    	String msg = textoLanche;
				    	msg += "\nO lanche foi: ";
				    	
				    	switch (eventoExecutado.getAvaliacaoEvento()){
						case 1:
							msg += "Otimo.";
							break;
							
						case 2:
							msg += "Bom.";
							break;
							
						case 3:
							msg += "Ruim.";
							break;
							
						case 4:
							msg += "Não Aceitou.";
							break;							

						default:
							msg += "Não sabe.";
							break;
						}
				    	
				    	msg += "\n\nObservações:\n"+eventoExecutado.getObservacoes();
				    					    	
				    	showEventoDetails("Detalhes Lanche", msg,  R.drawable.icon_lanche);


				    }
				});
				
				break;	
				
			case "#Mamadeira":
				
				tv_titulo.setText("Mamadeira");//TODO colocar no bundle
				final String textoMamadeira ="Tomou "+eventoExecutado.getQuantidade()+eventoExecutado.getEvento().getUnidadeMedida()+" de leite às "+sdf.format(eventoExecutado.getDataInicio());			
				tv_mensagem.setText(textoMamadeira);
				icon.setImageResource(R.drawable.icon_mamadeira);

				if(eventoExecutado.getStatusEventoExecutado() == Constantes.STATUS_CANCELADO){
					itemLayout.setBackgroundColor(getResources().getColor(R.color.evento_canc_color));
					tv_titulo.setTextColor(Color.WHITE);
					tv_mensagem.setTextColor(Color.WHITE);
					tv_titulo.setText("Mamadeira(cancelado)");
				}else{
					tv_titulo.setTextColor(Color.DKGRAY);
					tv_mensagem.setTextColor(Color.DKGRAY);
					itemLayout.setBackgroundColor(Color.WHITE);
				}
				
				convertView.setOnClickListener(new View.OnClickListener(){
				    @Override
				    public void onClick(View v){
				    	showEventoDetails("Detalhes Mamadeira", textoMamadeira+"\n\nObservações:\n"+eventoExecutado.getObservacoes(), R.drawable.icon_mamadeira);

				    }
				});
				
				break;
				
			case "#Saida":
					
				tv_titulo.setText("Saída"); //TODO colocar no bundle	
				final String textoSaida ="Saiu às "+sdf.format(eventoExecutado.getDataInicio());			
				tv_mensagem.setText(textoSaida);
				icon.setImageResource(R.drawable.icon_saida);
				
				if(eventoExecutado.getStatusEventoExecutado() == Constantes.STATUS_CANCELADO){
					itemLayout.setBackgroundColor(getResources().getColor(R.color.evento_canc_color));
					tv_titulo.setTextColor(Color.WHITE);
					tv_mensagem.setTextColor(Color.WHITE);
					tv_titulo.setText("Saída(cancelado)");
				}else{
					tv_titulo.setTextColor(Color.DKGRAY);
					tv_mensagem.setTextColor(Color.DKGRAY);
					itemLayout.setBackgroundColor(Color.WHITE);
				}
				
				convertView.setOnClickListener(new View.OnClickListener(){
				    @Override
				    public void onClick(View v){
				    	
				    	String msg = textoSaida+"\n\nObservações:\n"+eventoExecutado.getObservacoes();
				    	
				    	showEventoDetails("Detalhes Saída", msg, R.drawable.icon_saida);
				    }
				});
				
				break;						

			default:
				
				tv_titulo.setText(eventoExecutado.getEvento().getNome());	
				tv_mensagem.setText(eventoExecutado.getObservacoes());
				icon.setImageResource(R.drawable.icon_generico);
				
				if(eventoExecutado.getStatusEventoExecutado() == Constantes.STATUS_CANCELADO){
					itemLayout.setBackgroundColor(getResources().getColor(R.color.evento_canc_color));
					tv_titulo.setTextColor(Color.WHITE);
					tv_mensagem.setTextColor(Color.WHITE);
					tv_titulo.setText(eventoExecutado.getEvento().getNome()+"(cancelado)");
				}else{
					tv_titulo.setTextColor(Color.DKGRAY);
					tv_mensagem.setTextColor(Color.DKGRAY);
					itemLayout.setBackgroundColor(Color.WHITE);
				}				
				
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
