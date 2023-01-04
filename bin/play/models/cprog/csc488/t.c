
int main(void) {
  //int a[10];

/*
  for (int i=1; i<10; i++) 
      a[i] = a[i-1];
  for (int i=1; i<10; i++) 
      a[i] = a[i-11];
*/
  int i;
  int j;
  //if (0 <= i and i<10 and 0<=j and j <10 and i!=j and i==j-1) {
    for (i=0; i<10; i++) 
    for (j=0; j<10; j++) 
      if (i==j-1 && i!=j) {
	P1:
      }
  //}


 // 0 <= i and i<10 and 0<=j and j <10 and i!=j and i=j-11
    for (i=0; i<10; i++) 
    for (j=0; j<10; j++) 
      if (i==j-11 && i!=j) {
	P2:
      }

} 
