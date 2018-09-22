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
import java.io.EOFException;
import java.net.SocketException;
import java.util.List;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.KeepAliveHttpTransportSE;

import br.com.marrs.ischool.util.Constantes;
import br.com.marrs.ischool.webservice.WS_Enums.SoapProtocolVersion;


public class IschoolWebServiceImplService {
    
	public static IschoolWebServiceImplService instance = null;
    public String NAMESPACE ="http://service.ischool.com.br/";
    public String url="http://"+Constantes.IP_CONNECT+":8443/ischool-webService/IschoolWebServiceImpl";
    public int timeOut = 15000;
    private static final int MAX_RETRIES = 5;
    private int retryCount;
    public IWsdl2CodeEvents eventHandler;
    public SoapProtocolVersion soapVersion;
    
    
    public static IschoolWebServiceImplService getInstance(){
    	
    	if(instance == null){
    		instance = new IschoolWebServiceImplService();
    	}
    	
    	return instance;
    }
    
    public IschoolWebServiceImplService(){}
    
    public IschoolWebServiceImplService(IWsdl2CodeEvents eventHandler){
        this.eventHandler = eventHandler;
    }
    public IschoolWebServiceImplService(IWsdl2CodeEvents eventHandler,String url)
    {
        this.eventHandler = eventHandler;
        this.url = url;
    }
    public IschoolWebServiceImplService(IWsdl2CodeEvents eventHandler,String url,int timeOutInSeconds)
    {
        this.eventHandler = eventHandler;
        this.url = url;
        this.setTimeOut(timeOutInSeconds);
    }
    public void setTimeOut(int seconds){
        this.timeOut = seconds * 1000;
    }
    public void setUrl(String url){
        this.url = url;
    }
    
    public VectorByte carregarArquivo(VectorByte encIdUsuario,VectorByte encIdMensagem,VectorByte encOffset,VectorByte encDataAtualizacao,VectorByte keyHash) throws Exception{
        return carregarArquivo(encIdUsuario, encIdMensagem,encOffset,encDataAtualizacao, keyHash, null);
    }
    
    public VectorByte carregarArquivo(VectorByte encIdUsuario,VectorByte encIdMensagem,VectorByte encOffset,VectorByte encDataAtualizacao,VectorByte keyHash,List<HeaderProperty> headers) throws Exception{
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = false;
        SoapObject soapReq = new SoapObject("http://service.ischool.com.br/","carregarArquivo");
        soapReq.addProperty("encIdUsuario",encIdUsuario.toString());
        soapReq.addProperty("encIdMensagem",encIdMensagem.toString());
        soapReq.addProperty("encOffset",encOffset.toString());
        soapReq.addProperty("encDataAtualizacao",encDataAtualizacao.toString());
        soapReq.addProperty("keyHash",keyHash.toString());
        soapEnvelope.setOutputSoapObject(soapReq);
        KeepAliveHttpTransportSE httpTransport = new KeepAliveHttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://service.ischool.com.br/carregarArquivo", soapEnvelope,headers);
            }else{
                httpTransport.call("http://service.ischool.com.br/carregarArquivo", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);           
                throw ex;
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result.getPropertyCount() > 0){
                    Object obj = result.getProperty(0);
                    SoapPrimitive j = (SoapPrimitive)obj;
                    VectorByte resultVariable = new VectorByte(j);
                    return resultVariable;
                }
            }
        }catch(SocketException ex){
            if (retryCount < MAX_RETRIES) {
                retryCount++;
               return carregarArquivo(encIdUsuario, encIdMensagem, encOffset,encDataAtualizacao,keyHash, headers);
             }else{
             	throw ex;
             }
         }catch(EOFException ex){
            if (retryCount < MAX_RETRIES) {
               retryCount++;
               return carregarArquivo(encIdUsuario, encIdMensagem, encOffset,encDataAtualizacao, keyHash, headers);
            }else{
            	throw ex;
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            throw e;
        }
        return null;
    }
    
    public VectorByte enviarMensagemWeb(VectorByte encIdUsuario,MensagemCrypt mensagemCrypt,VectorByte encDataAtualizacao,VectorByte keyHash) throws Exception{
        return enviarMensagemWeb(encIdUsuario, mensagemCrypt,encDataAtualizacao, keyHash, null);
    }
    
    public VectorByte enviarMensagemWeb(VectorByte encIdUsuario,MensagemCrypt mensagemCrypt,VectorByte encDataAtualizacao,VectorByte keyHash,List<HeaderProperty> headers) throws Exception{
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = false;
        SoapObject soapReq = new SoapObject("http://service.ischool.com.br/","enviarMensagemWeb");
        soapEnvelope.addMapping("http://service.ischool.com.br/","mensagemCrypt",new MensagemCrypt().getClass());
        soapReq.addProperty("encIdUsuario",encIdUsuario.toString());
        soapReq.addProperty("mensagemCrypt",mensagemCrypt);
        soapReq.addProperty("encDataAtualizacao",encDataAtualizacao.toString());
        soapReq.addProperty("keyHash",keyHash.toString());
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://service.ischool.com.br/enviarMensagemWeb", soapEnvelope,headers);
            }else{
                httpTransport.call("http://service.ischool.com.br/enviarMensagemWeb", soapEnvelope);
            }
            
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null){
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
                }               
                throw ex;              
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result.getPropertyCount() > 0){
                    Object obj = result.getProperty(0);
                    SoapPrimitive j = (SoapPrimitive)obj;
                    VectorByte resultVariable = new VectorByte(j);
                    return resultVariable;
                }
            }
            
        }catch(SocketException ex){
            if (retryCount < MAX_RETRIES) {
                retryCount++;
                return enviarMensagemWeb(encIdUsuario, mensagemCrypt,encDataAtualizacao, keyHash, headers);
             }else{
             	throw ex;
             }
         }catch(EOFException ex){
            if (retryCount < MAX_RETRIES) {
               retryCount++;
               return enviarMensagemWeb(encIdUsuario, mensagemCrypt,encDataAtualizacao, keyHash, headers);
            }else{
            	throw ex;
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            throw e;
        }
        
        return null;
    }
    
    
    public VectormensagemCrypt listarMensagens(VectorByte encIdUsuario,VectorByte encIdAluno,VectorByte encIdsMensagens,VectorByte encDataAtualizacao,VectorByte keyHash) throws Exception{
        return listarMensagens(encIdUsuario, encIdAluno, encIdsMensagens,encDataAtualizacao, keyHash, null);
    }
    
    public VectormensagemCrypt listarMensagens(VectorByte encIdUsuario,VectorByte encIdAluno,VectorByte encIdsMensagens,VectorByte encDataAtualizacao,VectorByte keyHash,List<HeaderProperty> headers) throws Exception{
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = false;
        SoapObject soapReq = new SoapObject("http://service.ischool.com.br/","listarMensagens");
        soapReq.addProperty("encIdUsuario",encIdUsuario.toString());
        soapReq.addProperty("encIdAluno",encIdAluno.toString());
        soapReq.addProperty("encIdsMensagens",encIdsMensagens.toString());
        soapReq.addProperty("encDataAtualizacao",encDataAtualizacao.toString());
        soapReq.addProperty("keyHash",keyHash.toString());
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://service.ischool.com.br/listarMensagens", soapEnvelope,headers);
            }else{
                httpTransport.call("http://service.ischool.com.br/listarMensagens", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null){
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
                }               
                throw ex;              
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result.getPropertyCount() > 0){
//                    Object obj = result.getProperty(0);
//                    SoapObject j = (SoapObject)obj;
                    VectormensagemCrypt resultVariable = new VectormensagemCrypt(result);
                    return resultVariable;
                }
            }
        }catch(SocketException ex){
            if (retryCount < MAX_RETRIES) {
                retryCount++;
               return listarMensagens(encIdUsuario, encIdAluno, encIdsMensagens,encDataAtualizacao, keyHash, headers);
             }else{
             	throw ex;
             }
         }catch(EOFException ex){
            if (retryCount < MAX_RETRIES) {
               retryCount++;
              return listarMensagens(encIdUsuario, encIdAluno, encIdsMensagens,encDataAtualizacao, keyHash, headers);
            }else{
            	throw ex;
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            throw e;
        }
        return null;
    }
    
    
    public UsuarioCrypt loginDevice(VectorByte encUsuario,VectorByte encPass,VectorByte deviceRegId,VectorByte strDeviceReg,VectorByte tipoDevice) throws Exception{
        return loginDevice(encUsuario, encPass,deviceRegId,strDeviceReg,tipoDevice, null);
    }
    
    public UsuarioCrypt loginDevice(VectorByte encUsuario,VectorByte encPass,VectorByte deviceRegId,VectorByte strDeviceReg,VectorByte tipoDevice,List<HeaderProperty> headers) throws Exception{
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = false;
        SoapObject soapReq = new SoapObject("http://service.ischool.com.br/","loginDevice");
        soapReq.addProperty("encUsuario",encUsuario.toString());
        soapReq.addProperty("encPass",encPass.toString());
        soapReq.addProperty("deviceRegId",deviceRegId.toString());
        soapReq.addProperty("strDeviceReg",strDeviceReg.toString());
        soapReq.addProperty("tipoDevice",tipoDevice.toString());
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://service.ischool.com.br/loginDevice", soapEnvelope,headers);
            }else{
                httpTransport.call("http://service.ischool.com.br/loginDevice", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);           
                throw ex;
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result.getPropertyCount() > 0){
                    Object obj = result.getProperty(0);
                    SoapObject j = (SoapObject)obj;
                    UsuarioCrypt resultVariable =  new UsuarioCrypt(j);
                    return resultVariable;
                    
                }
            }
        }catch(SocketException ex){
            if (retryCount < MAX_RETRIES) {
                retryCount++;
               return loginDevice(encUsuario, encPass,deviceRegId,strDeviceReg,tipoDevice, headers);
             }else{
             	throw ex;
             }
         }catch(EOFException ex){
            if (retryCount < MAX_RETRIES) {
               retryCount++;
              return loginDevice(encUsuario, encPass,deviceRegId,strDeviceReg,tipoDevice, headers);
            }else{
            	throw ex;
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            throw e;
        }
        return null;
    }
    
    

    
  
    public void registrarDevice(VectorByte encIdUsuario,VectorByte oldDeviceRegId,VectorByte newDeviceRegId,VectorByte tipoDevice, VectorByte encDataAtualizacao,VectorByte keyHash)  throws Exception{
        registrarDevice(encIdUsuario, oldDeviceRegId, newDeviceRegId,tipoDevice,encDataAtualizacao,keyHash, null);
    }
    
    public void registrarDevice(VectorByte encIdUsuario,VectorByte oldDeviceRegId,VectorByte newDeviceRegId,VectorByte tipoDevice,VectorByte encDataAtualizacao,VectorByte keyHash,List<HeaderProperty> headers)  throws Exception{
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = false;
        SoapObject soapReq = new SoapObject("http://service.ischool.com.br/","registrarDevice");
        soapReq.addProperty("encIdUsuario",encIdUsuario.toString());
        soapReq.addProperty("oldDeviceRegId",oldDeviceRegId.toString());
        soapReq.addProperty("newDeviceRegId",newDeviceRegId.toString());
        soapReq.addProperty("tipoDevice",tipoDevice.toString());
        soapReq.addProperty("encDataAtualizacao",encDataAtualizacao.toString());
        soapReq.addProperty("keyHash",keyHash.toString());
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("http://service.ischool.com.br/registrarDevice", soapEnvelope,headers);
            }else{
                httpTransport.call("http://service.ischool.com.br/registrarDevice", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);           
                throw ex;              
            }
        }catch(SocketException ex){
            if (retryCount < MAX_RETRIES) {
                retryCount++;
               registrarDevice(encIdUsuario, oldDeviceRegId, newDeviceRegId, tipoDevice,encDataAtualizacao, keyHash, headers);
             }else{
             	throw ex;
             }            
         }catch(EOFException ex){
            if (retryCount < MAX_RETRIES) {
               retryCount++;
               registrarDevice(encIdUsuario, oldDeviceRegId, newDeviceRegId, tipoDevice,encDataAtualizacao, keyHash, headers);
            }else{
            	throw ex;
            }            
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            throw e; 
        }
    }
    
    
    public VectoreventoExeCrypt listarEventosExecutados(VectorByte encIdUsuario,VectorByte encIdAluno,VectorByte datasAtualizacao,VectorByte encDataAtualizacao,VectorByte encDataPesquisa,
    		VectorByte keyHash) throws Exception{
        return listarEventosExecutados(encIdUsuario, encIdAluno, datasAtualizacao,encDataAtualizacao,encDataPesquisa, keyHash, null);
    }
    
    public VectoreventoExeCrypt listarEventosExecutados(VectorByte encIdUsuario,VectorByte encIdAluno,VectorByte datasAtualizacao,VectorByte encDataAtualizacao,VectorByte encDataPesquisa,
    		VectorByte keyHash,List<HeaderProperty> headers) throws Exception{
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = false;
        SoapObject soapReq = new SoapObject("http://service.ischool.com.br/","listarEventosExecutados");
        soapReq.addProperty("encIdUsuario",encIdUsuario.toString());
        soapReq.addProperty("encIdAluno",encIdAluno.toString());
        soapReq.addProperty("datasAtualizacao",datasAtualizacao.toString());
        soapReq.addProperty("encDataAtualizacao",encDataAtualizacao.toString());
        soapReq.addProperty("encDataPesquisa",encDataPesquisa.toString());
        soapReq.addProperty("keyHash",keyHash.toString());
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
//        httpTransport.getServiceConnection().setRequestProperty("Connection", "close");
//        httpTransport.getServiceConnection().setRequestProperty("KeepAlive","false");
        try{
            if (headers!=null){
                httpTransport.call("http://service.ischool.com.br/listarEventosExecutados", soapEnvelope,headers);
            }else{
                httpTransport.call("http://service.ischool.com.br/listarEventosExecutados", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);                
                throw ex;
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result.getPropertyCount() > 0){
//                    Object obj = result.getProperty(0);
//                    SoapObject j = (SoapObject)obj;
                    VectoreventoExeCrypt resultVariable = new VectoreventoExeCrypt(result);
                    return resultVariable;
                }
            }
            
        }catch(SocketException ex){
            if (retryCount < MAX_RETRIES) {
               retryCount++;
               return listarEventosExecutados(encIdUsuario, encIdAluno, datasAtualizacao,encDataAtualizacao,encDataPesquisa,keyHash, headers);
            }else{
            	throw ex;
            }
        }
        catch(EOFException ex){
            if (retryCount < MAX_RETRIES) {
               retryCount++;
               return listarEventosExecutados(encIdUsuario, encIdAluno, datasAtualizacao,encDataAtualizacao,encDataPesquisa,keyHash, headers);
            }else{
            	throw ex;
            }
        }
        catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            throw e;
        }
        return null;
    }
    
}