void main () {
   int x;
   x = 3;
   if ( x != 3) {x = x;} 
   else {
   while (x > 0)
     x = x -1;}
}
//start with  x>0 
//then becomes x>0; x<=1
//then becomes x>0; x<=1; x<=2
//then becomes x>0; x<=1; x<=2; x<=3

//There is a problem with initialization
