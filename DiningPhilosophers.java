import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class DiningPhilosophers {
    public static void main(String[] args) {
        int numberOfPhilosophers = 5;
        int maxEats = 3; // Termination condition: each philosopher eats 3 times
        Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
        Fork[] forks = new Fork[numberOfPhilosophers];

        for (int i = 0; i < numberOfPhilosophers; i++) {
            forks[i] = new Fork(i);
        }

        for (int i = 0; i < numberOfPhilosophers; i++) {
            Fork leftFork = forks[i];
            Fork rightFork = forks[(i + 1) % numberOfPhilosophers];
            
            if (i == numberOfPhilosophers - 1) {
                philosophers[i] = new Philosopher(i, rightFork, leftFork, maxEats); // Reverse order for last philosopher
            } else {
                philosophers[i] = new Philosopher(i, leftFork, rightFork, maxEats);
            }

            new Thread(philosophers[i], "Philosopher " + (i + 1)).start();
        }
    }
}

class Philosopher implements Runnable {
    private int id;
    private Fork leftFork;
    private Fork rightFork;
    private int maxEats;
    private int eatCount = 0;

    public Philosopher(int id, Fork leftFork, Fork rightFork, int maxEats) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.maxEats = maxEats;
    }

    private void think() {
        System.out.println(System.currentTimeMillis() + " - Philosopher " + id + " is thinking.");
        sleep(1000);
    }

    private void eat() {
        System.out.println(System.currentTimeMillis() + " - Philosopher " + id + " is eating. (" + (eatCount + 1) + "/" + maxEats + ")");
        sleep(1000);
        eatCount++;
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
        while (eatCount < maxEats) {
            think();

            try {
                if (leftFork.pickUp(id, 500)) { // Try picking up left fork with timeout
                    try {
                        if (rightFork.pickUp(id, 500)) { // Try picking up right fork with timeout
                            try {
                                eat();
                            } finally {
                                rightFork.putDown(id);
                            }
                        }
                    } finally {
                        leftFork.putDown(id);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(System.currentTimeMillis() + " - Philosopher " + id + " is done eating.");
    }
}

class Fork {
    private final ReentrantLock lock;
    private final int id;

    public Fork(int id) {
        this.id = id;
        this.lock = new ReentrantLock();
    }

    public boolean pickUp(int philosopherId, long timeout) throws InterruptedException {
        if (lock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
            System.out.println(System.currentTimeMillis() + " - Philosopher " + philosopherId + " picked up Fork " + id);
            return true;
        }
        return false;
    }

    public void putDown(int philosopherId) {
        lock.unlock();
        System.out.println(System.currentTimeMillis() + " - Philosopher " + philosopherId + " put down Fork " + id);
    }
}
