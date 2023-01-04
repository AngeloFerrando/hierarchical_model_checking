void main () {
  int x;
  int y;

  if (x > 0) { 
  y = x; 
  while (x > 0) {
    x -- ;
    y -- ;
  }
  if (y != 0)
    ERROR: goto ERROR;
  }
  //  assert (y == 0);
}
