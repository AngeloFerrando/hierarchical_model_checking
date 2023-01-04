int foo (int a)
{
  int b;
  
  if (a < b)
    return b;
  else
    ABORT: goto ABORT;
}

void main ()
{
  int x;
  int y;
  int z;
  
  if (x < y)
    {
      z = foo (y);

      /* at this point 
       * z == foo (y)
       * z == b
       * b > y
       * and therefore z > y
       * !(x < z) == (x >= z)
       * z > y > x implies (z > x) contradicting !(z > x)
       */

      L1: if (!(x < z))
	ERROR: goto ERROR;
    } 
}
