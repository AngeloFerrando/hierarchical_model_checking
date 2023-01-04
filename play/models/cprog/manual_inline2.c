/* ORIGINAL

void main (void)
{

  int y;

  y = foo (1);

  while (y > 0)
  {
    y--;
  }

  y = foo (2);

  while (y > 0)
  {
    y--;
  }
}

int foo (int x)
{
  if (x == 1)
    return 3;
  else
    return 5;
}

*/

void main (void)
{
  int y;
  int foo_param_x;
  int foo_result;

  /* y = foo (1); */
  foo_param_x = 1;
  if (foo_param_x == 1)
  {
    foo_result = 1;  
  }
  else
  {
    foo_result = 2;
  }  
  y = foo_result;

  while (y > 0)
  {
    y--;
  }  

  /* y = foo (2); */
  foo_param_x = 2;
  if (foo_param_x == 1)
  {
    foo_result = 1; 
  }
  else
  {
    foo_result = 2;
  }
  y = foo_result;

  while (y > 0)
  {
    y--;
  }  

}
