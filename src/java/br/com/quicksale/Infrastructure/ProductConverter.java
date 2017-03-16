package br.com.quicksale.Infrastructure;

import br.com.quicksale.Infrastructure.builder.TargetBuilderFactory;
import br.com.quicksale.beans.Product;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 *
 * @author jean.souza
 */
@FacesConverter(value = "productConverter", forClass = Product.class)
public class ProductConverter implements Converter {
    
    public static final String PRODUCT_FINDONE_URI = "/product/findOneByID/";
    
    @Override
    public Object getAsObject(FacesContext context,
            UIComponent component, String value) {
        Product p = null;
        Client requestClient = ClientBuilder.newClient();        
        Response r = requestClient.target(TargetBuilderFactory.buildTarget(
                PRODUCT_FINDONE_URI + Long.parseLong(value))).
                request(MediaType.APPLICATION_JSON).
                get();
        if (r.getStatus() == Response.Status.OK.getStatusCode()) {
           p = r.readEntity(Product.class);
        } 
        
        return p;
    }

    @Override
    public String getAsString(FacesContext context,
            UIComponent component, Object value) {
        return String.valueOf(((Product) value).getId());        
    }
}
