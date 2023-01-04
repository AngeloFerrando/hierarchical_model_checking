void main ()
{
  int x;
  int y;
  int cond;
  
  x = 3;
  y = 3;
  cond = 0;
  
  if (x != 3)
    cond = 1;
  else
    cond = cond;
  
  if (y != 3)
    cond = 1;
  else
    cond = cond;
  
  if (cond != 1)
    {
      while (x > 0)
	x = x - 1;
    }
}
