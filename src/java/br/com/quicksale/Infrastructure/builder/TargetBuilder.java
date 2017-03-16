package br.com.quicksale.Infrastructure.builder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author jean.souza
 */
class TargetBuilder {

    private static String WS_ENDPOINT;

    public TargetBuilder() {
        readEndPoint();
    }

    private static void readEndPoint() {
        InputStream input = null;
        Properties prop = new Properties();
        try {
            prop.load(TargetBuilder.class.getResourceAsStream("services.properties"));                 
            WS_ENDPOINT = prop.getProperty("uri_service_endpoint");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String generateTarget(String resource) {
        return WS_ENDPOINT+resource;
    }

}
