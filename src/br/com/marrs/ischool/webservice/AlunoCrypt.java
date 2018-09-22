package br.com.marrs.ischool.webservice;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Locale;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import br.com.marrs.ischool.entidades.Aluno;
import br.com.marrs.ischool.util.Crypto;


public class AlunoCrypt implements KvmSerializable {
    
    public VectorByte idAluno;
    public VectorByte nome;
    public VectorByte ativo;
    public VectorByte codigoAluno;
    public VectorByte cpf;
    public VectorByte dataCadastro;
    public VectorByte dataNascimento;
    public VectorByte email;
    public VectorByte endereco;
    public VectorByte faltas;
    public VectorByte nota;
    public VectorByte observacoes;
    public VectorByte rg;
    public VectorByte telefone;
    public VectorByte semClassesAtribuidas;
    
    public AlunoCrypt(){}
    
    public AlunoCrypt(SoapObject soapObject)
    {
        if (soapObject == null)
            return;
        if (soapObject.hasProperty("ativo"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("ativo");
            ativo = new VectorByte(j);
        }
        if (soapObject.hasProperty("codigoAluno"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("codigoAluno");
            codigoAluno = new VectorByte(j);
        }
        if (soapObject.hasProperty("cpf"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("cpf");
            cpf = new VectorByte(j);
        }
        if (soapObject.hasProperty("dataCadastro"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("dataCadastro");
            dataCadastro = new VectorByte(j);
        }
        if (soapObject.hasProperty("dataNascimento"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("dataNascimento");
            dataNascimento = new VectorByte(j);
        }
        if (soapObject.hasProperty("email"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("email");
            email = new VectorByte(j);
        }
        if (soapObject.hasProperty("endereco"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("endereco");
            endereco = new VectorByte(j);
        }
        if (soapObject.hasProperty("faltas"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("faltas");
            faltas = new VectorByte(j);
        }
        if (soapObject.hasProperty("idAluno"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("idAluno");
            idAluno = new VectorByte(j);
        }
        if (soapObject.hasProperty("nome"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("nome");
            nome = new VectorByte(j);
        }
        if (soapObject.hasProperty("nota"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("nota");
            nota = new VectorByte(j);
        }
        if (soapObject.hasProperty("observacoes"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("observacoes");
            observacoes = new VectorByte(j);
        }
        if (soapObject.hasProperty("rg"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("rg");
            rg = new VectorByte(j);
        }
        if (soapObject.hasProperty("telefone"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("telefone");
            telefone = new VectorByte(j);
        }       
        if (soapObject.hasProperty("semClassesAtribuidas"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("semClassesAtribuidas");
            semClassesAtribuidas = new VectorByte(j);
        }        
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return ativo.toString();
            case 1:
                return codigoAluno.toString();
            case 2:
                return cpf.toString();
            case 3:
                return dataCadastro.toString();
            case 4:
                return dataNascimento.toString();
            case 5:
                return email.toString();
            case 6:
                return endereco.toString();
            case 7:
                return faltas.toString();
            case 8:
                return idAluno.toString();
            case 9:
                return nome.toString();
            case 10:
                return nota.toString();
            case 11:
                return observacoes.toString();
            case 12:
                return rg.toString();
            case 13:
                return telefone.toString();
            case 14:
                return semClassesAtribuidas.toString();                
        }
        return null;
    }
    
    @Override
    public int getPropertyCount() {
        return 15;
    }
    
    @Override
    public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ativo";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "codigoAluno";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "cpf";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "dataCadastro";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "dataNascimento";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "email";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "endereco";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "faltas";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "idAluno";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "nome";
                break;
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "nota";
                break;
            case 11:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "observacoes";
                break;
            case 12:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "rg";
                break;
            case 13:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "telefone";
                break;
            case 14:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "semClassesAtribuidas";
                break;                
        }
    }
    
    @Override
    public void setProperty(int arg0, Object arg1) {
    }
    
    public Aluno decriptarAluno() throws Exception{
    	
    	Aluno aluno = new Aluno();
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.US);
    	
    	aluno.setId(this.idAluno == null ? null : Long.valueOf(new String(Crypto.decrypt(this.idAluno.toBytes()))));
    	aluno.setAtivo(this.ativo == null ? null : Boolean.valueOf(new String(Crypto.decrypt(this.ativo.toBytes()))));
    	aluno.setCodigoAluno(this.codigoAluno == null ? null : new String(Crypto.decrypt(this.codigoAluno.toBytes())));
    	aluno.setCpf(this.cpf == null ? null : Long.valueOf(new String(Crypto.decrypt(this.cpf.toBytes()))));
    	aluno.setDataCadastro(this.dataCadastro == null ? null : dateFormat.parse(new String(Crypto.decrypt(this.dataCadastro.toBytes()))));
    	aluno.setDataNascimento(this.dataNascimento == null ? null : dateFormat.parse(new String(Crypto.decrypt(this.dataNascimento.toBytes()))));
    	aluno.setEmail(this.email == null ? null : new String(Crypto.decrypt(this.email.toBytes())));
    	aluno.setEndereco(this.endereco == null ? null : new String(Crypto.decrypt(this.endereco.toBytes())));
    	aluno.setFaltas(this.faltas == null ? null : Integer.valueOf(new String(Crypto.decrypt(this.faltas.toBytes()))));
    	aluno.setNome(this.nome == null ? null : new String(Crypto.decrypt(this.nome.toBytes())));
    	aluno.setNota(this.nota == null ? null : Double.valueOf(new String(Crypto.decrypt(this.nota.toBytes()))));
    	aluno.setObservacoes(this.observacoes == null ? null : new String(Crypto.decrypt(this.observacoes.toBytes())));
    	aluno.setRg(this.rg == null ? null : new String(Crypto.decrypt(this.rg.toBytes())));
    	aluno.setTelefone(this.telefone == null ? null : new String(Crypto.decrypt(this.telefone.toBytes())));
    	aluno.setSemClassesAtribuidas(this.semClassesAtribuidas == null ? null : Boolean.valueOf(new String(Crypto.decrypt(this.semClassesAtribuidas.toBytes()))));
    	
    	return aluno;
    }

}
