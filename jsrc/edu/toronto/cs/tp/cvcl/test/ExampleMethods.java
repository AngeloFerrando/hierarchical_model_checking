package edu.toronto.cs.tp.cvcl.test;

import edu.toronto.cs.tp.cvcl.*;


public class ExampleMethods{
    public static void check (ValidityChecker vc, Expr e)
    {
        System.out.println("Query: ");
        vc.printExpr(e);
        boolean result = vc.query(e, false);
        if(result==false)
	    System.out.println("Invalid\n");
	else
	    System.out.println("Valid\n");
    }
    public static void newAssertion(ValidityChecker vc, Expr e){
	System.out.println("Assert: ");
	vc.printExpr(e);
	vc.assertFormula(e);
    }
    public static Expr ltLex(ValidityChecker vc, Expr i1, Expr i2, Expr j1, Expr j2){
	Expr res = vc.ltExpr(i1, j1);
	return vc.orExpr(res, vc.andExpr(vc.eqExpr(i1, j1), vc.ltExpr(i2, j2)));
    }
    public static Expr createTestFormula(ValidityChecker vc, Expr i1, Expr i2, Expr r1, Expr r2){
	Expr lt1 = ltLex(vc, r1, r2, i1, i2);
	Expr lt2 = ltLex(vc, i2, i1, r2, r1);
	return vc.andExpr(lt1, lt2);
    }
    public static void findLeaves(Expr e, CVectorExpr l){
   	int ar = e.arity();
    	if(ar>0){
    	    for(int i=0; i<ar; i++)
    		findLeaves(e.get(i),l);
   	    return;
	} 
        l.add(e);
    }
    public static boolean hasij(Expr e, Expr i, Expr j){
	int ar = e.arity();
	if(ar>0){
	    for(int k = 0; k<ar; k++)
		if( hasij(e.get(k), i, j) ) 
		    return true;
	    return false;
	}
	if(e==i||e==j) return true;
	return false;
    }
    public static void add(ValidityChecker vc, CVectorExpr a, CVectorExpr b, CVectorExpr sum){
	int i, N = (int)a.size();
	Expr c = vc.falseExpr();
	for(i = 0; i<N; i++){
	    sum.add(halfadder(vc, a.get(i), b.get(i), c));
	    c = carry(vc, a.get(i), b.get(i), c);
	}
    }
    public static Expr halfadder(ValidityChecker vc, Expr a, Expr b, Expr c)
    {
	return vc.notExpr(vc.iffExpr(vc.notExpr(vc.iffExpr(a,b)),c));
    }
    public static Expr carry(ValidityChecker vc, Expr a, Expr b, Expr c){
	return vc.orExpr(a, vc.orExpr(b, c));
    }
    public static Expr CVectorExprq(ValidityChecker vc, CVectorExpr a, CVectorExpr b){
	int i, N =(int) a.size();
	Expr result = vc.trueExpr();
	
	for( i = 0; i<N; i++){
	    result = result.andExpr(a.get(i).iffExpr(b.get(i)));
	}
	return result;
    }
			  
				 
}
