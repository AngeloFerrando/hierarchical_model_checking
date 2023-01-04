void main (void) 
{ 
  int y;
  int x;
  x = 3;
  while (x == 3) {
      y = 10;
      if (y == 10) {
	  y = y - 1;
	  if (y == 9) {
	      y = y - 1;
	      if (y >= 8) {
		  y = y - 1;
		  if (y == 7) {
		      y = y - 1;
		  }
	      }
	  }
      }
  }
}
