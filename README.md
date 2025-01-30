## COMP590 A02: Dining Philosophers Problem

## Overview

This project entails a Java solution to the Dining Philosophers problem using threads and synchronization mechanisms. The problem is a classic example of concurrency control in computer science, where multiple philosophers sit around a circular table with forks between them. Each philosopher alternates between thinking and eating, but to eat, they need to pick up the two forks adjacent to them.

## Design Rationale:

Philosophers: Represented as instances of the Philosopher class, each running in its own thread.
Forks: Modeled using the Fork class, which employs ReentrantLock to ensure mutual exclusion when philosophers attempt to pick up forks.
Table: The main class DiningPhilosophers initializes philosophers and forks and starts their threads.
Eating & Thinking: Each philosopher follows a cycle of thinking, attempting to pick up forks, eating if successful, and then releasing their forks.

## Deadlock Prevention:

To prevent deadlock, the last philosopher picks up the right fork before the left fork. This asymmetric strategy prevents circular wait conditions, which is a prerequisite for deadlock.

## Starvation Prevention:

Timeout-Based Fork Acquisition: Philosophers attempt to pick up forks using tryLock() with a timeout, which ensures they do not wait indefinitely if a fork is unavailable.
Fair Resource Allocation: If a philosopher fails to acquire both forks, they release any acquired forks and retry, which allows other philosophers a chance to proceed.

## Race Condition Avoidance:

ReentrantLock ensures that only one philosopher can hold a fork at a time.
Synchronization mechanisms prevent multiple threads from modifying shared resources concurrently.

## Final Details:

Each philosopher eats a maximum of 3 times, preventing infinite execution.
Timestamps are included in log messages for debugging and tracking execution flow.
The Philosopher class uses sleep() to simulate thinking and eating phases.