package br.com.comunication.mvendas;

import java.io.IOException;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

//09-07 16:10:27.923: I/System.out(538): 
//org.xmlpull.v1.XmlPullParserException: expected: START_TAG {http://schemas.xmlsoap.org/soap/envelope/}Envelope (position:START_TAG <script>@1:8 in java.io.InputStreamReader@4055d408) 

public class SoapSerEnv extends SoapSerializationEnvelope {

    public SoapSerEnv(int version) {
        super(version);
    }

    public void write(XmlSerializer writer)
        throws IOException
    {
        writer.setPrefix("i", xsi);
        writer.setPrefix("d", xsd);
        writer.setPrefix("c", enc);
        writer.setPrefix("soap", env); // era writer.setPrefix("v", env)
        
        System.out.println("SoapSerializationEnvelope modificado...");
        
        writer.startTag(env, "Envelope");
        writer.startTag(env, "Header");
        writeHeader(writer);
        writer.endTag(env, "Header");
        writer.startTag(env, "Body");
        writeBody(writer);
        writer.endTag(env, "Body");
        writer.endTag(env, "Envelope");
        
        System.out.println(writer);
        
    }
}