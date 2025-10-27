package easystore.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Sale {

    private int id;
    private int customerId;
    private LocalDate date;
    private double totalAmount;
    private List<SaleDetail> details = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<SaleDetail> getDetails() {
        return details;
    }

    public void setDetails(List<SaleDetail> details) {
        this.details = details;
    }

    public void addDetail(SaleDetail d) {
        this.details.add(d);
    }
}
