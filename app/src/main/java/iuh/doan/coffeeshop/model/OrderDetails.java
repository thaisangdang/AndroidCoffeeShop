package iuh.doan.coffeeshop.model;

import java.io.Serializable;
import java.util.Objects;

public class OrderDetails implements Serializable {
    private String drinkId;
    private int num;

    public OrderDetails(String drinkId, int num) {
        this.drinkId = drinkId;
        this.num = num;
    }

    public String getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(String drinkId) {
        this.drinkId = drinkId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetails that = (OrderDetails) o;
        return Objects.equals(drinkId, that.drinkId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(drinkId);
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "drinkId='" + drinkId + '\'' +
                ", num=" + num +
                '}';
    }
}
