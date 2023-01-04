void main () { 
	int y;
	int x;
      	y = 10;
      	y = y + 1;
      	if (y == 11)
       		y = Procedura(y);
      	else{
       		y = y + 1;
		y= Procedura(8);
	}	
	y= Procedura1(8);
}
int Procedura(int y){
	if (y==10){
		y = Procedura1(y);
		return y;
	}
	else
		return 11;
}
int Procedura1(int z){
	if(z>5)
		return 35;
	else
		return 53;
}

