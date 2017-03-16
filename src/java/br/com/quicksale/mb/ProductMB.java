package br.com.quicksale.mb;

import br.com.quicksale.Infrastructure.builder.TargetBuilderFactory;
import br.com.quicksale.beans.Product;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Named(value = "productMB")
@RequestScoped
public class ProductMB {

    private List<Product> products;
    private Product product;
    public static final String PRODUCT_LIST_URI = "/product/";
    public static final String PRODUCT_UPDATE_URI = "/product/";
    public static final String PRODUCT_CREATE_URI = "/product/";
    public static final String PRODUCT_REMOVE_URI = "/product/";

    public ProductMB() {

    }

    @PostConstruct
    public void init() {
        this.updateProductList();
        product = new Product();
    }

    public void remove(Product product) {
        Client requestClient = ClientBuilder.newClient();
        Response r = requestClient.target(TargetBuilderFactory.buildTarget(PRODUCT_REMOVE_URI + product.getId())).
                request(MediaType.APPLICATION_JSON).
                delete();
        if (r.getStatus() != Response.Status.OK.getStatusCode()) {
            String message = r.readEntity(String.class);
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            message, null));
        } else {
            this.updateProductList();
        }
    }

    public void updateProductList() {
        Client requestClient = ClientBuilder.newClient();
        Response resp = requestClient.target(TargetBuilderFactory.buildTarget(PRODUCT_LIST_URI)).
                request(MediaType.APPLICATION_JSON).get();
        products = resp.readEntity(new GenericType<List<Product>>() {
        });
    }

    public String productForm(Product product) {
        this.product = product;
        return "productForm";

    }

    public String saveProduct() {
        Client requestClient = ClientBuilder.newClient();
        if (this.product.getId() != null) {
            requestClient
                    .target(TargetBuilderFactory.buildTarget(PRODUCT_UPDATE_URI))
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.json(this.product));
        } else {
            requestClient
                    .target(TargetBuilderFactory.buildTarget(PRODUCT_CREATE_URI))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(this.product));
        }
        this.updateProductList();
        return "products";
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
    

}
