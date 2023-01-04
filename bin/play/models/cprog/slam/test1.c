
/*
 * From "Refining Approximations in Software Predicate Abstraction"
 * by T. Ball, B. Cook, S. Das, and S. Rajamani, TACAS'04
 *
 * for SLAM this example requires the new refinement technique intoroduced
 * in the above paper
 *
 * computing the inconsistent invariant makes yasm solve this
 */
void main ()
{
  int x;
  int y;
  int z;
  
  if (x < y)
    {
      if (y < z)
	{
	  if (! (x < z))
	    {
	    ERROR: goto ERROR;
	    }
	}
    }
  
}

