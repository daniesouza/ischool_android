package br.com.marrs.ischool.dao;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import br.com.marrs.ischool.entidades.Aluno;
import br.com.marrs.ischool.entidades.Classe;
import br.com.marrs.ischool.entidades.Cliente;
import br.com.marrs.ischool.entidades.Evento;
import br.com.marrs.ischool.entidades.EventoExecutado;
import br.com.marrs.ischool.entidades.Usuario;
import br.com.marrs.ischool.util.Constantes;

public class DbEventoExecutadoAdapter {

	public static final String KEY_ID		 	  	= "id_evento_executado";
	public static final String KEY_CODIGO_EVENTO  	= "codigo_evento";
	public static final String KEY_UNID_MED		  	= "unidade_medida_evento";
	public static final String KEY_ID_CLIENTE	  	= "id_cliente";	
	public static final String KEY_NOME_CLIENTE   	= "nome_cliente";
	public static final String KEY_ID_USUARIO	  	= "id_usuario";	
	public static final String KEY_NOME_USUARIO   	= "nome_usuario";
	public static final String KEY_ID_CLASSE  	  	= "id_classe";
	public static final String KEY_NOME_CLASSE    	= "nome_classe";
	public static final String KEY_CODIGO_CLASSE  	= "codigo_classe";	
	public static final String KEY_TURMA_CLASSE  	= "turma_classe";
	public static final String KEY_ID_ALUNO  	  	= "id_aluno";
	public static final String KEY_NOME_ALUNO    	= "nome_aluno";
	public static final String KEY_CODIGO_ALUNO	  	= "codigo_aluno";
	public static final String KEY_OBSERVACOES    	= "observacoes";
	public static final String KEY_QUANTIDADE  		= "quantidade";
	public static final String KEY_DATA_CADASTRO  	= "data_cadastro";
	public static final String KEY_DATA_INICIO  	= "data_inicio";
	public static final String KEY_DATA_FIM  		= "data_fim";
	public static final String KEY_AVALIACAO  		= "avaliacaoEvento";
	public static final String KEY_TOMOU_BANHO  	= "tomou_banho";
	public static final String KEY_MEDICAMENTOS  	= "medicamentos";	
	public static final String KEY_ENVIAR_FRALDA  	= "enviar_fralda";
	public static final String KEY_ENVIAR_LENCOS  	= "enviar_lencos";
	public static final String KEY_ENVIAR_POMADA  	= "enviar_pomada";
	public static final String KEY_ENVIAR_LEITE  	= "enviar_leite";
	public static final String KEY_ENVIAR_OUTROS  	= "enviar_outros";
	public static final String KEY_STATS_EV_EXECUTADO  = "status_evento_executado";
	public static final String KEY_DATA_ATUALIZACAO    = "data_atualizacao";
	public static final String KEY_NOME_EVENTO  	   = "nome_evento";
	public static final String KEY_ID_EVENTO		   = "id_evento";
	public static final String KEY_TIPO		   		   = "tipo";
	
	public static final String DB_NAME  	 = "ischool_userData";
	public static final String DB_TABLE 	 = "EVENTO_EXECUTADO";
	//private static final int    DB_VERSION = 2;
	
	private static final String DB_CREATE ="CREATE TABLE IF NOT EXISTS EVENTO_EXECUTADO(" +
											"id_evento_executado NUMERIC NOT NULL primary key, " +
											"codigo_evento text(30)," +
											"unidade_medida_evento text(30)," +
											"id_cliente 	 NUMERIC NOT NULL," +
											"nome_cliente 	 text(255)," +
											"id_usuario 	 NUMERIC NOT NULL," +
											"nome_usuario 	 text(255)," +
											"id_classe 	 	 NUMERIC NOT NULL," +
											"nome_classe 	 text(255)," +
											"codigo_classe 	 text(30)," +			
											"turma_classe 	 text(30)," +
											"id_aluno 	 	 NUMERIC NOT NULL," +
											"nome_aluno 	 text(255)," +
											"codigo_aluno 	 text(30)," +												
											"observacoes 	 text(255)," +
											"quantidade 	 NUMERIC," +
											"data_cadastro 	 text(20)," +
											"data_inicio 	 text(20)," +
											"data_fim 		 text(20)," +		
											"avaliacaoEvento tinyInt(1), " +
											"tomou_banho 	 tinyInt(1), " +			
											"medicamentos 	 text(255), " +
											"enviar_fralda 	 tinyInt(1)," +			
											"enviar_lencos 	 tinyInt(1)," +		
											"enviar_pomada 	 tinyInt(1)," +
											"enviar_leite 	 tinyInt(1)," +
											"enviar_outros 	 text(255)," +
											"status_evento_executado tinyInt(1)," +
											"data_atualizacao NUMERIC," +
											"id_evento 	 	  NUMERIC NOT NULL," +
											"nome_evento  	  text(255)," +
											"tipo 			  tinyInt(1));";
	
	private SQLiteDatabase mDb;
	private final Context  mCtx;
    private SimpleDateFormat dateFormat;

	
	public DbEventoExecutadoAdapter(Context ctx) {
		mCtx = ctx;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.US);
	}
	
	private DbEventoExecutadoAdapter open(){
		try{
			mDb = mCtx.openOrCreateDatabase(DB_NAME, 0, null);
	//		mDb.execSQL("DROP TABLE EVENTO_EXECUTADO");
			mDb.execSQL(DB_CREATE);				
		} catch(SQLException e){
			e.printStackTrace();
			throw new SQLException("Nao Conseguiu Criar o DB");
		}
		
		return this;
	}
	
	private void close(){
		mDb.close();
	}
	
	private void merge(EventoExecutado eventoExecutado){
		
		int rows = returnCountById(eventoExecutado.getId());
		
		if(rows>0){
			update(eventoExecutado);
		}else{
			insert(eventoExecutado);
		}
	}
	
	private int returnCountById(long id){
		
		Cursor c = mDb.query(true, DB_TABLE,
					new String[]{KEY_ID}, KEY_ID + "=" +id,
			null, null,null,null, null);
			
			c.moveToFirst();
			
			int result = c.getCount();
			c.close();
			
			return result;	
	}
	
	
	private long insert(EventoExecutado eventoExecutado){
			ContentValues contentValues = new ContentValues();
			
			contentValues.put(KEY_ID  			,  eventoExecutado.getId());	
			contentValues.put(KEY_CODIGO_EVENTO  ,  eventoExecutado.getEvento().getCodigoEvento());	
			contentValues.put(KEY_UNID_MED		  ,  eventoExecutado.getEvento().getUnidadeMedida());		
			contentValues.put(KEY_ID_CLIENTE	  ,  eventoExecutado.getCliente().getId());		
			contentValues.put(KEY_NOME_CLIENTE   ,  eventoExecutado.getCliente().getNome());		
			contentValues.put(KEY_ID_USUARIO	  ,  eventoExecutado.getUsuario().getId());		
			contentValues.put(KEY_NOME_USUARIO   ,  eventoExecutado.getUsuario().getNome());		
			contentValues.put(KEY_ID_CLASSE  	  ,  eventoExecutado.getClasse().getId());		
			contentValues.put(KEY_NOME_CLASSE    ,  eventoExecutado.getClasse().getNome());		
			contentValues.put(KEY_CODIGO_CLASSE  ,  eventoExecutado.getClasse().getCodigoClasse());		
			contentValues.put(KEY_TURMA_CLASSE  ,  eventoExecutado.getClasse().getTurma());	
			contentValues.put(KEY_ID_ALUNO  	,  eventoExecutado.getAluno().getId());		
			contentValues.put(KEY_NOME_ALUNO	,  eventoExecutado.getAluno().getNome());		
			contentValues.put(KEY_CODIGO_ALUNO  ,  eventoExecutado.getAluno().getCodigoAluno());		
			contentValues.put(KEY_OBSERVACOES    ,  eventoExecutado.getObservacoes());		
			contentValues.put(KEY_QUANTIDADE  	,  eventoExecutado.getQuantidade());		
			contentValues.put(KEY_DATA_CADASTRO  ,  eventoExecutado.getDataCadastro() == null ? null : dateFormat.format(eventoExecutado.getDataCadastro()));		
			contentValues.put(KEY_DATA_INICIO  	,   eventoExecutado.getDataInicio() == null ? null :  dateFormat.format(eventoExecutado.getDataInicio()));	 
			contentValues.put(KEY_DATA_FIM  		,  eventoExecutado.getDataFim() == null ? null : dateFormat.format(eventoExecutado.getDataFim()));	 
			contentValues.put(KEY_AVALIACAO  		,  eventoExecutado.getAvaliacaoEvento());	 
			contentValues.put(KEY_TOMOU_BANHO  	,  eventoExecutado.getTomouBanho());	 
			contentValues.put(KEY_MEDICAMENTOS  	,  eventoExecutado.getMedicamentos());	
			contentValues.put(KEY_ENVIAR_FRALDA  ,  eventoExecutado.getEnviarFralda());		
			contentValues.put(KEY_ENVIAR_LENCOS  ,  eventoExecutado.getEnviarLencos());		
			contentValues.put(KEY_ENVIAR_POMADA  ,  eventoExecutado.getEnviarPomada());		
			contentValues.put(KEY_ENVIAR_LEITE  ,  eventoExecutado.getEnviarLeite());		
			contentValues.put(KEY_ENVIAR_OUTROS  ,  eventoExecutado.getEnviarOutros());		
			contentValues.put(KEY_STATS_EV_EXECUTADO,  eventoExecutado.getStatusEventoExecutado());	
			contentValues.put(KEY_DATA_ATUALIZACAO,  eventoExecutado.getDataAtualizacao());	
			contentValues.put(KEY_ID_EVENTO,  		eventoExecutado.getEvento().getId());	
			contentValues.put(KEY_NOME_EVENTO,  	eventoExecutado.getEvento().getNome());	
			contentValues.put(KEY_TIPO,  			eventoExecutado.getTipo());	
			

			
			return mDb.insert(DB_TABLE, null, contentValues);
	}
	
	public boolean delete(EventoExecutado eventoExecutado){
		return mDb.delete(DB_TABLE, KEY_ID+"="+eventoExecutado.getId(), null)>0;
	}
	
	public List<EventoExecutado> returnAllRows(){
		List<EventoExecutado> lista = new ArrayList<EventoExecutado>(); 
		Cursor c = null;
		try{
		open();

		c = mDb.query(DB_TABLE, 
				new String[]{
				KEY_ID,
				KEY_CODIGO_EVENTO,  
				KEY_UNID_MED,  
				KEY_ID_CLIENTE,  
				KEY_NOME_CLIENTE,  
				KEY_ID_USUARIO,  
				KEY_NOME_USUARIO,  
				KEY_ID_CLASSE,  
				KEY_NOME_CLASSE,  
				KEY_CODIGO_CLASSE,  
				KEY_TURMA_CLASSE,
				KEY_ID_ALUNO,
				KEY_NOME_ALUNO,
				KEY_CODIGO_ALUNO,
				KEY_OBSERVACOES,  
				KEY_QUANTIDADE,  
				KEY_DATA_CADASTRO,  
				KEY_DATA_INICIO,  
				KEY_DATA_FIM,  
				KEY_AVALIACAO,  
				KEY_TOMOU_BANHO,  
				KEY_MEDICAMENTOS, 
				KEY_ENVIAR_FRALDA,  
				KEY_ENVIAR_LENCOS,  
				KEY_ENVIAR_POMADA,  
				KEY_ENVIAR_LEITE,  
				KEY_ENVIAR_OUTROS,  
				KEY_STATS_EV_EXECUTADO,
				KEY_DATA_ATUALIZACAO,
				KEY_ID_EVENTO,
				KEY_NOME_EVENTO,
				KEY_TIPO}, null, null, null, null, KEY_DATA_ATUALIZACAO +" desc");
		
        c.moveToFirst(); 
        
        
        while(!c.isAfterLast()){

        	EventoExecutado eventoExecutado = new EventoExecutado();
        	
        	eventoExecutado.setId(c.getLong(0));
        	eventoExecutado.setEvento(new Evento());	
        	eventoExecutado.getEvento().setCodigoEvento(c.getString(1));
        	eventoExecutado.getEvento().setUnidadeMedida(c.getString(2));
        	
        	eventoExecutado.setCliente(new Cliente());
        	eventoExecutado.getCliente().setId(c.getLong(3));
        	eventoExecutado.getCliente().setNome(c.getString(4));
        	
        	eventoExecutado.setUsuario(new Usuario());
        	eventoExecutado.getUsuario().setId(c.getLong(5));
        	eventoExecutado.getUsuario().setNome(c.getString(6));

        	eventoExecutado.setClasse(new Classe());
        	eventoExecutado.getClasse().setId(c.getLong(7));
        	eventoExecutado.getClasse().setNome(c.getString(8));
        	eventoExecutado.getClasse().setCodigoClasse(c.getString(9));
        	eventoExecutado.getClasse().setTurma(c.getString(10));
        	
        	eventoExecutado.setAluno(new Aluno());
        	eventoExecutado.getAluno().setId(c.getLong(11));
        	eventoExecutado.getAluno().setNome(c.getString(12));
        	eventoExecutado.getAluno().setCodigoAluno(c.getString(13));
        	
		
        	eventoExecutado.setObservacoes(c.getString(14));		
        	eventoExecutado.setQuantidade(c.getLong(15));		
        	eventoExecutado.setDataCadastro(c.getString(16) == null ? null : dateFormat.parse(c.getString(16)));		
        	eventoExecutado.setDataInicio(c.getString(17) == null ? null : dateFormat.parse(c.getString(17)));	 
        	eventoExecutado.setDataFim(c.getString(18) == null ? null :dateFormat.parse(c.getString(18)));	 
        	eventoExecutado.setAvaliacaoEvento(c.getInt(19));	 
        	eventoExecutado.setTomouBanho(c.getInt(20) == 0 ? false:true);	 
        	eventoExecutado.setMedicamentos(c.getString(21));	
        	eventoExecutado.setEnviarFralda(c.getInt(22) == 0 ? false:true);		
        	eventoExecutado.setEnviarLencos(c.getInt(23) == 0 ? false:true);		
        	eventoExecutado.setEnviarPomada(c.getInt(24) == 0 ? false:true);		
        	eventoExecutado.setEnviarLeite(c.getInt(25) == 0 ? false:true);		
        	eventoExecutado.setEnviarOutros(c.getString(26));		
        	eventoExecutado.setStatusEventoExecutado(c.getInt(27));	
        	eventoExecutado.setDataAtualizacao(c.getLong(28));
        	
        	eventoExecutado.getEvento().setId(c.getLong(29));
        	eventoExecutado.getEvento().setNome(c.getString(30));
        	eventoExecutado.setTipo(c.getInt(31));
        	
        	

        	
            lista.add(eventoExecutado);  
            c.moveToNext();  
        }  
        
        
        
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(c != null){
					c.close();
				}
				close();
			}catch(Exception ex){
				// fodeu
				ex.printStackTrace();
			}

		}         
        
        return lista; 
	}
	
	
	public EventoExecutado returnById(long id){
		EventoExecutado eventoExecutado = null;
		
		Cursor c = null;
		try{
		 open();
		 c = mDb.query(true, DB_TABLE,
				new String[]{
				KEY_ID,
				KEY_CODIGO_EVENTO,  
				KEY_UNID_MED,  
				KEY_ID_CLIENTE,  
				KEY_NOME_CLIENTE,  
				KEY_ID_USUARIO,  
				KEY_NOME_USUARIO,  
				KEY_ID_CLASSE,  
				KEY_NOME_CLASSE,  
				KEY_CODIGO_CLASSE,  
				KEY_TURMA_CLASSE,
				KEY_ID_ALUNO,
				KEY_NOME_ALUNO,
				KEY_CODIGO_ALUNO,				
				KEY_OBSERVACOES,  
				KEY_QUANTIDADE,  
				KEY_DATA_CADASTRO,  
				KEY_DATA_INICIO,  
				KEY_DATA_FIM,  
				KEY_AVALIACAO,  
				KEY_TOMOU_BANHO,  
				KEY_MEDICAMENTOS, 
				KEY_ENVIAR_FRALDA,  
				KEY_ENVIAR_LENCOS,  
				KEY_ENVIAR_POMADA,  
				KEY_ENVIAR_LEITE,  
				KEY_ENVIAR_OUTROS,  
				KEY_STATS_EV_EXECUTADO,
				KEY_DATA_ATUALIZACAO,
				KEY_ID_EVENTO,
				KEY_NOME_EVENTO,
				KEY_TIPO}, KEY_ID + "=" +id,
		null, null,null,null, null);
		
		c.moveToFirst();
				
		if(c.getCount() != 0)
		{
        	eventoExecutado = new EventoExecutado();
        	
        	eventoExecutado.setId(c.getLong(0));
        	eventoExecutado.setEvento(new Evento());	
        	eventoExecutado.getEvento().setCodigoEvento(c.getString(1));
        	eventoExecutado.getEvento().setUnidadeMedida(c.getString(2));
        	
        	eventoExecutado.setCliente(new Cliente());
        	eventoExecutado.getCliente().setId(c.getLong(3));
        	eventoExecutado.getCliente().setNome(c.getString(4));
        	
        	eventoExecutado.setUsuario(new Usuario());
        	eventoExecutado.getUsuario().setId(c.getLong(5));
        	eventoExecutado.getUsuario().setNome(c.getString(6));

        	eventoExecutado.setClasse(new Classe());
        	eventoExecutado.getClasse().setId(c.getLong(7));
        	eventoExecutado.getClasse().setNome(c.getString(8));
        	eventoExecutado.getClasse().setCodigoClasse(c.getString(9));
        	eventoExecutado.getClasse().setTurma(c.getString(10));
        	
        	eventoExecutado.setAluno(new Aluno());
        	eventoExecutado.getAluno().setId(c.getLong(11));
        	eventoExecutado.getAluno().setNome(c.getString(12));
        	eventoExecutado.getAluno().setCodigoAluno(c.getString(13));
        	
		
        	eventoExecutado.setObservacoes(c.getString(14));		
        	eventoExecutado.setQuantidade(c.getLong(15));		
        	eventoExecutado.setDataCadastro(c.getString(16) == null ? null : dateFormat.parse(c.getString(16)));		
        	eventoExecutado.setDataInicio(c.getString(17) == null ? null : dateFormat.parse(c.getString(17)));	 
        	eventoExecutado.setDataFim(c.getString(18) == null ? null :dateFormat.parse(c.getString(18)));	 
        	eventoExecutado.setAvaliacaoEvento(c.getInt(19));	 
        	eventoExecutado.setTomouBanho(c.getInt(20) == 0 ? false:true);	 
        	eventoExecutado.setMedicamentos(c.getString(21));	
        	eventoExecutado.setEnviarFralda(c.getInt(22) == 0 ? false:true);		
        	eventoExecutado.setEnviarLencos(c.getInt(23) == 0 ? false:true);		
        	eventoExecutado.setEnviarPomada(c.getInt(24) == 0 ? false:true);		
        	eventoExecutado.setEnviarLeite(c.getInt(25) == 0 ? false:true);		
        	eventoExecutado.setEnviarOutros(c.getString(26));		
        	eventoExecutado.setStatusEventoExecutado(c.getInt(27));	
        	eventoExecutado.setDataAtualizacao(c.getLong(28));
        	
        	eventoExecutado.getEvento().setId(c.getLong(29));
        	eventoExecutado.getEvento().setNome(c.getString(30));
        	
        	eventoExecutado.setTipo(c.getInt(31));

		}
		
		
		
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(c != null){
					c.close();
				}
				close();
			}catch(Exception ex){
				// fodeu
				ex.printStackTrace();
			}

		} 		
		
    	return eventoExecutado;
	}
	
	public List<EventoExecutado> returnByDataSelected(long idAluno,Calendar selectedDate){
		List<EventoExecutado> lista = new ArrayList<EventoExecutado>(); 
		
		Calendar dataInicio = Calendar.getInstance();
		
		dataInicio.set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH));
		dataInicio.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH));
		dataInicio.set(Calendar.YEAR, selectedDate.get(Calendar.YEAR));		
		dataInicio.set(Calendar.HOUR_OF_DAY, 0);
		dataInicio.set(Calendar.MINUTE, 0);
		dataInicio.set(Calendar.SECOND, 0);
		dataInicio.set(Calendar.MILLISECOND, 0);
					
		Calendar dataFim = Calendar.getInstance();
		
		dataFim.set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH));
		dataFim.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH));
		dataFim.set(Calendar.YEAR, selectedDate.get(Calendar.YEAR));		
		dataFim.set(Calendar.HOUR_OF_DAY, 23);
		dataFim.set(Calendar.MINUTE, 59);
		dataFim.set(Calendar.SECOND, 59);
		dataFim.set(Calendar.MILLISECOND, 999);
		
		Cursor c = null;
		try{
		 open();
		 c = mDb.query(DB_TABLE, 
				new String[]{
				KEY_ID,
				KEY_CODIGO_EVENTO,  
				KEY_UNID_MED,  
				KEY_ID_CLIENTE,  
				KEY_NOME_CLIENTE,  
				KEY_ID_USUARIO,  
				KEY_NOME_USUARIO,  
				KEY_ID_CLASSE,  
				KEY_NOME_CLASSE,  
				KEY_CODIGO_CLASSE,  
				KEY_TURMA_CLASSE,
				KEY_ID_ALUNO,
				KEY_NOME_ALUNO,
				KEY_CODIGO_ALUNO,
				KEY_OBSERVACOES,  
				KEY_QUANTIDADE,  
				KEY_DATA_CADASTRO,  
				KEY_DATA_INICIO,  
				KEY_DATA_FIM,  
				KEY_AVALIACAO,  
				KEY_TOMOU_BANHO,  
				KEY_MEDICAMENTOS, 
				KEY_ENVIAR_FRALDA,  
				KEY_ENVIAR_LENCOS,  
				KEY_ENVIAR_POMADA,  
				KEY_ENVIAR_LEITE,  
				KEY_ENVIAR_OUTROS,  
				KEY_STATS_EV_EXECUTADO,
				KEY_DATA_ATUALIZACAO,
				KEY_ID_EVENTO,
				KEY_NOME_EVENTO,
				KEY_TIPO}, 
				KEY_DATA_CADASTRO+" >= Datetime('"+dateFormat.format(dataInicio.getTime())+"') and "+KEY_DATA_CADASTRO+ " <= Datetime('"+dateFormat.format(dataFim.getTime())+"')" +
						"and "+KEY_ID_ALUNO+"="+idAluno+" order by "+KEY_DATA_ATUALIZACAO+" asc" ,
				null, null,null,null,null);
		
        c.moveToFirst(); 
        
        
        while(!c.isAfterLast()){

        	EventoExecutado eventoExecutado = new EventoExecutado();
        	
        	eventoExecutado.setId(c.getLong(0));
        	eventoExecutado.setEvento(new Evento());	
        	eventoExecutado.getEvento().setCodigoEvento(c.getString(1));
        	eventoExecutado.getEvento().setUnidadeMedida(c.getString(2));
        	
        	eventoExecutado.setCliente(new Cliente());
        	eventoExecutado.getCliente().setId(c.getLong(3));
        	eventoExecutado.getCliente().setNome(c.getString(4));
        	
        	eventoExecutado.setUsuario(new Usuario());
        	eventoExecutado.getUsuario().setId(c.getLong(5));
        	eventoExecutado.getUsuario().setNome(c.getString(6));

        	eventoExecutado.setClasse(new Classe());
        	eventoExecutado.getClasse().setId(c.getLong(7));
        	eventoExecutado.getClasse().setNome(c.getString(8));
        	eventoExecutado.getClasse().setCodigoClasse(c.getString(9));
        	eventoExecutado.getClasse().setTurma(c.getString(10));
        	
        	eventoExecutado.setAluno(new Aluno());
        	eventoExecutado.getAluno().setId(c.getLong(11));
        	eventoExecutado.getAluno().setNome(c.getString(12));
        	eventoExecutado.getAluno().setCodigoAluno(c.getString(13));
        	
		
        	eventoExecutado.setObservacoes(c.getString(14));		
        	eventoExecutado.setQuantidade(c.getLong(15));		
        	eventoExecutado.setDataCadastro(c.getString(16) == null ? null : dateFormat.parse(c.getString(16)));		
        	eventoExecutado.setDataInicio(c.getString(17) == null ? null : dateFormat.parse(c.getString(17)));	 
        	eventoExecutado.setDataFim(c.getString(18) == null ? null :dateFormat.parse(c.getString(18)));	 
        	eventoExecutado.setAvaliacaoEvento(c.getInt(19));	 
        	eventoExecutado.setTomouBanho(c.getInt(20) == 0 ? false:true);	 
        	eventoExecutado.setMedicamentos(c.getString(21));	
        	eventoExecutado.setEnviarFralda(c.getInt(22) == 0 ? false:true);		
        	eventoExecutado.setEnviarLencos(c.getInt(23) == 0 ? false:true);		
        	eventoExecutado.setEnviarPomada(c.getInt(24) == 0 ? false:true);		
        	eventoExecutado.setEnviarLeite(c.getInt(25) == 0 ? false:true);		
        	eventoExecutado.setEnviarOutros(c.getString(26));		
        	eventoExecutado.setStatusEventoExecutado(c.getInt(27));	
        	eventoExecutado.setDataAtualizacao(c.getLong(28));
        	
        	eventoExecutado.getEvento().setId(c.getLong(29));
        	eventoExecutado.getEvento().setNome(c.getString(30));
        	
        	eventoExecutado.setTipo(c.getInt(31));

        	
            lista.add(eventoExecutado);  
            c.moveToNext();  
        }  
        
        
        
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(c != null){
					c.close();
				}
				close();
			}catch(Exception ex){
				// fodeu
				ex.printStackTrace();
			}

		}        
        
        return lista; 
	}
	
	public List<EventoExecutado> returnLembretes(long idAluno){
		List<EventoExecutado> lista = new ArrayList<EventoExecutado>(); 
		
		Calendar dataInicio = Calendar.getInstance();
		
		dataInicio.set(Calendar.HOUR_OF_DAY, 0);
		dataInicio.set(Calendar.MINUTE, 0);
		dataInicio.set(Calendar.SECOND, 0);
		dataInicio.set(Calendar.MILLISECOND, 0);
					
		Calendar dataFim = Calendar.getInstance();
		dataFim.set(Calendar.HOUR_OF_DAY, 23);
		dataFim.set(Calendar.MINUTE, 59);
		dataFim.set(Calendar.SECOND, 59);
		dataFim.set(Calendar.MILLISECOND, 999);
		
		Cursor c = null;
		try{
		 open();
		 c = mDb.query(DB_TABLE, 
				new String[]{
				KEY_ID,
				KEY_CODIGO_EVENTO,  
				KEY_UNID_MED,  
				KEY_ID_CLIENTE,  
				KEY_NOME_CLIENTE,  
				KEY_ID_USUARIO,  
				KEY_NOME_USUARIO,  
				KEY_ID_CLASSE,  
				KEY_NOME_CLASSE,  
				KEY_CODIGO_CLASSE,  
				KEY_TURMA_CLASSE,
				KEY_ID_ALUNO,
				KEY_NOME_ALUNO,
				KEY_CODIGO_ALUNO,
				KEY_OBSERVACOES,  
				KEY_QUANTIDADE,  
				KEY_DATA_CADASTRO,  
				KEY_DATA_INICIO,  
				KEY_DATA_FIM,  
				KEY_AVALIACAO,  
				KEY_TOMOU_BANHO,  
				KEY_MEDICAMENTOS, 
				KEY_ENVIAR_FRALDA,  
				KEY_ENVIAR_LENCOS,  
				KEY_ENVIAR_POMADA,  
				KEY_ENVIAR_LEITE,  
				KEY_ENVIAR_OUTROS,  
				KEY_STATS_EV_EXECUTADO,
				KEY_DATA_ATUALIZACAO,
				KEY_ID_EVENTO,
				KEY_NOME_EVENTO,
				KEY_TIPO}, 
				"( " + KEY_TIPO+" = " + Constantes.TIPO_LEMBRETE +	"or "+ KEY_TIPO+ " = "+Constantes.TIPO_LEMBRETE_GEN  +" ) and "+KEY_ID_ALUNO+"="+idAluno+" order by "+KEY_DATA_ATUALIZACAO+" asc" ,
				null, null,null,null,null);
		
        c.moveToFirst(); 
        
        
        while(!c.isAfterLast()){

        	EventoExecutado eventoExecutado = new EventoExecutado();
        	
        	eventoExecutado.setId(c.getLong(0));
        	eventoExecutado.setEvento(new Evento());	
        	eventoExecutado.getEvento().setCodigoEvento(c.getString(1));
        	eventoExecutado.getEvento().setUnidadeMedida(c.getString(2));
        	
        	eventoExecutado.setCliente(new Cliente());
        	eventoExecutado.getCliente().setId(c.getLong(3));
        	eventoExecutado.getCliente().setNome(c.getString(4));
        	
        	eventoExecutado.setUsuario(new Usuario());
        	eventoExecutado.getUsuario().setId(c.getLong(5));
        	eventoExecutado.getUsuario().setNome(c.getString(6));

        	eventoExecutado.setClasse(new Classe());
        	eventoExecutado.getClasse().setId(c.getLong(7));
        	eventoExecutado.getClasse().setNome(c.getString(8));
        	eventoExecutado.getClasse().setCodigoClasse(c.getString(9));
        	eventoExecutado.getClasse().setTurma(c.getString(10));
        	
        	eventoExecutado.setAluno(new Aluno());
        	eventoExecutado.getAluno().setId(c.getLong(11));
        	eventoExecutado.getAluno().setNome(c.getString(12));
        	eventoExecutado.getAluno().setCodigoAluno(c.getString(13));
        	
		
        	eventoExecutado.setObservacoes(c.getString(14));		
        	eventoExecutado.setQuantidade(c.getLong(15));		
        	eventoExecutado.setDataCadastro(c.getString(16) == null ? null : dateFormat.parse(c.getString(16)));		
        	eventoExecutado.setDataInicio(c.getString(17) == null ? null : dateFormat.parse(c.getString(17)));	 
        	eventoExecutado.setDataFim(c.getString(18) == null ? null :dateFormat.parse(c.getString(18)));	 
        	eventoExecutado.setAvaliacaoEvento(c.getInt(19));	 
        	eventoExecutado.setTomouBanho(c.getInt(20) == 0 ? false:true);	 
        	eventoExecutado.setMedicamentos(c.getString(21));	
        	eventoExecutado.setEnviarFralda(c.getInt(22) == 0 ? false:true);		
        	eventoExecutado.setEnviarLencos(c.getInt(23) == 0 ? false:true);		
        	eventoExecutado.setEnviarPomada(c.getInt(24) == 0 ? false:true);		
        	eventoExecutado.setEnviarLeite(c.getInt(25) == 0 ? false:true);		
        	eventoExecutado.setEnviarOutros(c.getString(26));		
        	eventoExecutado.setStatusEventoExecutado(c.getInt(27));	
        	eventoExecutado.setDataAtualizacao(c.getLong(28));
        	
        	eventoExecutado.getEvento().setId(c.getLong(29));
        	eventoExecutado.getEvento().setNome(c.getString(30));
        	
        	eventoExecutado.setTipo(c.getInt(31));

        	
            lista.add(eventoExecutado);  
            c.moveToNext();  
        }  
        
        
        
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(c != null){
					c.close();
				}
				close();
			}catch(Exception ex){
				// fodeu
				ex.printStackTrace();
			}

		}        
        
        return lista; 
	}	
	
	
	private boolean update(EventoExecutado  eventoExecutado){
		ContentValues contentValues = new ContentValues();
		
	//	contentValues.put(KEY_ID  			,  eventoExecutado.getId());	
		contentValues.put(KEY_CODIGO_EVENTO  ,  eventoExecutado.getEvento().getCodigoEvento());	
		contentValues.put(KEY_UNID_MED		  ,  eventoExecutado.getEvento().getUnidadeMedida());		
		contentValues.put(KEY_ID_CLIENTE	  ,  eventoExecutado.getCliente().getId());		
		contentValues.put(KEY_NOME_CLIENTE   ,  eventoExecutado.getCliente().getNome());		
		contentValues.put(KEY_ID_USUARIO	  ,  eventoExecutado.getUsuario().getId());		
		contentValues.put(KEY_NOME_USUARIO   ,  eventoExecutado.getUsuario().getNome());		
		contentValues.put(KEY_ID_CLASSE  	  ,  eventoExecutado.getClasse().getId());		
		contentValues.put(KEY_NOME_CLASSE    ,  eventoExecutado.getClasse().getNome());		
		contentValues.put(KEY_CODIGO_CLASSE  ,  eventoExecutado.getClasse().getCodigoClasse());		
		contentValues.put(KEY_TURMA_CLASSE  ,  eventoExecutado.getClasse().getTurma());	
		contentValues.put(KEY_ID_ALUNO  	,  eventoExecutado.getAluno().getId());		
		contentValues.put(KEY_NOME_ALUNO	,  eventoExecutado.getAluno().getNome());		
		contentValues.put(KEY_CODIGO_ALUNO  ,  eventoExecutado.getAluno().getCodigoAluno());		
		contentValues.put(KEY_OBSERVACOES    ,  eventoExecutado.getObservacoes());		
		contentValues.put(KEY_QUANTIDADE  	,  eventoExecutado.getQuantidade());		
		contentValues.put(KEY_DATA_CADASTRO  ,  eventoExecutado.getDataCadastro() == null ? null : dateFormat.format(eventoExecutado.getDataCadastro()));		
		contentValues.put(KEY_DATA_INICIO  	,   eventoExecutado.getDataInicio() == null ? null :  dateFormat.format(eventoExecutado.getDataInicio()));	 
		contentValues.put(KEY_DATA_FIM  		,  eventoExecutado.getDataFim() == null ? null : dateFormat.format(eventoExecutado.getDataFim()));	 
		contentValues.put(KEY_AVALIACAO  		,  eventoExecutado.getAvaliacaoEvento());	 
		contentValues.put(KEY_TOMOU_BANHO  	,  eventoExecutado.getTomouBanho());	 
		contentValues.put(KEY_MEDICAMENTOS  	,  eventoExecutado.getMedicamentos());	
		contentValues.put(KEY_ENVIAR_FRALDA  ,  eventoExecutado.getEnviarFralda());		
		contentValues.put(KEY_ENVIAR_LENCOS  ,  eventoExecutado.getEnviarLencos());		
		contentValues.put(KEY_ENVIAR_POMADA  ,  eventoExecutado.getEnviarPomada());		
		contentValues.put(KEY_ENVIAR_LEITE  ,  eventoExecutado.getEnviarLeite());		
		contentValues.put(KEY_ENVIAR_OUTROS  ,  eventoExecutado.getEnviarOutros());		
		contentValues.put(KEY_STATS_EV_EXECUTADO,  eventoExecutado.getStatusEventoExecutado());	
		contentValues.put(KEY_DATA_ATUALIZACAO,  eventoExecutado.getDataAtualizacao());	
		
		contentValues.put(KEY_ID_EVENTO,  		eventoExecutado.getEvento().getId());	
		contentValues.put(KEY_NOME_EVENTO,  	eventoExecutado.getEvento().getNome());	
		
		contentValues.put(KEY_TIPO,  	eventoExecutado.getTipo());		
		
		return mDb.update(DB_TABLE, contentValues, KEY_ID + "=" +eventoExecutado.getId(), null)>0;
	}
	
	public void deleteAllData(){
		try{
			open();
			mDb.execSQL("delete from "+DB_TABLE);
		}catch(Exception e){
			
		}finally{
			try{
				close();
			}catch(Exception ex){
				// fodeu
				ex.printStackTrace();
			}
		}	
	}
	
	public void storeEventosExecutadosInDataBase(final List<EventoExecutado> eventosExecutados){
		
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void...params) {
								
				try{
					open();
					
					for(EventoExecutado eventoExecutado:eventosExecutados){
						merge(eventoExecutado);					
					}
										
				}catch(Exception ex){
					ex.printStackTrace();
				}finally{
					try{
						close();
					}catch(Exception ex){
						// fodeu
						ex.printStackTrace();
					}
				}
				return null;
			}
			
			
		}.execute();
	}
	
}
