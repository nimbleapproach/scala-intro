package oystercard.java;

import org.junit.Assert;
import org.junit.Test;

import static oystercard.java.TubeStation.*;

public class OysterCardTest {

    @Test
    public void testStartWithInitialBalanceOfZero() {
        Assert.assertEquals(OysterCard.Empty.balance, Amount.Zero);
    }
    @Test
    public void touchCardChargesMaxFare() {
        JourneyCalculator calc = new DefaultJourneyCalculator();

        Amount actual = OysterCard.Empty
                .loadCard(Amount.inPounds(10))
                .touchCard(Hammersmith, calc)
                .balance;
        Assert.assertEquals(Amount.inPence(680), actual);
    }

    @Test
    public void testMainScenario() {
        final JourneyCalculator calc = new DefaultJourneyCalculator();

        final Amount actual = OysterCard.Empty
                .update(UserEvent.loadCard(Amount.inPounds(30)), calc)
                .update(UserEvent.touchCard(Holborn), calc)
                .update(UserEvent.touchCard(EarlsCourt), calc)
                .update(UserEvent.takeBus, calc)
                .update(UserEvent.touchCard(EarlsCourt), calc)
                .update(UserEvent.touchCard(Hammersmith), calc)
                .balance;
        Assert.assertEquals(Amount.inPence(2270), actual);
    }
}
