package edu.toronto.cs.tp.cvcl.test;

import edu.toronto.cs.tp.cvcl.*;


public class Example1 {
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
	flags.setFlag("dagify-exprs", false);
	ValidityChecker vc = new ValidityChecker(flags);
	Expr x = vc.varExpr("x", vc.realType());
	
	Expr y = vc.varExpr("y", vc.realType());

	//***************************************
	
	



	//***************************************






	System.out.println("Check p OR ~p");
	
	Expr p = vc.varExpr("p", vc.boolType());
	Expr e = vc.orExpr(p, vc.notExpr(p));


	ExampleMethods.check(vc, e);

	System.out.println("check x = y -> f(x) = f(y)");
	
	
	Type real2real = vc.funType(vc.realType(), vc.realType());
	Op f = vc.createOp("f", real2real);
	
	System.out.println("Printing f");
	System.out.println(f.toString());
	System.out.println("Test assignment\nTest Op::operator=()");
	Op z1 = f;
	Op z2 = new Op(f.getOp(x));
	System.out.println("z2 = new Op(f.getOp(x)) => " + z2 +"\n");
	z2 = f;
	System.out.println("z2 = f => " + z2 +"\n");

	Expr fx = vc.funExpr(f, x);
	Expr fy = vc.funExpr(f, y);
	
	e = vc.impliesExpr(vc.eqExpr(x, y), vc.eqExpr(fx, fy));
	ExampleMethods.check(vc, e);
	
	System.out.println("Check f(x) = f(y) => x = y");
	
	e = vc.impliesExpr(vc.eqExpr(fx, fy), vc.eqExpr(x, y));
	ExampleMethods.check(vc, e);

	System.out.println("Get counter-example");
	CVectorExpr assertions = new CVectorExpr(0);
	System.out.println("Scope level: " + vc.scopeLevel());
	System.out.println("Counter-example: ");
	vc.getCounterExample(assertions);
	for (int i = 0; i<assertions.size(); i++)
	{
		vc.printExpr(assertions.get(i));
	}
	
	System.out.println("End of counter-example\n");

	System.out.println("Reset to initial scope");

	System.out.println("Resetting...");
	vc.popto(1);
	System.out.println("Scope level: " + vc.scopeLevel() + "\n");
	
	Expr w = vc.varExpr("w", vc.realType());
	Expr z = vc.varExpr("z", vc.realType());
	
	System.out.println("Push Scope");
	vc.push();
	
	ExampleMethods.newAssertion(vc, vc.eqExpr(w, x));
	ExampleMethods.newAssertion(vc, vc.eqExpr(x, y));
	ExampleMethods.newAssertion(vc, vc.eqExpr(y, z));
	ExampleMethods.newAssertion(vc, vc.eqExpr(fx, fy));
	ExampleMethods.newAssertion(vc, vc.eqExpr(x, vc.ratExpr(1, 1)));
	ExampleMethods.newAssertion(vc, vc.eqExpr(z, vc.ratExpr(2, 1)));
	
	System.out.println("\nsimplify(w) = ");
	vc.printExpr(vc.simplify(w));
	System.out.println();

	assertions.clear();
	System.out.println("Inconsistent?: " + vc.inconsistent(assertions));
	
	System.out.println("Assumptions used: ");
	for(int i = 0; i<assertions.size(); i++){
	    vc.printExpr(assertions.get(i));
	}

	System.out.println("\nPop Scope\n\n");
	vc.pop();

	System.out.println("simplify(w) = ");
	vc.printExpr(vc.simplify(w));
	System.out.println();
	assertions.clear();
	System.out.println("Inconsistent?:" + vc.inconsistent(assertions));
    }
}
