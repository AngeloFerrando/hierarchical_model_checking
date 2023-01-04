void main () { 
	int y;
	int x;
      	y = 10;
      	y = y + 1;
      	if (y == 11)
       		y = Procedura(10);
      	else
       		y = y + 1;
	y= Procedura1(8);
}
int Procedura(int k){
	if (k==10){
		k= Procedura1(k);
		k++;
		return k;
	}	
	else
		return 11;
}

int Procedura1(int g){
	int i=0;	
	while(i<3){
		g= g+4;
		i++;
	}
	return g;
}
