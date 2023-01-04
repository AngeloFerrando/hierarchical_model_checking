package edu;

public class WeirdAnt extends Ant
{
  public int age = 5;
  public int id = 7;
  
  public WeirdAnt (int a, int i)
  {
    super (i, a);
    this.id = 2 * id;
    age = id * super.id;
  }
  
  public String toString ()
  {
    return age + " " + super.age + " " + id + " " + super.id;
  }
  
  public void setID (int i) 
  {
    this.id = i;
  }
  
  public static void main (String[] args)
  {
    Ant babs = new Ant (-2, -3);
    WeirdAnt ali = new WeirdAnt (11, 13);
    System.out.println (ali);
    System.out.println (babs);
    ali.setID (ali.id);
    System.out.println (ali.id + " " + babs.id + " " + ali.id);
    babs.setID (babs.id);
    System.out.println (ali.id + " " + babs.id + " " + babs.id);    
  }
}


class Ant
{
  public int age = 2;
  public static int id = 3;
  
  public Ant (int a, int i)
  {
    age = a;
    setID (i);
  }
  
  public void setID (int i) 
  {
    id = i;
  }
  
  public String toString ()
  {
    return age + " " + id;
  }
}


  
