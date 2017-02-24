package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gargoylesoftware.htmlunit.JavaScriptPage;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import play.*;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;
import play.mvc.*;

import views.html.*;

import java.io.*;
import java.util.Date;

public class Application extends Controller {

    public static Result index() {
        connect();
        return ok(index.render("Your new application is ready."));
    }

    public static JsonNode connect() {
        String url = "http://localhost:9898/csvTest";//"https://api.tigoenergy.com/api/data?cmd=data&sysid=25337&start=2017-02-01_06-00-00&interval=5&end=2017-02-01_20-00-00&agg=sum";
        WSRequestHolder holder = WS.url(url);
        holder.setAuth("Enlight", "Enlight1");
        WSResponse response = holder.get().get(60000);
        String filePath = "C:\\git\\play23\\Datos";
        if (response.getStatus() != 200) {
            Logger.error("Error" + response.getStatusText());
        } else {
            try{
                if(!writeFile(response.getBodyAsStream(), filePath)){
                    //error
                } else {

                }
            }catch(Exception e){
                Logger.error(e.getMessage());
            }
        }
        return response.asJson();
    }

    public synchronized static boolean writeFile(InputStream input, String filePath){
        return writeFile(input, new File(filePath));
    }

    public synchronized static boolean writeFile(InputStream input, File file){
        try(OutputStream os =  new FileOutputStream(file); InputStream is = input;) {
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                os.write(bytes, 0, read);
            }
            //System.out.println("Done!");
            return true;
        } catch (IOException e) {
            Logger.error("Error al descargar el recibo "+file.getName(), e);
        }
        return false;
    }

}
