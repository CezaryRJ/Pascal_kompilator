package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;

public class SimpleExpr extends PascalSyntax {

	PrefixOpr oper;
	ArrayList<TermOpr> trmopr = new ArrayList<>();
	ArrayList<Term> trm = new ArrayList<>();
	// en prefix operator etterfulgt av en vilkaaerlig mengde termer og term
	// operatorer
	types.Type type;

	SimpleExpr(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String identify() {
		return "<simple-expr> on line " + lineNum;
	}

	public void prettyPrint() {
		if (oper != null) {

			oper.prettyPrint();
		}
		trm.get(0).prettyPrint();
		for (int i = 1; i < trm.size(); i++) {
			trmopr.get(i - 1).prettyPrint();
			trm.get(i).prettyPrint();

		}

	}

	static SimpleExpr parse(Scanner s) {
		enterParser("simple-expr");
		SimpleExpr exp = new SimpleExpr(s.curLineNum());

		if (s.curToken.kind == addToken || s.curToken.kind == subtractToken) {
			exp.oper = PrefixOpr.parse(s);
		}
		exp.trm.add(Term.parse(s));
		while (s.curToken.kind == addToken || s.curToken.kind == subtractToken || s.curToken.kind == orToken) {
			// hvis det er en termoperatro saa maa den ha en operator aa operere
			// paa
			exp.trmopr.add(TermOpr.parse(s));
			exp.trm.add(Term.parse(s));

		}
		leaveParser("simple-expr");
		return exp;
	}

	void check(Block curScope, Library lib) {
		trm.get(0).check(curScope, lib);

		type = trm.get(0).type;
		if (oper != null) {
			type.checkType(lib.inttype, oper.type + " operands", this,
					"Non Integers cant have " + oper.type + " operator");

		}

		for (int i = 1; i < trm.size(); i++) {
			trm.get(i).check(curScope, lib);

			type.checkType(trm.get(i).type, "equal types", this, "Types are not equal");
			
			if(trmopr.get(i-1).operator.equals("+") || trmopr.get(i-1).operator.equals("-")){
				type.checkType(lib.inttype, "Integer type ", this, "Non Integers cant use +/-");
			}
			else {
				type.checkType(lib.booleantype, "Boolean type ", this, "Not a boolean");
			}

		}

	}
	public void genCode(CodeFile f) {
		trm.get(0).genCode(f);
		if(oper != null) {
			if(oper.type == '-')
			f.genInstr("", "negl", "%eax", "");
		}
		for (int i = 1; i < trm.size(); i++) {
			f.genInstr("", "pushl", "%eax", "");
			trm.get(i).genCode(f);
			f.genInstr("", "movl", "%eax,%ecx", "");
			f.genInstr("", "popl", "%eax", "");
			if(trmopr.get(i-1).operator == "+") {
				f.genInstr("", "addl", "%ecx,%eax", "");
			}
			else if(trmopr.get(i-1).operator == "-") {
				f.genInstr("", "subl", "%ecx,%eax", "");
			}
			else {
				f.genInstr("", "orl", "%ecx,%eax", "");
			}
		}
	}

}
