package zverevvv.android.moneykeeper;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Vasily on 01.05.2016.
 */
public class Payment {
    private UUID id;
    private String name;
    private Date date;
    private float sum;

    public Payment(){
        this(UUID.randomUUID());
    }

    public Payment(UUID id){
        this.id = id;
        date = new Date();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }
}
