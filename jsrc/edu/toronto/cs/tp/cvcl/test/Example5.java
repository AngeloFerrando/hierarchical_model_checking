package edu.toronto.cs.tp.cvcl.test;

import edu.toronto.cs.tp.cvcl.*;


public class Example5{
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
	ValidityChecker vc1 = new ValidityChecker();
	ValidityChecker vc2 = new ValidityChecker();
	
	Type real1 = vc1.realType();
	
	Expr x1 = vc1.varExpr("x", real1);
	Expr y1 = vc1.boundVarExpr("y", "0", real1);
	
	System.out.println("vcl variables: " + x1 + ", " + y1);
	
	//the following line is very important, see the use of importExpr when want Expr that is created froma nother VC
	Expr x2 = vc2.varExpr("x", vc2.importType(real1));
	Expr y2 = vc2.boundVarExpr("y", "0", vc2.realType());
	
	System.out.println("vc2 variables: " + x2 + ", " + y2);
	System.out.println("vars imported to vc2 from vc1: " + vc2.importExpr(x1) + ", " + vc2.importExpr(y1));

	Expr t1 = vc1.trueExpr();
	Expr and1 = vc1.andExpr(t1, vc1.falseExpr());
	Op f1 = vc1.createOp("f", vc1.funType(real1, real1));
	Expr fx1 = vc1.funExpr(f1, x1);
	Expr f5_1 = vc1.funExpr(f1, vc1.ratExpr(5,1));
	
	Type rt1 = vc1.recordType("foo", real1, "bar", real1);
	Expr r1 = vc1.recordExpr("foo", fx1, "bar", f5_1);
	Expr r1_eq = vc1.eqExpr(r1, vc1.recUpdateExpr(r1, "foo", f5_1));

	Type art1 = vc1.arrayType(real1, rt1);
	Expr ar1 = vc1.varExpr("ar", art1);
	Expr ar_eq1 = vc1.eqExpr(vc1.writeExpr(ar1, x1, r1), ar1);
	
	Expr query1 = vc1.eqExpr(vc1.recSelectExpr(vc1.readExpr(ar1, x1), "foo"), vc1.recSelectExpr(r1, "bar"));
	
	//Start the tracing
	//Debug debugger = new Debug();//won't work
	//JavaCVC.getDebugger().traceFlag("expr").assign(true);

	ExampleMethods.newAssertion(vc1, r1_eq);
	ExampleMethods.newAssertion(vc1, ar_eq1);
	ExampleMethods.check(vc1, query1);

	System.out.println("*** VC #2:");
	ExampleMethods.newAssertion(vc2, vc2.importExpr(r1_eq));
	ExampleMethods.newAssertion(vc2, vc2.importExpr(ar_eq1));
	ExampleMethods.check(vc2, vc2.importExpr(query1)); //suppose to abort here.
	
    }
}
