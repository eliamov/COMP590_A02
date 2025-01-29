import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {
    public static void main(String[] args) {
        int numberOfPhilosophers = 5;
        Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
        Fork[] forks = new Fork[numberOfPhilosophers];

        for (int i = 0; i < numberOfPhilosophers; i++) {
            forks[i] = new Fork(i);
        }

        for (int i = 0; i < numberOfPhilosophers; i++) {
            // To prevent deadlock, ensure the last philosopher picks up right fork first
            Fork leftFork = forks[i];
            Fork rightFork = forks[(i + 1) % numberOfPhilosophers];

            if (i == numberOfPhilosophers - 1) {
                philosophers[i] = new Philosopher(i, rightFork, leftFork); // Reverse order
            } else {
                philosophers[i] = new Philosopher(i, leftFork, rightFork);
            }

            new Thread(philosophers[i], "Philosopher " + (i + 1)).start();
        }
    }
}

class Philosopher implements Runnable {
    private int id;
    private Fork leftFork;
    private Fork rightFork;

    public Philosopher(int id, Fork leftFork, Fork rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    private void think() {
        System.out.println("Philosopher " + id + " is thinking.");
        sleep(1000);
    }

    private void eat() {
        System.out.println("Philosopher " + id + " is eating.");
        sleep(1000);
    }

    private void sleep(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        while (true) {
            think();

            // Try to pick up the forks
            leftFork.pickUp();
            rightFork.pickUp();

            eat();

            // Put down the forks
            rightFork.putDown();
            leftFork.putDown();
        }
    }
}

class Fork {
    private final ReentrantLock lock;
    private final int id;

    public Fork(int id) {
        this.id = id;
        this.lock = new ReentrantLock();
    }

    public void pickUp() {
        lock.lock();
        System.out.println("Fork " + id + " picked up.");
    }

    public void putDown() {
        System.out.println("Fork " + id + " put down.");
        lock.unlock();
    }
}
