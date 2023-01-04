package edu.toronto.cs.tp.cvcl.test;

import edu.toronto.cs.tp.cvcl.*;


public class Example2 {
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
	
	Type realxreal = vc.tupleType(vc.realType(), vc.realType());
	
	Type realxreal2real = vc.funType(realxreal, vc.realType());
	Op g = vc.createOp("g", realxreal2real);
	
	Expr gxy = vc.funExpr(g, x, y);
	Expr gyx = vc.funExpr(g, y, x);
	
	Expr e = vc.impliesExpr(vc.eqExpr(x, y), vc.eqExpr(gxy, gyx));
	ExampleMethods.check(vc, e);
	
	CVectorType v = new CVectorType(0);
	v.add(vc.realType());
	v.add(vc.realType());
	
	realxreal = vc.tupleType(v);
	
	realxreal2real = vc.funType(realxreal, vc.realType());
	Op h = vc.createOp("h", realxreal2real);
	
	Expr hxy = vc.funExpr(h, x, y);
	Expr hyx = vc.funExpr(h, y, x);
	
	e = vc.impliesExpr(vc.eqExpr(x, y), vc.eqExpr(hxy, hyx));
	ExampleMethods.check(vc, e);

    }
}
