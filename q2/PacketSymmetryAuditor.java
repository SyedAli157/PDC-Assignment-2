public class PacketSymmetryAuditor {

    static boolean audit(String payload) {
        TransactionStack stack = new TransactionStack(payload.length());
        RequestQueue queue = new RequestQueue(payload.length());

        // push into stack and enqueue into queue at the same time
        for (int i = 0; i < payload.length(); i++) {
            char c = payload.charAt(i);
            stack.push(c);
            queue.enqueue(c);
        }

        boolean symmetric = true;
        int step = 1;
        while (!stack.isEmpty() && !queue.isEmpty()) {
            char fromStack = stack.pop();      // last character
            char fromQueue = queue.dequeue();  // first character
            System.out.println("  Step " + step + ": pop = '" + fromStack
                    + "' , dequeue = '" + fromQueue + "' -> "
                    + (fromStack == fromQueue ? "MATCH" : "MISMATCH"));
            if (fromStack != fromQueue) {
                symmetric = false;
                break;
            }
            step++;
        }
        return symmetric;
    }

    public static void main(String[] args) {
        String[] packets = {"racecar", "A1B2B1A", "NETWORK"};
        for (String p : packets) {
            System.out.println("Auditing packet payload: " + p);
            boolean ok = audit(p);
            System.out.println("  RESULT: Packet \"" + p + "\" is "
                    + (ok ? "SYMMETRIC (palindrome) - VERIFIED" : "NOT symmetric - REJECTED"));
            System.out.println();
        }
    }
}

// Author: Syed Ali Haider (247172)
