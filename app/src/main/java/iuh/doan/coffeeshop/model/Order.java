package iuh.doan.coffeeshop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Order implements Serializable {

    private String ma;
    private String maBan;
    private String createdTime;
    private long totalCost;
    private ArrayList<String> drinks; // lưu mã nước/món
    private String status;

    public Order() {
    }

    public Order(String ma, String maBan, String createdTime, long totalCost, ArrayList<String> drinks, String status) {
        this.ma = ma;
        this.maBan = maBan;
        this.createdTime = createdTime;
        this.totalCost = totalCost;
        this.drinks = drinks;
        this.status = status;
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

    public ArrayList<String> getDrinks() {
        return drinks;
    }

    public void setDrinks(ArrayList<String> drinks) {
        this.drinks = drinks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
                ", totalCost=" + totalCost +
                ", drinks=" + drinks +
                ", status='" + status + '\'' +
                '}';
    }

}