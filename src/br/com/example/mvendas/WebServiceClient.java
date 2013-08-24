package br.com.example.mvendas;

import java.util.LinkedList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServiceClient {
	
	private static final String NAMESPACE = "http://webservice.henriquelacerda.com.br/";
    private static final String METHOD_NAME_BUSCAR_CLIENTES = "buscarCliente";
    private static final String SOAP_ACTION = "";
    private static String URL = "http://www.webservice.henriquelacerda.com.br:8080/webService?wsdl";
 
    public WebServiceClient() { }
 
    public List buscarClientes() {    	
    	
        List clientes = new LinkedList();
        // Aqui criamos a requisicao
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME_BUSCAR_CLIENTES);
        
    	//PropertyInfo propInfo = new PropertyInfo();
    	//propInfo.name = "id_empresa";
    	//propInfo.type = PropertyInfo.STRING_CLASS;
    	//request.addProperty(propInfo, _configuracao.getCodigoEmpresa());    	
        
        // Aqui criamos o envelope que será enviado
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	//envelope.implicitTypes = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);       
        
        try {
            // Realizamos a chamada do webservice
            androidHttpTransport.call(SOAP_ACTION, envelope);
            // Recebemos o retorno, convertendo em SoapObject
            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
            int propsNum = resultsRequestSOAP.getPropertyCount();
            // Iteração para "ler" os objetos recebidos
            for (int i = 0; i < propsNum; i++) {
                SoapObject element = (SoapObject) resultsRequestSOAP.getProperty(i);
                clientes.add(element.getProperty("cliente").toString());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return clientes;
    }	

}
