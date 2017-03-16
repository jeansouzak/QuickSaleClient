package br.com.quicksale.mb;

import br.com.quicksale.Infrastructure.builder.TargetBuilderFactory;
import br.com.quicksale.beans.Client;
import br.com.quicksale.beans.Order;
import br.com.quicksale.beans.OrderAggregate;
import br.com.quicksale.beans.OrderItem;
import br.com.quicksale.beans.Product;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Named(value = "orderMB")
@SessionScoped
public class OrderMB implements Serializable {

    private List<OrderItem> itens;
    private Order order;
    private Client client;
    private List<Product> products;
    private List<Product> selectedProducts;
    private String cpf;
    private String productDescription;

    public static final String GET_CLIENT_CPF = "/client/findOneByCPF/";
    public static final String GET_PRODUCTS_DESCRIPTION = "/product/findProductsByDescription/";
    public static final String ORDER_SAVE_URI = "/order/";
    public static final String ORDER_ADDITENS_URI = "/order/addItens/";

    @PostConstruct
    public void init() {
        itens = new ArrayList();
        selectedProducts = new ArrayList();
        client = new Client();
    }

    public void removeItem(OrderItem item) {
        OrderItem itemToDelete = null;
        for (OrderItem orderItem : itens) {
            if (orderItem.equals(item)) {
                if (orderItem.getQuantity() > 1) {
                    orderItem.decrementQuantity();
                } else {
                    itemToDelete = orderItem;
                }
            }
        }
        itens.remove(itemToDelete);
    }

    public void searchClientByCPF() {
        javax.ws.rs.client.Client requestClient = ClientBuilder.newClient();
        Response r = requestClient.target(TargetBuilderFactory.buildTarget(GET_CLIENT_CPF + cpf)).
                request(MediaType.APPLICATION_JSON).get();
        if (r.getStatus() != Response.Status.OK.getStatusCode()) {
            String message = r.readEntity(String.class);
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            message, null));
        } else {
            this.client = r.readEntity(Client.class);
            this.order = new Order();
            this.order.setClient(this.client);
            this.order.setDate(new Date());
        }
    }

    public void searchProductsByDescription() {
        String param = "{\"description\":\"" + productDescription + "\"}";

        javax.ws.rs.client.Client requestClient = ClientBuilder.newClient();
        Response r = requestClient
                .target(TargetBuilderFactory.buildTarget(GET_PRODUCTS_DESCRIPTION))
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(param));

        if (r.getStatus() != Response.Status.OK.getStatusCode()) {
            String message = r.readEntity(String.class);
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            message, null));
        } else {
            products = r.readEntity(new GenericType<List<Product>>() {
            });
        }

    }

    public void addProductToList() {
        if (this.order != null) {
            for (Product pr : selectedProducts) {
                if (!this.verifyPreviouslyAddedProduct(pr)) {
                    OrderItem item = new OrderItem();
                    item.setProduct(pr);
                    item.setQuantity(1);
                    item.setOrder(this.order);
                    this.itens.add(item);
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Para adicionar os produtos selecione um cliente válido", null));
        }
    }

    private boolean verifyPreviouslyAddedProduct(Product pr) {
        for (OrderItem item : itens) {
            if (item.getProduct().equals(pr)) {
                item.incrementQuantity();
                return true;
            }
        }
        return false;
    }



    public void saveOrder() throws IOException {
        if (this.order != null && this.itens.size() > 0) {
            OrderAggregate orderAggregate = new OrderAggregate();
            orderAggregate.setOrder(order);
            orderAggregate.setOrderItem(itens);
            javax.ws.rs.client.Client requestClient = ClientBuilder.newClient();
            //Save order
            requestClient
                    .target(TargetBuilderFactory.buildTarget(ORDER_SAVE_URI))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(orderAggregate));
         
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().getExternalContext().redirect("orderForm.xhtml");
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Para salvar um pedido você precisa informar um cliente e produtos válidos", null));
    }

    public void clearOrder() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect("orderForm.xhtml");
    }

    public List<OrderItem> getItens() {
        return itens;
    }

    public void setItens(List<OrderItem> itens) {
        this.itens = itens;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(List<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

}
