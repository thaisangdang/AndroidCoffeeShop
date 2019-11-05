package iuh.doan.coffeeshop.model;

import java.io.Serializable;
import java.util.Objects;

public class Table implements Serializable {

    private long soBan;
    private String moTa;
    private String status;

    public Table() {
    }

    public Table(long soBan, String moTa, String status) {
        this.soBan = soBan;
        this.moTa = moTa;
        this.status = status;
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
                ", status='" + status + '\'' +
                '}';
    }
}
