package br.com.quicksale.beans;

import java.util.List;

/**
 *
 * @author jean.souza
 */
public class OrderAggregate {
    
    private Order order;
    
    private List<OrderItem> orderItem;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }
    
    
    
    
    
}
