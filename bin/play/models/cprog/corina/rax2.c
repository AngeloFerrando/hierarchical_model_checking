void main (void)
{
  /* current state of first and second thread */
  int st1;
  int st2;
  /* count variables */
  int count1;
  int count2;
  /* count variables of event */
  int ev1_count;
  int ev2_count;


  /* intially everything is set */
  ev1_count = 0;
  ev2_count = 0;
  count1 = 0;
  count2 = 0;
  
  st1 = 0;
  st2 = 0;


 SCHEDULE: __nd_goto("THREAD1", "THREAD2");

 THREAD1:
  if (st1 == 0) /* INIT */
    {
      if (count1 == ev1_count)
	st1 = 1; /* THEN-PART */
      else
	st1 = 3; /* NEXT-PART */
    }
  else
    {
      if (st1 == 1) /* ASSERT */
	{
	  if (count1 != ev1_count) goto ERROR;
	  else 
	    st1 = 2; /* WAIT */
	}
      else
	{
	  if (st1 == 3)
	    {
	      count1 = ev1_count;
	      st1 = 4;
	    }
	  else
	    {
	      if (st1 == 4)
		{
		  /* signal_event */
		  ev2_count = ev2_count + 1;
		  if (st2 == 2)
		    st2 = 3;
		  st1 = 0;
		}
	    }
	}
    } 
	  
  goto SCHEDULE;

 THREAD2:
  if (st2 == 0) /* INIT */
    {
      if (count2 == ev2_count)
	st2 = 1; /* THEN-PART */
      else
	st2 = 3; /* NEXT-PART */
    }
  else
    {
      if (st2 == 1) /* ASSERT */
	{
	  if (count2 != ev2_count) goto ERROR;
	  else 
	    st1 = 2; /* WAIT */
	}
      else
	{
	  if (st2 == 3)
	    {
	      count2 = ev2_count;
	      st2 = 4;
	    }
	  else
	    {
	      if (st2 == 4)
		{
		  /* signal_event */
		  ev1_count = ev1_count + 1;
		  if (st1 == 2)
		    st1 = 3;
		  st2 = 0;
		}
	    }
	}
    } 
	  
  goto SCHEDULE;
 

 ERROR: goto ERROR;
  
  
}
