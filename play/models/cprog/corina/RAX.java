/* This file is part of the Java Pathfinder (JPF) distribution from
 * NASA Ames Research Center. See file LICENSE for usage terms.
 * (C) 1999,2003 NASA Ames Research Center
 */


/* RAX example */

public class RAX {
  public static void main (String[] args) {
    Event      new_event1 = new Event();
    Event      new_event2 = new Event();

    FirstTask  task1 = new FirstTask(new_event1, new_event2);
    SecondTask task2 = new SecondTask(new_event1, new_event2);

    task1.start();
    task2.start();
  }
}

class Event {
  int count = 0;

  public synchronized void signal_event () {
      count = (count + 1);//% 3;
    notifyAll();
  }

  public synchronized void wait_for_event () {
    try {
      wait();
    } catch (InterruptedException e) {
    }
  }
}

class FirstTask extends java.lang.Thread {
  Event event1;
  Event event2;
  int   count = 0;

  public FirstTask (Event e1, Event e2) {
    this.event1 = e1;
    this.event2 = e2;
  }

  public void run () {
    count = 0;

    while (true) {
      System.out.println("1");
      if (count == event1.count) {
        assert(count == event1.count);
        event1.wait_for_event();
      }
      count = event1.count;
      event2.signal_event();
    }
  }
}

class SecondTask extends java.lang.Thread {
  Event event1;
  Event event2;
  int   count = 0;

  public SecondTask (Event e1, Event e2) {
    this.event1 = e1;
    this.event2 = e2;
  }

  public void run () {
    count = 0;

    while (true) {
      System.out.println("2");
      event1.signal_event();
      if (count == event2.count) {
        assert(count == event2.count);
        event2.wait_for_event();
      }
      count = event2.count;
    }
  }
}

