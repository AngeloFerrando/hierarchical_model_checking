/* Generated by CIL v. 1.2.5 */
/* print_CIL_Input is true */

#line 1 "test22.c"
void junk(int x , int y ) 
{ 

  {
#line 2
  return;
}
}
#line 4 "test22.c"
void main(void) 
{ int y ;
  int x ;

  {
#line 10
  y = 10;
#line 11
  x = 2;
#line 12
  junk(x, y);
#line 13
  if (y == 0) {
#line 14
    y = y + 1;
#line 15
    if (y == 1) {
#line 16
      y = y + 1;
#line 17
      while (x > 0) {
#line 18
        x = x - 1;
      }
    }
  }
#line 21
  return;
}
}