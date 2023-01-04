#include <stdio.h>

#include "util.h"
#include "cudd.h"
#include "cuddInt.h"



int main (int argc, char** argv)
{
  DdManager * manager;
  
  printf ("Test CUDD\n");

  manager = Cudd_Init ((int)4, 0, CUDD_UNIQUE_SLOTS, CUDD_CACHE_SLOTS, 0);
  printf ("Allocated manager\n");
  printf ("manager->perm: %i\n", manager->perm);

  printf ("Test CUDD is done\n");
}
