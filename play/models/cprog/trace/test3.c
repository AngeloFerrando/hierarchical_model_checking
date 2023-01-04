void main (void)
{
  int x;
  int y;

L1:  x = 0;
L2:  y = 0;

L3: if (x < 0) 
  L4: x = x + 1;
  else
    L5: x = x - 1;
}
