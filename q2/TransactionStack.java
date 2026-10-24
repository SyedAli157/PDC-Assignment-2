public class TransactionStack {
    private char[] data;
    private int top;

    public TransactionStack(int capacity) {
        data = new char[capacity];
        top = -1;
    }

    public void push(char c) {
        if (top == data.length - 1) {
            System.out.println("Stack is full!");
            return;
        }
        data[++top] = c;
    }

    public char pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return data[top--];
    }

    public char peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return data[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }
}
