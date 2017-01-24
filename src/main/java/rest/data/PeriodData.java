package rest.data;

import core.entity.RegistrationPeriod;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.Date;

@JsonPropertyOrder({"id", "countdown", "beginning", "end", "price"})
public class PeriodData {

    public Integer id;

    public Integer countdown;

    public Date beginning;

    public Date end;

    public BigDecimal price;

    public PeriodData() {
    }

    public PeriodData(RegistrationPeriod period) {
        beginning = period.getBeginning();
        end = period.getEnd();
        price = period.getPrice();
    }
}
