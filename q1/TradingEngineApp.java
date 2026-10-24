public class TradingEngineApp {
    public static void main(String[] args) {
        System.out.println("===== Trading Engine Started =====");
        long now = 1730000000000L;

        // Superclass reference variables pointing to subclass objects
        TradeEvent e1 = new TradeEvent("SYSTEM-01", now, 100.00);
        TradeEvent e2 = new FIXOrderEvent("FIX-GATEWAY", now + 1000, 250.50, "ORD-1001");
        TradeEvent e3 = new ReutersQuoteEvent("REUTERS-FEED", now + 2000, 250.50, 250.40, 250.65);
        TradeEvent e4 = new FIXOrderEvent("FIX-GATEWAY", now + 3000, 1200.00, "ORD-1002");

        TradeEvent[] events = {e1, e2, e3, e4};
        for (TradeEvent e : events) {
            e.dispatch();   // run-time polymorphism: correct dispatch() is chosen at run time
        }
        System.out.println("===== All Events Dispatched =====");
    }
}

// Author: Syed Ali Haider (247172)
