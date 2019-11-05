package iuh.doan.coffeeshop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Order implements Serializable {

//    private HashMap<String, Integer> drinks;
    private String ma;
    private String maBan;
    private String createdTime;
    private String note;
    private String status;
    private ArrayList<OrderDetails> drinks;
    private long totalCost;

    public Order() {
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(long totalCost) {
        this.totalCost = totalCost;
    }

    public ArrayList<OrderDetails> getDrinks() {
        return drinks;
    }

    public void setDrinks(ArrayList<OrderDetails> drinks) {
        this.drinks = drinks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return ma.equals(order.ma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ma);
    }

    @Override
    public String toString() {
        return "Order{" +
                "ma='" + ma + '\'' +
                ", maBan='" + maBan + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", note='" + note + '\'' +
                ", status='" + status + '\'' +
                ", totalCost=" + totalCost +
                '}';
    }
}
