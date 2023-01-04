%module JavaCVC
%{
#include "expr_op.h"
#include "vc.h"
#include "exception.h"
#include "command_line_flags.h"
#include "debug.h"
#include "expr.h"
#include "type.h"
#include "statistics.h"
#include "theorem.h"
#include "proof.h"
#include "rational.h"

%}
//%javamethodmodifiers "public synchronized";
%include typemaps.i

%include "carrays.i"
%array_class(int, CIntArray);
%include std_map.i
%include std_pair.i
%include std_string.i
%include std_vector.i

%rename(assign) CVCL::Expr::operator=;
%rename(get) CVCL::Expr::operator[];
//%rename(assign) CVCL::DebugFlag::operator=;
%rename(equals) CVCL::Expr::operator==;
namespace std{
	%template(CVectorExpr)	vector<CVCL::Expr>;
	%template(CVectorType)  vector<CVCL::Type>;
}

namespace CVCL 
{

  class Context;

  class Statistics;
  //class Debug;

  // class DebugFlag {
  //     public:
  //     DebugFlag& operator=(bool x) { *d_flag=(x!=false); return *this; }
  //     %extend{
  // 	~DebugFlag(){
  // 	    delete self;
  // 	}
  //     }
  // };



  // class Debug{
  //     public:
  //       DebugFlag traceFlag(const std::string& name)
  //       { return DebugFlag(d_traceFlags[name]); }
  //     %extend{
  // 	  ~Debug(){
  // 	      delete self;
  // 	  }
  //     }
  // };

  // extern Debug debugger;

  typedef enum {
    NULL_KIND = 0,
    // Generic LISP kinds for representing raw parsed expressions
    RAW_LIST, //!< May have any number of children >= 0
    //! Identifier is (ID (STRING_EXPR "name"))
    ID,
    // Leaf exprs
    STRING_EXPR,
    RATIONAL_EXPR,
    TRUE,
    FALSE,
    // Types
    BOOLEAN,
    TUPLETYPE,
    ARROW,
    // The "type" of any expression type (as in BOOLEAN : TYPE).
    TYPE,
    // Declaration of new (uninterpreted) types: T1, T2, ... : TYPE
    // (TYPEDECL T1 T2 ...)
    TYPEDECL,
    // Declaration of a defined type T : TYPE = type === (TYPEDEF T type)
    TYPEDEF,


    // Equality
    EQ,
    NEQ,

    // Propositional connectives
    NOT,
    AND,
    OR,
    XOR,
    IFF,
    IMPLIES,

    // Propositional relations (for circuit propagation)
    AND_R,
    IFF_R,
    ITE_R,

    // (ITE c e1 e2) == IF c THEN e1 ELSE e2 ENDIF, the internal
    // representation of the conditional.  Parser produces (IF ...).
    ITE, 

    // Quantifiers
    FORALL,
    EXISTS,

    // Application of an uninterpreted function.  Exprs of this kind are
    // special exprs with children; the function name can be accessed
    // with getOp(), and getKids() are the arguments.  NOTE, that the
    // operator is NOT the first child.
    UFUNC,

    // Top-level Commands
    ASSERT,
    QUERY,
    DBG,
    TRACE,
    UNTRACE,
    OPTION,
    HELP,
    TRANSFORM,
    PRINT,
    CALL,
    ECHO,
    INCLUDE,
    DUMP_PROOF,
    DUMP_ASSUMPTIONS,
    DUMP_SIG,
    DUMP_TCC,
    DUMP_TCC_ASSUMPTIONS,
    DUMP_TCC_PROOF,
    DUMP_CLOSURE,
    DUMP_CLOSURE_PROOF,
    WHERE,
    COUNTEREXAMPLE,
    POP,
    POP_SCOPE,
    POPTO,
    PUSH,
    CONTEXT,
    FORGET,
    GET_TYPE,
    CHECK_TYPE,
    GET_CHILD,
    SUBSTITUTE,

    // Kinds used mostly in the parser

    TCC,
    // Variable declaration (VARDECL v1 v2 ... v_n type).  A variable
    // can be an ID or a BOUNDVAR.
    VARDECL,
    // A list of variable declarations (VARDECLS (VARDECL ...) (VARDECL ...) ...)
    VARDECLS,

    // Bound variables have a "printable name", the one the user typed
    // in, and a uniqueID used to distinguish it from other bound
    // variables, which is effectively the alpha-renaming: 

    // Op(BOUND_VAR (BOUND_ID "user_name" "uniqueID")).  Note that
    // BOUND_VAR is an operator (Expr without children), just as UFUNC
    // and UCONST.

    // The uniqueID normally is just a number, so one can print a bound
    // variable X as X_17.

    // NOTE that in the parsed expressions like LET x: T = e IN foo(x),
    // the second instance of 'x' will be an ID, and *not* a BOUNDVAR.
    // The parser does not know how to resolve bound variables, and it
    // has to be resolved later.
    BOUND_VAR,
    BOUND_ID,

    // Updator "e1 WITH <bunch of stuff> := e2" is represented as
    // (UPDATE e1 (UPDATE_SELECT <bunch of stuff>) e2), where <bunch
    // of stuff> is the list of accessors: 
    // (READ idx)
    // ID (what's that for?)
    // (REC_SELECT ID)
    // and (TUPLE_SELECT num).
    UPDATE,
    UPDATE_SELECT,
    // Record type [# f1 : t1, f2 : t2 ... #] is represented as 
    // (RECORD_TYPE (FIELD_DECL f1 t1) (FIELD_DECL f2 t2) ... )
    RECORD_TYPE,
    FIELD_DECL,
    // (# f1=e1, f2=e2, ...#) == (REC_LITERAL (REC_ENTRY f1 e1) ...)
    REC_LITERAL,
    REC_ENTRY,
    REC_SELECT,
    REC_UPDATE,

    // (e1, e2, ...) == (TUPLE e1 e2 ...)
    TUPLE,
    TUPLE_SELECT,
    TUPLE_UPDATE,

    ARRAY,
    READ,
    WRITE,
    // Array literal [ [type] e ]; creates a constant array holding 'e'
    // in all elements: (CONST_ARRAY type e)
    CONST_ARRAY,
    SUBRANGE,
    // Enumerated type (SCALARTYPE v1 v2 ...)
    SCALARTYPE,
    // Predicate subtype: the argument is the predicate (lambda-expression)
    SUBTYPE,
    // Datatype is Expr(DATATYPE, Constructors), where Constructors is a
    // vector of Expr(CONSTRUCTOR, id [ , arg ]), where 'id' is an ID,
    // and 'arg' a VARDECL node (list of variable declarations with
    // types).  If 'arg' is present, the constructor has arguments
    // corresponding to the declared variables.
    DATATYPE,
    CONSTRUCTOR,
    // Expression e WITH accessor := e2 is transformed by the command
    // processor into (DATATYPE_UPDATE e accessor e2), where e is the
    // original datatype value C(a1, ..., an) (here C is the
    // constructor), and "accessor" is the name of one of the arguments
    // a_i of C.
    DATATYPE_UPDATE,
    // Statement IF c1 THEN e1 ELSIF c2 THEN e2 ... ELSE e_n ENDIF is
    // represented as (IF (IFTHEN c1 e1) (IFTHEN c2 e2) ... (ELSE e_n))
    IF,
    IFTHEN,
    ELSE,

    // LET x1: t1 = e1, x2: t2 = e2, ... IN e
    // Parser builds:
    // (LET (LETDECLS (LETDECL x1 t1 e1) (LETDECL x2 t2 e2) ... ) e)
    // where each x_i is a BOUNDVAR.
    // After processing, it is rebuilt to have (LETDECL var def); the
    // type is set as the attribute to var.
    LET,
    LETDECLS,
    LETDECL,
    // Lambda-abstraction LAMBDA (<vars>) : e  === (LAMBDA <vars> e)
    LAMBDA,
    // Application of a lambda-term
    APPLY,
    // Symbolic simulation operator
    SIMULATE,

    // Constant declaration x : type = e  === (CONSTDEF x type e)
    CONSTDEF,
    // Uninterpreted constants (variables) x1, x2, ... , x_n : type
    // (CONST (VARLIST x1 x2 ... x_n) type)
    // Uninterpreted functions are declared as constants of functional type.

    // After processing, uninterpreted functions and constants
    // (a.k.a. variables) are represented as Op(UFUNC, (ID "name")) and
    // Op(UCONST, (ID "name")) with the appropriate type attribute.
    CONST,  
    VARLIST,
    UCONST,
    // User function definition f(args) : type = e === (FUNCDEF args type e)
    // Here 'args' are bound var declarations
    FUNCDEF,

    // Arithmetic types and operators
    REAL,
    INT,

    UMINUS,
    PLUS,
    MINUS,
    MULT,
    DIVIDE,
    INTDIV,
    MOD,
    LT,
    LE,
    GT,
    GE,
    IS_INTEGER,
    NEGINF,
    POSINF,
    DARK_SHADOW,
    GRAY_SHADOW,

    //Floor theory operators
    FLOOR,

    // Bitvector types and operators
    BITVECTOR,
    CONCAT,
    BIT_AND,
    BIT_NEG,
    EXTRACT,
    INT_TO_BV,
    BV_TO_INT,
    BOOL_EXTRACT,

    // Kinds for proof terms
    PF_APPLY,
    PF_HOLE,

    // Kind for Extension to Non-linear Arithmetic
    POW,

    // Mlss
    EMPTY, // {}
    UNION, // +
    INTER, // * 
    DIFF,  
    SINGLETON,
    IN,
    INCS,
    INCIN,

    //! Must always be the last kind
    LAST_KIND
  } Kind;




  /*****************************************************************************/
  /*!
   *\class ValidityChecker
   *\brief Generic API for a validity checker
   *\ingroup VC_API
   *\anchor vc
   *
   * Author: Clark Barrett
   *
   * Created: Tue Nov 26 18:24:25 2002
   *
   * All terms and formulas are represented as expressions using the Expr class.
   * The notion of a context is also important.  A context is a "background" set
   * of formulas which are assumed to be true or false.  Formulas can be added to
   * the context explicitly, using assertFormula, or they may be added as part of
   * processing a query command.  At any time, the current set of formulas making
   * up the context can be retrieved using getAssertions.
   */
  /*****************************************************************************/
  %nodefault;
  class Proof{
  public:
    std::string toString() const {
      std::ostringstream ss;
      ss<<(*this);
      return ss.str();
    }
    //      %extend{
    // 	 ~Proof(){
    // 	     delete self;
    // 	 }
    //      }
  };
  class Rational{
  public:
    Rational getNumerator() const;
    Rational getDenominator() const;
    
    // Equivalent to (getDenominator() == 1), but possibly more efficient
    bool isInteger() const;
    // Convert to int; defined only on integer values
    int getInt() const;
    Rational();
    // Copy constructor
    Rational(const Rational &n);
    Rational(int n, int d = 1);
  };
  class CLFlags {
  public:		
    size_t countFlags(const std::string& name);
    size_t countFlags(const std::string& name,
		      std::vector<std::string>& names);
    void setFlag(const std::string& name, bool b);
    void setFlag(const std::string& name, int i);
    //void setFlag(const std::string& name, const std::string& s);
    void setFlag(const std::string& name, const char* s);
    void setFlag(const std::string& name, 
		 const std::pair<std::string,bool>& p);
    void setFlag(const std::string& name, 
		 const std::vector<std::pair<std::string,bool> >& sv);

    %extend{
      ~CLFlags()
	{
	  delete self;
	}
    }
      

  };
  typedef long long unsigned ExprIndex;
  //inline bool operator==(const Expr& e1, const Expr& e2) {
  // Comparing pointers (equal expressions are always shared)
  //  return e1.d_expr == e2.d_expr;
  //}
  class Expr;
  class Op;
  class ExprManager{
  public:
    Expr rebuildExpr(const Expr& e) { return Expr(rebuild(e)); }
  };
  
  %typemap(javacode) Expr %{ 
    public boolean equals (Object o)
      {
	if (o == null || !(o instanceof Expr)) return false;
	return equal (this, (Expr)o);
      }
    
    public int hashCode ()
      {
	return computeHashCode (this);
      }
    %}

  class Expr {
  public:
    ExprIndex getIndex() const;
    Expr(): d_expr(NULL) {}
    Expr(const Expr& e);
    const Rational & getRational();
    int arity() const;
    int getKind() const;
   
    // Create a new Expr with given ExprManager, operator and children.
    // If the children belong to another ExprManager, convert them to
    // use the given one.
    Expr(ExprManager *em, const Op& op);
    Expr(ExprManager *em, const Op& op, const Expr& child);
    Expr(ExprManager *em, const Op& op, const Expr& child0, const Expr& child1);
    Expr(ExprManager *em, const Op& op, const Expr& child0, const Expr& child1, 
	 const Expr& child2);
    Expr(ExprManager *em, const Op& op, const std::vector<Expr>& children);
  
    // Similar constructors, but from kinds rather than from an Op
    Expr(ExprManager *em, int kind);
    Expr(ExprManager *em, int kind, const Expr& child);
    Expr(ExprManager *em, int kind, const Expr& child0, const Expr& child1);
    Expr(ExprManager *em, int kind, const Expr& child0, const Expr& child1,
	 const Expr& child2);
    Expr(ExprManager *em, int kind, const std::vector<Expr>& children);



  
    bool hasOp() const;
    const Expr& getOp() const;
    bool isFalse() const { return getKind() == FALSE; }
    bool isTrue() const { return getKind() == TRUE; }
    bool isBoolConst() const { return isFalse() || isTrue(); }
    bool isVar() const;
    bool isString() const;
    bool isClosure() const;
    bool isQuantifier() const;
    bool isLambda() const;
    //! Expr is an application of a LAMBDA-term to arguments
    bool isApply() const;
    bool isRecord() const;
    bool isRecordAccess() const;
    bool isTupleAccess() const;
    size_t isGeneric() const;
    bool isGeneric(size_t idx) const;

    bool isEq() const { return getKind() == EQ; }
    bool isNot() const { return getKind() == NOT; }
    bool isAnd() const { return getKind() == AND; }
    bool isOr() const { return getKind() == OR; }
    bool isITE() const { return getKind() == ITE; }
    bool isIff() const { return getKind() == IFF; }
    bool isImpl() const { return getKind() == IMPLIES; }

    bool isForall() const { return getKind() == FORALL; }
    bool isExists() const { return getKind() == EXISTS; }
    // Arith testers
    bool isRational() const { return getKind() == RATIONAL_EXPR; }
    
    bool isNull() const;

    Expr eqExpr(const Expr& right) const;
    Expr notExpr() const;
    Expr negate() const; // avoid double-negatives
    Expr andExpr(const Expr& right) const;
    static Expr andExpr(std::vector <Expr>& children);
    Expr orExpr(const Expr& right) const;
    static Expr orExpr(std::vector <Expr>& children);
    Expr iteExpr(const Expr& thenpart, const Expr& elsepart) const;
    Expr iffExpr(const Expr& right) const;
    Expr impExpr(const Expr& right) const;

    Expr& operator=(const Expr& e);
    //   inline bool operator==(const Expr& e1, const Expr& e2) {
    //     return e1.d_expr == e2.d_expr;
    //   };

    const Expr& operator[](int i) const;
    std::string toString() const;
    %extend{
      bool equal(const Expr& e1, const Expr& e){
	return CVCL::operator==(e1, e);
      }
      size_t computeHashCode (const Expr& e)
	{
	  return e.getEM ()->hash (e);
	}
      //       ~Expr ()
      // 	  {
      // 	      delete self;
      // 	  };
    }
  };



  class Op{
  public:
    std::string toString() const;
    Op(const Op& op);
    Op(ExprManager *em, int kind);
    int getKind() const { return d_kind; }

    Op(int kind);
  
    bool hasExpr() const;
  
    // Return the expr associated with this operator if applicable.  It
    // is an error to call this when Op has no associated Expr.
    const Expr& getExpr() const {
      DebugAssert(hasExpr(),
		  "CVCL::Op::getExpr(" + toString()
		  + "): no Expr in this Op");
      return d_expr;
    }
  
    %extend{
      Op getOp(const Expr& e){
	return CVCL::getOp(e);
      }
      //         ~Op()
      // 	    {
      // 		delete self;
      // 	    };
    }
  };


  class Type {
  public:
    Type ();
    std::string toString() const { return getExpr().toString(); }
    const Expr& getExpr() const { return d_expr; }
    int arity() const { return d_expr.arity(); }
    //     %extend{
    //       ~Type ()
    // 	  {
    // 	      delete self;
    // 	  };
    //    }
  };
  class Statistics{
  public:
    %extend{
      ~Statistics(){
	delete self;
      };
    }
  };
  class Theorem3{
  public:
    bool isNull() const { return d_thm.isNull(); }
    
    // True if theorem is of the form t=t' or phi iff phi'
    bool isRewrite() const { return d_thm.isRewrite(); }
    bool isAxiom() const { return d_thm.isAxiom(); }
    
    // Return the theorem value as an Expr
    const Expr& getExpr() const { return d_thm.getExpr(); }
    const Expr& getLHS() const { return d_thm.getLHS(); }
    const Expr& getRHS() const { return d_thm.getRHS(); }
    
    // Return the proof of the theorem.  If running without proofs,
    // return the Null proof.
    const Proof& getProof() const { return d_thm.getProof(); }
    // Return the lowest scope level at which this theorem is valid.
    // Value -1 means no information is available.
    // int getScope() const;
    
    // Test if we are running in a proof production mode and with assumptions
    bool withProof() const { return d_thm.withProof(); }
    bool withAssumptions() const { return d_thm.withAssumptions(); }
    
    // Printing
    std::string toString() const;
    
    // For debugging
    void printx() const { d_thm.printx(); }
    void print() const { d_thm.print(); }
    
    //! Check if the theorem is a literal
    bool isLiteral() const { return d_thm.isLiteral(); }
    
    int getScope() const { return d_thm.getScope(); }
    %extend{
      ~Theorem3(){
	delete self;
      };
    }
  };
  class Context {
  public:
    %extend{
      ~Context(){
	delete self;
      }
    }
  };
  class ValidityChecker {

  public:
    
    static CLFlags createFlags ();
    static ValidityChecker* create ();
    static ValidityChecker* create (CLFlags flags);
    
    Type boolType(); 
    Type realType();
    Type intType();
    Type subrangeType(const Expr& l, const Expr& r);
    //! Creates a subtype defined by the given predicate (a LAMBDA-term)
    /*!
     * \param pred is a term (LAMBDA (x: T): p(x)), and the resulting
     * type is a subtype of T whose elements x are those satisfying the
     * predicate p(x).
     */
    Type subtypeType(const Expr& pred);
    // Tuple types
    //! 2-element tuple
    Type tupleType(const Type& type0, const Type& type1);
    //! 3-element tuple
    Type tupleType(const Type& type0, const Type& type1,
		   const Type& type2);
    //! n-element tuple (from a vector of types)
    Type tupleType(const std::vector<Type>& types);

    // Record types
    //! 1-element record
    Type recordType(const std::string& field, const Type& type);
    //! 2-element record
    /*! Fields will be sorted automatically */
    Type recordType(const std::string& field0, const Type& type0,
		    const std::string& field1, const Type& type1);
    //! 3-element record
    /*! Fields will be sorted automatically */
    Type recordType(const std::string& field0, const Type& type0,
		    const std::string& field1, const Type& type1,
		    const std::string& field2, const Type& type2);
    //! n-element record (fields and types must be of the same length)
    /*! Fields will be sorted automatically */
    Type recordType(const std::vector<std::string>& fields,
		    const std::vector<Type>& types);

    //! Create an array type (ARRAY typeIndex OF typeData)
    Type arrayType(const Type& typeIndex, const Type& typeData);

    //! Create a function type ([typeDom -> typeRan])
    Type funType(const Type& typeDom, const Type& typeRan);

    //! Create named user-defined uninterpreted type
    Type createType(const std::string& typeName);
    //! Create named user-defined interpreted type (type abbreviation)
    Type createType(const std::string& typeName, const Type& def);
    //! Lookup a user-defined (uninterpreted) type by name
    Type lookupType(const std::string& typeName);

    ExprManager* getEM();

    Expr varExpr(const std::string& name, const Type& type);
    
    Expr boundVarExpr(const std::string& name,
		      const std::string& uid,
		      const Type& type);

    //! Get the variable associated with a name, and its type 
    /*!
      \param name is the variable name
      \param type is where the type value is returned

      \return a variable by the name. If there is no such Expr, a NULL \
      Expr is returned.
    */
    Expr lookupVar(const std::string& name, Type* type);

    //! Get the type of the Expr.
    Type getType(const Expr& e);
    //! Get the largest supertype of the Expr.
    Type getBaseType(const Expr& e);

  //! Create a list Expr 
  /*! Intermediate representation for DP-specific expressions.
   *  Normally, the first element of the list is a string Expr
   *  representing an operator, and the rest of the list are the
   *  arguments.  For example, 
   *
   *  kids.push_back(vc->stringExpr("PLUS"));
   *  kids.push_back(x); // x and y are previously created Exprs
   *  kids.push_back(y);
   *  Expr lst = vc->listExpr(kids);
   *
   * Or, alternatively (using its overloaded version):
   *
   * Expr lst = vc->listExpr("PLUS", x, y);
   *
   * or
   *
   * vector<Expr> summands;
   * summands.push_back(x); summands.push_back(y); ...
   * Expr lst = vc->listExpr("PLUS", summands);
   */
  Expr listExpr(const std::vector<Expr>& kids);
  //! Overloaded version of listExpr with one argument
  Expr listExpr(const Expr& e1);
  //! Overloaded version of listExpr with two arguments
  Expr listExpr(const Expr& e1, const Expr& e2);
  //! Overloaded version of listExpr with three arguments
  Expr listExpr(const Expr& e1, const Expr& e2, const Expr& e3);
  //! Overloaded version of listExpr with string operator and many arguments
  Expr listExpr(const std::string& op,
			const std::vector<Expr>& kids);
  //! Overloaded version of listExpr with string operator and one argument
  Expr listExpr(const std::string& op, const Expr& e1);
  //! Overloaded version of listExpr with string operator and two arguments
  Expr listExpr(const std::string& op, const Expr& e1, const Expr& e2);
  //! Overloaded version of listExpr with string operator and three arguments     Expr listExpr(const std::string& op, const Expr& e1, const Expr& e2, const Expr& e3);



    //! Prints e to the standard output
    void printExpr(const Expr& e);
    //! Parse an expression using a Theory-specific parser
    Expr parseExpr(const Expr& e);

    //! Import the Expr from another instance of ValidityChecker
    /*! When expressions need to be passed among several instances of
     *  ValidityChecker, they need to be explicitly imported into the
     *  corresponding instance using this method.  The return result is
     *  an identical expression that belongs to the current instance of
     *  ValidityChecker, and can be safely used as part of more complex
     *  expressions from the same instance.
     */
    Expr importExpr(const Expr& e);
    //! Import the Type from another instance of ValidityChecker
    /*! \sa getType() */
    Type importType(const Type& t);

    /*@}*/ // End of General Expr Methods

    /***************************************************************************/
    /*!
     *\name Core expression methods
     * Methods for manipulating core expressions
     *
     * Except for equality and ite, the children provided as arguments must be of
     * type Boolean.
     *@{
     */
    /***************************************************************************/

    //! Return TRUE Expr
    Expr trueExpr();
    //! Return FALSE Expr
    Expr falseExpr();
    //! Create negation
    Expr notExpr(const Expr& child);
    //! Create 2-element conjunction 
    Expr andExpr(const Expr& left, const Expr& right);
    //! Create n-element conjunction
    Expr andExpr(std::vector<Expr>& children);
    //! Create 2-element disjunction
    Expr orExpr(const Expr& left, const Expr& right);
    //! Create n-element disjunction
    Expr orExpr(std::vector<Expr>& children);
    //! Create Boolean implication
    Expr impliesExpr(const Expr& hyp, const Expr& conc);
    //! Create left IFF right (boolean equivalence)
    Expr iffExpr(const Expr& left, const Expr& right);
    //! Create an equality expression.
    /*!
      The two children must have the same type, and cannot be of type
      Boolean.
    */
    Expr eqExpr(const Expr& child0, const Expr& child1);
    //! Create IF ifpart THEN thenpart ELSE elsepart ENDIF
    /*!
      \param ifpart must be of type Boolean.
      \param thenpart and \param elsepart must have the same type, which will
      also be the type of the ite expression.
    */
    Expr iteExpr(const Expr& ifpart, const Expr& thenpart,
		 const Expr& elsepart);

    /*@}*/ // End of Core expression methods

    /***************************************************************************/
    /*!
     *\name User-defined (uninterpreted) function methods
     * Methods for manipulating uninterpreted function expressions
     *@{
     */
    /***************************************************************************/

    //! Create a named uninterpreted function with a given type
    /*! 
      \param name is the new function's name (as ID Expr)
      \param type is a function type ( [range -> domain] )
    */
    Op createOp(const std::string& name, const Type& type);

    //! Unary function application (op must be of function type)
    Expr funExpr(const Op& op, const Expr& child);
    //! Binary function application (op must be of function type)
    Expr funExpr(const Op& op, const Expr& left, const Expr& right);
    //! Ternary function application (op must be of function type)
    Expr funExpr(const Op& op, const Expr& child0,
		 const Expr& child1, const Expr& child2);
    //! n-ary function application (op must be of function type)
    Expr funExpr(const Op& op, const std::vector<Expr>& children);

    /*@}*/ // End of User-defined (uninterpreted) function methods

    /***************************************************************************/
    /*!
     *\name Arithmetic expression methods
     * Methods for manipulating arithmetic expressions
     *
     * These functions create arithmetic expressions.  The children provided
     * as arguments must be of type Real.
     *@{
     */
    /***************************************************************************/

    //! Create a rational number with numerator n and denominator d.
    /*!
      \param n the numerator
      \param d the denominator, cannot be 0.
    */
    Expr ratExpr(int n, int d = 1);
    //! Create a rational number with numerator n and denominator d.
    /*!
      Here n and d are given as strings.  They are converted to
      arbitrary-precision integers according to the given base.
    */
    Expr ratExpr(const std::string& n, const std::string& d, int base);
    //! Create a rational from a single string.  
    /*!  
      \param n can be a string containing an integer, a pair of integers
      "nnn/ddd", or a number in the fixed or floating point format.
      \param base is the base in which to interpret the string.
    */
    Expr ratExpr(const std::string& n, int base = 10);

    //! Unary minus.
    Expr uminusExpr(const Expr& child);

    //! Create 2-element sum (left + right)
    Expr plusExpr(const Expr& left, const Expr& right);
    //! Make a difference (left - right)
    Expr minusExpr(const Expr& left, const Expr& right);
    //! Create a product (left * right)
    Expr multExpr(const Expr& left, const Expr& right);
    //! Create a power expression (x ^ n); n must be integer
    Expr powExpr(const Expr& x, const Expr& n);
    Expr divideExpr(const Expr& numerator, const Expr& denominator);

    //! Create (left < right)
    Expr ltExpr(const Expr& left, const Expr& right);
    //! Create (left <= right)
    Expr leExpr(const Expr& left, const Expr& right);
    //! Create (left > right)
    Expr gtExpr(const Expr& left, const Expr& right);
    //! Create (left >= right)
    Expr geExpr(const Expr& left, const Expr& right);
    //! Create FLOOR(expr)
    Expr floorExpr(const Expr& expr);

    /*@}*/ // End of Arithmetic expression methods

    /***************************************************************************/
    /*!
     *\name Record expression methods
     * Methods for manipulating record expressions
     *@{
     */
    /***************************************************************************/

    //! Create a 1-element record value (# field := expr #)
    /*! Fields will be sorted automatically */
    Expr recordExpr(const std::string& field, const Expr& expr);
    //! Create a 2-element record value (# field0 := expr0, field1 := expr1 #)
    /*! Fields will be sorted automatically */
    Expr recordExpr(const std::string& field0, const Expr& expr0,
		    const std::string& field1, const Expr& expr1);
    //! Create a 3-element record value (# field_i := expr_i #)
    /*! Fields will be sorted automatically */
    Expr recordExpr(const std::string& field0, const Expr& expr0,
		    const std::string& field1, const Expr& expr1,
		    const std::string& field2, const Expr& expr2);
    //! Create an n-element record value (# field_i := expr_i #)
    /*!
     * \param fields
     * \param exprs must be the same length as fields
     *
     * Fields will be sorted automatically
     */
    Expr recordExpr(const std::vector<std::string>& fields,
		    const std::vector<Expr>& exprs);

    //! Create record.field (field selection)
    /*! Create an expression representing the selection of a field from
      a record. */
    Expr recSelectExpr(const Expr& record, const std::string& field);

    //! Record update; equivalent to "record WITH .field := newValue"
    /*! Notice the `.' before field in the presentation language (and
      the comment above); this is to distinguish it from datatype
      update.
    */
    Expr recUpdateExpr(const Expr& record, const std::string& field,
		       const Expr& newValue);

    /*@}*/ // End of Record expression methods

    /***************************************************************************/
    /*!
     *\name Array expression methods
     * Methods for manipulating array expressions
     *@{
     */
    /***************************************************************************/

    //! Create an expression array[index] (array access)
    /*! Create an expression for the value of array at the given index */
    Expr readExpr(const Expr& array, const Expr& index);

    //! Array update; equivalent to "array WITH index := newValue"
    Expr writeExpr(const Expr& array, const Expr& index,
		   const Expr& newValue);

    /*@}*/ // End of Array expression methods

    /***************************************************************************/
    /*!
     *\name Other expression methods
     * Methods for manipulating other kinds of expressions
     *@{
     */
    /***************************************************************************/

    //! Tuple expression
    Expr tupleExpr(const std::vector<Expr>& exprs);
    //! Tuple select; equivalent to "tuple.n", where n is an numeral (e.g. tup.5)
    Expr tupleSelectExpr(const Expr& tuple, int index);
    //! Tuple update; equivalent to "tuple WITH index := newValue"
    Expr tupleUpdateExpr(const Expr& tuple, int index,
			 const Expr& newValue);
    //! Datatype update; equivalent to "dt WITH accessor := newValue"
    Expr datatypeUpdateExpr(const Expr& dt, const Expr& accessor,
			    const Expr& newValue);
    //! Universal quantifier
    Expr forallExpr(const std::vector<Expr>& vars, const Expr& body);
    //! Existential quantifier
    Expr existsExpr(const std::vector<Expr>& vars, const Expr& body);
    //! Lambda-expression
    Expr lambdaExpr(const std::vector<Expr>& vars, const Expr& body);
    //! Symbolic simulation expression
    /*!
     * \param f is the next state function (LAMBDA-expression)
     * \param s0 is the initial state
     * \param inputs is the vector of LAMBDA-expressions representing
     * the sequences of inputs to f
     * \param n is a constant, the number of cycles to run the simulation.
     */
    Expr simulateExpr(const Expr& f, const Expr& s0,
		      const std::vector<Expr>& inputs,
		      const Expr& n);

    /*@}*/ // End of Other expression methods

    /***************************************************************************/
    /*!
     *\name Validity checking methods
     * Methods related to validity checking
     *
     * This group includes methods for asserting formulas, checking
     * validity in the given logical context, manipulating the scope
     * level of the context, etc.
     *@{
     */
    /***************************************************************************/

    //! Assert a new formula in the current context.
    /*! The formula must have Boolean type.
     */
    void assertFormula(const Expr& e);

    //! Simplify e with respect to the current context
    Expr simplify(const Expr& e);
    //! Simplify e into e', and return the Theorem3 object for e = e'
    /*! The resulting Theorem3 (a 3-valued theorem object) can be used
      to extract the simplified expression and the proof of its
      equivalence to the origital expression: thm.getExpr() [to get
      e=e'], thm.getRHS() [to get just e'], and thm.getProof() [to get a
      Proof object].
    */
    Theorem3 simplifyThm(const Expr& e);
  
    void printV(const Expr& e);
  
    //! Check validity of e in the current context.
    /*! If the result is true, then the resulting context is the same as
      the starting context.  If the result is false, then the resulting
      context is a context in which e is false.  e must have Boolean
      type.
    
      \param e is the queried formula
    
      \param tcc is a flag indicating that the formula is a TCC, which
      is assumed to be always defined (so, its TCC is not computed)
    */
    bool query(const Expr& e, bool tcc = false) throw(CVCL::Exception);


    //! Get all assertions made in this and all previous contexts.
    /*! \param assertions should be empty on entry.
     */
    void getAssertions(std::vector<Expr>& assertions);

    //! Will return the set of assertions which make the queried formula false.
    /*! This method should only be called after a query which returns
      false.  It will try to return the simplest possible set of
      assertions which are sufficient to make the queried expression
      false.
      \param assertions should be empty on entry.
    */
    void getCounterExample(std::vector<Expr>& assertions);

    //! Returns true if the current context is inconsistent.
    /*! Also returns a minimal set of assertions used to determine the
      inconsistency.  
      \param assumptions should be empty on entry.
    */
    bool inconsistent(std::vector<Expr>& assumptions);

    //! Returns the proof term for the last proven query
    /*! If there has not been a successful query, it should return a NULL proof
     */
    Proof getProof();

    //! Returns the set of assertions used in the proof of queried formula.
    /*!  It returns a subset of getAssertions().  If the last query was not valid
      or there has not yet been a query, assumptions should be unchanged.
    */
    void getAssumptions(std::vector<Expr>& assumptions) throw(CVCL::Exception);

    //! Returns the TCC of the last assumption or query
    /*! Returns Null if no assumptions or queries were performed. */
    const Expr& getTCC();
    //! Return the set of assertions used in the proof of the last TCC
    void getAssumptionsTCC(std::vector<Expr>& assumptions);
    //! Returns the proof of TCC of the last assumption or query
    /*! Returns Null if no assumptions or queries were performed. */
    const Proof& getProofTCC();
    //! After successful query, return its closure |- Gamma => phi
    /*! Turn a valid query Gamma |- phi into an implication
     * |- Gamma => phi.
     *
     * Returns Null if last query was invalid.
     */
    const Expr& getClosure();
    //! Construct a proof of the query closure |- Gamma => phi
    /*! Returns Null if last query was Invalid. */
    const Proof& getProofClosure();

    /*@}*/ // End of Validity checking methods

    /***************************************************************************/
    /*!
     *\name Context methods
     * Methods for manipulating contexts
     *@{
     */
    /***************************************************************************/

    //! Checkpoint the current context and increase the scope level
    void push();

    //! Restore the current context to its state at the last checkpoint
    void pop() ;

    //! Restore the current context to the given scopeLevel.
    /*!
      \param scopeLevel must be less than or equal to the current scope level.

      If scopeLevel is less than 1, then the current context is reset
      and the scope level is set to 1.
    */
    void popto(int scopeLevel);

    //! Returns the current scope level.  Initially, the scope level is 1.
    int scopeLevel();

    //! Create a new context
    Context* createContext();

    //! Get the current context
    Context* getCurrentContext();

    //! Switch to a different context.  The old context is returned.
    Context* switchContext(Context* context);

    /*@}*/ // End of Context methods

    /***************************************************************************/
    /*!
     *\name Reporting Statistics
     * Methods for collecting and reporting run-time statistics
     *@{
     */
    /***************************************************************************/
  
    Statistics& getStatistics();
    //! Print collected statistics to stdout
    void printStatistics();


    %extend{
      ValidityChecker ()
	{
	  return CVCL::ValidityChecker::create();
	};
      ValidityChecker (CLFlags flags)
	{
	  return CVCL::ValidityChecker::create (flags);
	};
      // 	~ValidityChecker ()
      // 	    {
      // 		delete self;
      // 	    };
    }
  };
}



