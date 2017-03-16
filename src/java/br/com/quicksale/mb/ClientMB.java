package br.com.quicksale.mb;

import br.com.quicksale.Infrastructure.builder.TargetBuilderFactory;
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

@Named(value = "clientMB")
@RequestScoped
public class ClientMB {

    private List<br.com.quicksale.beans.Client> clients;
    private br.com.quicksale.beans.Client client;
    public static final String CLIENT_LIST_URI = "/client/";
    public static final String CLIENT_UPDATE_URI = "/client/";
    public static final String CLIENT_CREATE_URI = "/client/";
    public static final String CLIENT_REMOVE_URI = "/client/";

    public ClientMB() {

    }

    @PostConstruct
    public void init() {
        this.updateClientList();
        client = new br.com.quicksale.beans.Client();
    }

    public void remove(br.com.quicksale.beans.Client client) {
        Client requestClient = ClientBuilder.newClient();        
        Response r = requestClient.target(TargetBuilderFactory.buildTarget(
                CLIENT_REMOVE_URI + client.getId())).
                request(MediaType.APPLICATION_JSON).
                delete();
        if (r.getStatus() != Response.Status.OK.getStatusCode()) {
            String message = r.readEntity(String.class);            
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            message, null));
        } else {
            this.updateClientList();
        }
    }

    public void updateClientList() {
        Client requestClient = ClientBuilder.newClient();
        Response resp = requestClient.target(TargetBuilderFactory.buildTarget(CLIENT_LIST_URI)).
                request(MediaType.APPLICATION_JSON).get();
        clients = resp.readEntity(new GenericType<List<br.com.quicksale.beans.Client>>() {
        });
    }

    public String clientForm(br.com.quicksale.beans.Client client) {
        this.client = client;
        return "clientForm";

    }

    public String saveClient() {
        Client requestClient = ClientBuilder.newClient();
        if (this.client.getId() != null) {
            requestClient
                    .target(TargetBuilderFactory.buildTarget(CLIENT_UPDATE_URI))
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.json(this.client));
        } else {
            requestClient
                    .target(TargetBuilderFactory.buildTarget(CLIENT_CREATE_URI))
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(this.client));
        }
        this.updateClientList();
        return "clients";
    }

    public List<br.com.quicksale.beans.Client> getClients() {
        return clients;
    }

    public void setClients(List<br.com.quicksale.beans.Client> clients) {
        this.clients = clients;
    }

    public br.com.quicksale.beans.Client getClient() {
        return client;
    }

    public void setClient(br.com.quicksale.beans.Client client) {
        this.client = client;
    }

}
