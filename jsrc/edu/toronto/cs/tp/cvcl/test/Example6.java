package edu.toronto.cs.tp.cvcl.test;

import edu.toronto.cs.tp.cvcl.*;


import java.io.*;

public class Example6{
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
	if(argv.length != 1) 
	    return;
	else {
	    int N = Integer.parseInt(argv[0]);
	    
	    CLFlags flags = ValidityChecker.createFlags();
	    flags.setFlag("proofs", true);
	    flags.setFlag("dagify-exprs", false);
	    ValidityChecker vc = new ValidityChecker(flags);
	    
	    int i;
	    CVectorExpr a = new CVectorExpr(0);
	    CVectorExpr b = new CVectorExpr(0);
	    CVectorExpr sum1 = new CVectorExpr(0);
	    CVectorExpr sum2 = new CVectorExpr(0);
	    
	    for(i = 0 ; i<N; i++){
		a.add(vc.varExpr("a" + String.valueOf(i), vc.boolType()));
		b.add(vc.varExpr("b" + String.valueOf(i), vc.boolType()));
	    }
	    
	    ExampleMethods.add(vc, a, b, sum1);
	    ExampleMethods.add(vc, a, b, sum2);
	    
	    Expr q = ExampleMethods.CVectorExprq(vc, sum1, sum2);
	    
	    ExampleMethods.check(vc, q);
	    
	    Proof p = vc.getProof();
	    System.out.println(p);
	    
	    
	}
    }
}
