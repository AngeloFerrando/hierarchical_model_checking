void main ()
{
  int x = 1;
  
  while (x == 1)
    x = f ();
  
  ERROR : 0;
}


int f ()
{
  int x;
  if (x == 1)
    f ();
  else
    return x;
}

     
