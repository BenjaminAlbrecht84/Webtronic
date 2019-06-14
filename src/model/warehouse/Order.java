package model.warehouse;

import java.util.Date;

public class Order implements Comparable<Order> {

    private Item item;
    private Date dateOfDelivery;
    private int number;

    public Order(Item item, Date dateOfDelivery, int number) {
        this.item = item;
        this.dateOfDelivery = dateOfDelivery;
        this.number = number;
    }

    public Date getDateOfDelivery() {
        return dateOfDelivery;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public int compareTo(Order o) {
        return dateOfDelivery.compareTo(o.getDateOfDelivery());
    }

}
