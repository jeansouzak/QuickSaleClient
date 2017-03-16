package br.com.quicksale.mb;

import br.com.quicksale.Infrastructure.builder.TargetBuilderFactory;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import br.com.quicksale.beans.Client;
import br.com.quicksale.beans.Order;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Named(value = "clientOrderMB")
@RequestScoped
public class ClientOrderMB {

    private Client client;
    private List<Order> orders;

    public static final String CLIENT_ORDERS_URI = "/order/findClientOrders/";

    public ClientOrderMB() {

    }

    @PostConstruct
    public void init() {
        orders = new ArrayList<Order>();
    }

    public String showClientOrders(Client client) {
        this.client = client;
        javax.ws.rs.client.Client requestClient = ClientBuilder.newClient();
        Response resp = requestClient.target(TargetBuilderFactory.
                buildTarget(CLIENT_ORDERS_URI + client.getId())).
                request(MediaType.APPLICATION_JSON).get();
        System.err.println(resp.getEntity().getClass());
        orders = resp.readEntity(new GenericType<List<Order>>() {
        });        

        return "orders";
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    
}
