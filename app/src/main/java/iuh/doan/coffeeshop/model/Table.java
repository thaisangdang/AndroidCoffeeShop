package iuh.doan.coffeeshop.model;

import java.io.Serializable;
import java.util.Objects;

public class Table implements Serializable {

    private long soBan;
    private String moTa;

    public Table() {
    }

    public Table(long soBan, String moTa) {
        this.soBan = soBan;
        this.moTa = moTa;
    }

    public long getSoBan() {
        return soBan;
    }

    public void setSoBan(long soBan) {
        this.soBan = soBan;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return soBan == table.soBan;
    }

    @Override
    public int hashCode() {
        return Objects.hash(soBan);
    }

    @Override
    public String toString() {
        return "Table{" +
                "soBan=" + soBan +
                ", moTa='" + moTa + '\'' +
                '}';
    }
}
