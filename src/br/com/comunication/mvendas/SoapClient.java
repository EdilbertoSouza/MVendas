package br.com.comunication.mvendas;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class SoapClient {	
	
/*	private static final String NAMESPACE = "http://10.0.2.2:9876/wstest"; //"http://192.168.2.100:81/ws/WSTESTE.apw"; 
	private static final String METHOD_NAME = "getTexto"; //"GETTEXTO";
	private static final String SOAP_ACTION = "\"" + "http://10.0.2.2:9876/wstest" + "/getTexto" + "\""; //http://192.168.2.100:81/GETTEXTO";
    private static String URL = "http://10.0.2.2:9876/wstest?wsdl"; //"http://192.168.2.100:81/ws/WSTESTE.apw?WSDL"; //"http://www.thomas-bayer.com/axis2/services/BLZService?wsdl";
*/  
	private static final String NAMESPACE = "http://192.168.2.100:81/ws/WSTESTE.apw"; 
	private static final String METHOD_NAME = "GETTEXTO";
	private static final String SOAP_ACTION = "http://192.168.2.100:81/GETTEXTO";
    private static String URL = "http://192.168.2.100:81/ws/WSTESTE.apw?WSDL"; 
        
    public SoapClient() { }
 
    public void listarClientes() {    	
    	
        //List clientes = new LinkedList();
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    	
        //PropertyInfo pi = new PropertyInfo();
    	//pi.name = "bank";
    	//pi.type = PropertyInfo.STRING_CLASS;
    	//pi.setValue("37050198");
    	//request.addProperty("37050198", pi); 
    	//request.addProperty(pi);
        
        SoapSerializationEnvelope envelope = new SoapSerEnv(SoapEnvelope.VER11); //new SoapSerEnv(SoapEnvelope.VER11);
        //envelope.bodyOut=request;
        //envelope.enc = SoapSerializationEnvelope.ENC;        
        //envelope.env="http://schemas.xmlsoap.org/soap/envelope/";        
        //envelope.implicitTypes = true;
        //envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);            
        //androidHttpTransport.debug = true;    	
        androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);

            Boolean response = (Boolean)envelope.getResponse();   
            
			System.out.println(response.toString());
            
            
            Log.d("teste", response.toString());          
            
            // Recebemos o retorno, convertendo em SoapObject
            //SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
            //int propsNum = resultsRequestSOAP.getPropertyCount();

            /*
            // Iteração para "ler" os objetos recebidos
            for (int i = 0; i < propsNum; i++) {
                SoapObject element = (SoapObject) resultsRequestSOAP.getProperty(i);
                clientes.add(element.getProperty("cTexto").toString());
                Log.i("teste", element.getProperty("cTexto").toString());
            }            
            Log.i("teste", "Finalizando o laço");
            */
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (XmlPullParserException e) {
			System.out.println(e.getMessage());
        } catch (Exception e) {
            Log.e("teste", "Falhou a chamada do webservice");
            System.out.println(e.toString());
            //e.printStackTrace();
        }       
        
        return;
    }	

}
