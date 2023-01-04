package edu.toronto.cs.tp.cvcl.test;

import edu.toronto.cs.tp.cvcl.*;


public class Example4{
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
	
	Expr i = vc.varExpr("i1", vc.realType());
	Expr j = vc.varExpr("i2", vc.realType());
	Expr p = vc.varExpr("p", vc.realType());
	Expr q = vc.varExpr("q", vc.realType());
	Expr r = vc.varExpr("r", vc.realType());
	Expr a = vc.varExpr("arb_addr", vc.realType());
	Expr N = vc.varExpr("N", vc.realType());

	Expr M = vc.varExpr("M", vc.arrayType(vc.realType(), vc.realType()));
	
	Expr M2 = vc.writeExpr(M, vc.plusExpr(q, i), vc.readExpr(M, vc.plusExpr(r, i)));

	Expr M1 = vc.writeExpr(M, vc.plusExpr(p, j), vc.readExpr(M, vc.plusExpr(r, j)));

	Expr e = vc.eqExpr(vc.readExpr(vc.writeExpr(M2, vc.plusExpr(p, j), vc.readExpr(M2, vc.plusExpr(r, j))), a),
		      vc.readExpr(vc.writeExpr(M1, vc.plusExpr(q, i), vc.readExpr(M1, vc.plusExpr(r, i))), a));
	
	Expr one = vc.ratExpr(1,1);
	Expr zero = vc.ratExpr(0,1);
	
	Expr qmp = vc.minusExpr(q, p);
	Expr qmr = vc.minusExpr(q, r);

	CVectorExpr hyp = new CVectorExpr(0);
	hyp.add(vc.ltExpr(i,j));
	//hyp.add(vc.orExpr(vc.geExpr(qmp, N), vc.leExpr(qmp, zero)));
	//hyp.add(vc.orExpr(vc.geExpr(qmr, N), vc.leExpr(qmr, zero)));
	Expr test = vc.impliesExpr(vc.andExpr(hyp), e);
	Expr query;

	System.out.println("Checking verification condition:");
	vc.printExpr(test);
	System.out.println();
	
	boolean result = vc.query(test,false);
	if(result){
	    System.out.println("Valid");
	}
	else{
	    CVectorExpr conditions = new CVectorExpr(0);
	    CVectorExpr assertions = new CVectorExpr(0);
	    int index, index2, req;
	    CVectorExpr leaves = new CVectorExpr(0);
	    
	    vc.getCounterExample(assertions);
	    
	    int count = 0;
	    
	    System.out.println("Invalid Under Conditions: ");
	    for(index =0; index<assertions.size(); index++){
		if(assertions.get(index) == (test.notExpr())){
		    for(; index<assertions.size()-1; index++){
			assertions.get(index).assign(assertions.get(index+1));
		    }
		    count = count++;
		}
	    }
	    for(index = 0; index <assertions.size()-count; index++){
		vc.printExpr(assertions.get(index));
	    }
	    
	    System.out.println("Try assertions one by one");
	    for( index =0; index <assertions.size()-count; index++){
		e = assertions.get(index);
		
		//Check Condition for Eligibility
		if(e.isNot()){
		    System.out.println("Condition ineligible: negation");
		    vc.printExpr(e);
		    System.out.println();
		    continue;
		}
		if(e.isEq()){
		    req = 2;
		}
		else req = 1;
		leaves.clear();
		ExampleMethods.findLeaves(e, leaves);
		for(index2=0; index2<leaves.size(); index2++){
		    if(!leaves.get(index2).isVar()
		       ||a.equal(leaves.get(index2), a)
		       ||i.equal(leaves.get(index2), i)
		       ||j.equal(leaves.get(index2), j))
			continue;
		    req--;
		}
		if (req>0){
		    System.out.println("Condition ineligible: not enough non-loop variables");
		    vc.printExpr(e);
		    System.out.println();
		    continue;
		}
		
		System.out.println("Condition selected: ");
		vc.printExpr(e);
		System.out.println();
		
		conditions.add(vc.notExpr(e));
		System.out.println("Trying verification condition with hypothesis: ");
		vc.printExpr(vc.andExpr(conditions));
		System.out.println();
		vc.popto(0);
		query = vc.impliesExpr(vc.andExpr(conditions), test);
		result = vc.query(query,false);
		if(result){
		    System.out.println("Result Valid");
		    break;
		}
		else{
		    assertions.clear();
		    vc.getCounterExample(assertions);
		    count = 0;
	    
		    System.out.println("Invalid Under Conditions:");
		    for(index =0; index<assertions.size(); index++){
			if(assertions.get(index) == (test.notExpr())){
			    for(; index<assertions.size()-1; index++){
				assertions.get(index).assign(assertions.get(index+1));
			    }
			    count = count++;
			}
		    }
		    for(index = 0; index <assertions.size()-count; index++){
			vc.printExpr(assertions.get(index));
		    }
		    System.out.println();
		    index = -1;
		}

	    }
	    System.out.println("Attempting to remove loop variables");
	    CVectorExpr newConditions = new CVectorExpr(0);
	    CVectorExpr newPlus = new CVectorExpr(0);
		
	    boolean foundi, foundj, negi, negj;
	    Expr minusone = vc.ratExpr(-1, 1);
	    for(index = 0; index < conditions.size(); index++){
		if(conditions.get(index).get(0).isEq()){
		    e = vc.simplify(vc.minusExpr(conditions.get(index).get(0).get(0), conditions.get(index).get(0).get(1)));
		    if(ExampleMethods.hasij(e,i,j)){
			if(e.getKind() == JavaCVC.PLUS){
			    newPlus.clear();
			    newPlus.add(e.get(0));
			    foundi = foundj = negi = negj = false;
			    for(index2 = 1; index2<e.arity(); index2++){
				Expr term = e.get(index2);
				if(term == i && !foundi) foundi = true;
				else if (term == j && !foundj){
				    foundj = true;
				    negj = true;}
				else if(term.getKind() == JavaCVC.MULT && term.get(0) == minusone && term.get(1) == i && !foundi){
				    foundi = true;
				    negi =  true;
				}
				else if(term.getKind() == JavaCVC.MULT && term.get(0) == minusone && term.get(1) == j && !foundj) 
				    foundj=true;
				else newPlus.add(term);
			    }
		       
			    //Completely lost from here stop, (Xin Ma)
			}
			
		    }
		}
		
	    }
	}
	
	
    }
}
