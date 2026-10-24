public class RequestQueue {
    private char[] data;
    private int front, rear, size;

    public RequestQueue(int capacity) {
        data = new char[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    public void enqueue(char c) {
        if (size == data.length) {
            System.out.println("Queue is full!");
            return;
        }
        rear = (rear + 1) % data.length;
        data[rear] = c;
        size++;
    }

    public char dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        char c = data[front];
        front = (front + 1) % data.length;
        size--;
        return c;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}

// Author: Syed Ali Haider (247172)

// Author: Syed Ali Haider (247172)
