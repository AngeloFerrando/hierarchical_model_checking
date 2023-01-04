package edu.toronto.cs.tp.cvcl.test;

import edu.toronto.cs.tp.cvcl.*;


public class Example3 {
    static{
        try {
            System.loadLibrary("JavaCVC");
        }
        catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load. See the chapter on Dynamic Linking Problems in the SWIG Java documentation for help.\n" +e);
            System.exit(1);
        }
    }
    public static void main(String argv[]){
	CLFlags flags = ValidityChecker.createFlags();
	flags.setFlag("dagify-exprs",false);
	ValidityChecker vc = new ValidityChecker(flags);

	Expr i = vc.varExpr("i", vc.realType());
	Expr j = vc.varExpr("j", vc.realType());
	Expr k = vc.varExpr("k", vc.realType());
	
	Expr one = vc.ratExpr(1,1);
	
	System.out.println("i: " + i.getIndex());
	
	Expr test = ExampleMethods.createTestFormula(vc, i, j, vc.minusExpr(i, one), vc.minusExpr(j, k));
	
	System.out.println("Trying test: ");
	vc.printExpr(test);
	System.out.println();
	
	boolean result = vc.query(test, false);
	if(result){
	    System.out.println("Test Valid");
	}
	else{
	    Expr condition;
	    CVectorExpr assertions = new CVectorExpr(0);
	    int index;
	    
	    vc.getCounterExample(assertions);
	    
	    System.out.println("Test Invalid Under Coinditions: ");
	    for(index = 0; index<assertions.size(); index++){
		vc.printExpr(assertions.get(index));
	    }
	    
	    System.out.println("Try assertion one by one");
	    for(index = 0; index < assertions.size(); index++){
		condition = vc.notExpr(assertions.get(index));
		System.out.println("Trying test under condition: ");
		vc.printExpr(condition);
		System.out.println();
		vc.popto(0);
		result = vc.query(vc.impliesExpr(condition, test),false);
		if(result)
		    System.out.println("Result Valid");
		else
		    System.out.println("Result Invalid");
	    }
	}
	
    }
}
