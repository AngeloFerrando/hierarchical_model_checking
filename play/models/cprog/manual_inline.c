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
  int foo_return_index;
  int foo_result;

  /* y = foo (1); */
  {
  foo_param_x = 1;
  foo_return_index = 1;
  goto FOO;
  }
  {
FOO_RETURN_1:
  y = foo_result;
  }

  while (y > 0)
  {
    y--;
  }  

  /* y = foo (2); */
  foo_param_x = 2;
  foo_return_index = 2;
  goto FOO;
FOO_RETURN_2:
  y = foo_result;

  while (y > 0)
  {
    y--;
  }  
  goto END;

FOO:
  if (foo_param_x == 1)
  {
    foo_result = 1;
    goto FOO_END;
  }
  else
  {
    foo_result = 2;
    goto FOO_END;
  }

FOO_END:
  if (foo_return_index == 1)
    goto FOO_RETURN_1;
  else
    goto FOO_RETURN_2;  

END:
  goto END;  
}
