package iuh.doan.coffeeshop.model;

import java.io.Serializable;
import java.util.Objects;

public class Drink implements Serializable {

    private String ma;
    private String ten;
    private long gia;

    public Drink() {
    }

    public Drink(String ma, String ten, long gia) {
        this.ma = ma;
        this.ten = ten;
        this.gia = gia;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public long getGia() {
        return gia;
    }

    public void setGia(long gia) {
        this.gia = gia;
    }

    @Override
    public String toString() {
        return "Drink{" +
                "ma='" + ma + '\'' +
                ", ten='" + ten + '\'' +
                ", gia=" + gia +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drink drink = (Drink) o;
        return ma.equals(drink.ma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ma);
    }
}
