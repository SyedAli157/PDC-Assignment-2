public class ReutersQuoteEvent extends TradeEvent {
    private double bidPrice;
    private double askPrice;

    public ReutersQuoteEvent(String senderId, long timestamp, double basePrice,
                             double bidPrice, double askPrice) {
        super(senderId, timestamp, basePrice);
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    @Override
    public void dispatch() {
        double spread = askPrice - bidPrice;
        double spreadPercent = (spread / basePrice) * 100;
        System.out.println("[REUTERS QUOTE] Sender: " + senderId + " | Time: " + timestamp);
        System.out.printf("   Bid: %.2f | Ask: %.2f | Spread: %.2f (%.3f%% of base price)%n",
                bidPrice, askPrice, spread, spreadPercent);
    }
}

// Author: Syed Ali Haider (247172)

// Author: Syed Ali Haider (247172)
