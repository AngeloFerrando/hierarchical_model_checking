void fun0 (void)
{
}

void fun1 (void)
{
}

void fun2 (void)
{
}

void fun3 (void)
{
}

void fun4 (void)
{
}

void fun5 (void)
{
}

void fun6 (void)
{
}

void fun8 (void)
{
}

void fun16 (void)
{
}

void notcalled (void)
{
  fun2 ();

  fun3 ();
  fun3 ();

  fun4 ();
  fun4 ();
  fun4 ();

  fun5 ();
  fun5 ();
  fun5 ();
  fun5 ();


  fun8 ();
  fun8 ();
  fun8 ();
  fun8 ();
  fun8 ();
  fun8 ();
  fun8 ();

  fun16 ();
  fun16 ();
  fun16 ();
  fun16 ();
  fun16 ();
  fun16 ();
  fun16 ();
  fun16 ();
  fun16 ();
  fun16 ();
  fun16 ();
  fun16 ();
  fun16 ();
  fun16 ();
  fun16 ();
}

int main (void)
{
  int z;

  z = 0;

  if (z != 0)
    notcalled ();

  fun1 ();

  fun2 ();

  fun3 ();

  fun4 ();

  fun5 ();

  fun8 ();

  fun16 ();

  return 0;
}
