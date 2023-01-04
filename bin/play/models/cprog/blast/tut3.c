void main () {
  int x;
  int y;
  int __BLAST_NONDET;
  x = 0;
  y = 0;
  while (__BLAST_NONDET == 1) {
    x ++ ;
    y ++ ;
  }
  while (x > 0) {
    x -- ;
    y -- ;
  }
  if (y != 0)
    ERROR: goto ERROR;
  //  assert (y == 0);
}
