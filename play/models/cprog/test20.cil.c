/* Generated by CIL v. 1.2.5 */
/* print_CIL_Input is true */

#line 6 "test20.c"
int f(void) ;
#line 1 "test20.c"
void main(void) 
{ int x ;

  {
#line 3
  x = 1;
#line 5
  while (x == 1) {
#line 6
    x = f();
  }

#line 9
  return;
}
}
#line 12 "test20.c"
int f(void) 
{ int x ;

  {
#line 15
  if (x == 1) {
#line 16
    f();
  } else {
#line 18
    return (x);
  }
#line 19
  return (0);
}
}
