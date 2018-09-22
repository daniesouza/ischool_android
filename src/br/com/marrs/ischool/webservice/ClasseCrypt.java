package br.com.marrs.ischool.webservice;

//------------------------------------------------------------------------------
// <wsdl2code-generated>
//    This code was generated by http://www.wsdl2code.com version  2.5
//
// Date Of Creation: 7/6/2014 12:42:21 AM
//    Please dont change this code, regeneration will override your changes
//</wsdl2code-generated>
//
//------------------------------------------------------------------------------
//
//This source code was auto-generated by Wsdl2Code  Version
//

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Locale;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import br.com.marrs.ischool.entidades.Classe;
import br.com.marrs.ischool.util.Crypto;


public class ClasseCrypt implements KvmSerializable {
    
    public VectorByte ano;
    public VectorByte ativo;
    public VectorByte codigoClasseField;
    public VectorByte dataCadastro;
    public VectorByte idClasseField;
    public VectorByte nome;
    public VectorByte turma;
    
    public ClasseCrypt(){}
    
    public ClasseCrypt(SoapObject soapObject)
    {
        if (soapObject == null)
            return;
        if (soapObject.hasProperty("ano"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("ano");
            ano = new VectorByte(j);
        }
        if (soapObject.hasProperty("ativo"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("ativo");
            ativo = new VectorByte(j);
        }
        if (soapObject.hasProperty("codigoClasse"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("codigoClasse");
            codigoClasseField = new VectorByte(j);
        }
        if (soapObject.hasProperty("dataCadastro"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("dataCadastro");
            dataCadastro = new VectorByte(j);
        }
        if (soapObject.hasProperty("idClasse"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("idClasse");
            idClasseField = new VectorByte(j);
        }
        if (soapObject.hasProperty("nome"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("nome");
            nome = new VectorByte(j);
        }
        if (soapObject.hasProperty("turma"))
        {
            SoapPrimitive j = (SoapPrimitive)soapObject.getProperty("turma");
            turma = new VectorByte(j);
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:               
                return ano == null ? null : ano.toString();
            case 1:
                return ativo == null ? null : ativo.toString();
            case 2:
                return codigoClasseField == null ? null : codigoClasseField.toString();
            case 3:
                return dataCadastro == null ? null : dataCadastro.toString();
            case 4:
                return idClasseField == null ? null : idClasseField.toString();
            case 5:
                return nome == null ? null : nome.toString();
            case 6:
                return turma == null ? null : turma.toString();
        }
        return null;
    }
    
    @Override
    public int getPropertyCount() {
        return 7;
    }
    
    @Override
    public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ano";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ativo";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "codigoClasse";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "dataCadastro";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "idClasse";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "nome";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "turma";
                break;
        }
    }
    
    @Override
    public void setProperty(int arg0, Object arg1) {
    }
    
    public Classe decriptarClasse() throws Exception{
    	Classe classe = new Classe();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.US);
    	
    	classe.setId(this.idClasseField == null ? null : Long.valueOf(new String(Crypto.decrypt(this.idClasseField.toBytes()))));
    	classe.setAno(this.ano == null ? null : Integer.valueOf(new String(Crypto.decrypt(this.ano.toBytes()))));
    	classe.setAtivo(this.ativo == null ? null : Boolean.valueOf(new String(Crypto.decrypt(this.ativo.toBytes()))));
    	classe.setCodigoClasse(this.codigoClasseField == null ? null : new String(Crypto.decrypt(this.codigoClasseField.toBytes())));
    	classe.setDataCadastro(this.dataCadastro == null ? null : dateFormat.parse(new String(Crypto.decrypt(this.dataCadastro.toBytes()))));
    	classe.setNome(this.nome == null ? null : new String(Crypto.decrypt(this.nome.toBytes())));
    	classe.setTurma(this.turma == null ? null : new String(Crypto.decrypt(this.turma.toBytes())));
    	
    	
    	return classe;
    }


    
}
