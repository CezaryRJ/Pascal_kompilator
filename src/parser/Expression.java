package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class Expression extends PascalSyntax {

	SimpleExpr a;
	RelOpr oper = null;
	SimpleExpr b = null;
	types.Type type;

	Expression(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String identify() {
		return "<expression> on line " + lineNum;
	}

	void prettyPrint() {
		a.prettyPrint();
		if (oper != null) {
			oper.prettyPrint();
			b.prettyPrint();
		}
	}

	static Expression parse(Scanner s) {
		enterParser("expression");

		Expression exp = new Expression(s.curLineNum());
		exp.a = SimpleExpr.parse(s);
		if (s.curToken.kind == equalToken || s.curToken.kind == lessToken || s.curToken.kind == lessEqualToken
				|| s.curToken.kind == greaterToken || s.curToken.kind == greaterEqualToken
				|| s.curToken.kind == notEqualToken) {
			exp.oper = RelOpr.parse(s);
			exp.b = SimpleExpr.parse(s);
		}
		leaveParser("expression");
		return exp;

	}

	void check(Block curScope, Library lib) {

		
			
		a.check(curScope, lib);
		type = a.type;
		
		

		if (b != null) {
			b.check(curScope, lib);
			String oprName = oper.operator;
	
			type.checkType(b.type, oprName + " operands", this, "operands to " + oprName + " are of different type !");

			type = lib.booleantype;
		}

	}
	public void genCode(CodeFile f) {
		if (oper == null) {
			a.genCode(f);
		}
		else {
			a.genCode(f);
			f.genInstr("", "pushl", "%eax", "");
			b.genCode(f);
			f.genInstr("", "popl", "%ecx", "");
			f.genInstr("", "cmpl", "%eax,%ecx", "");
			f.genInstr("", "movl", "$0,%eax", "");
			switch (oper.operator){
			case "=": 
				f.genInstr("", "sete", "%al", "");
				break;
			case "<>":
				f.genInstr("", "setne", "%al", "");
				break;
			case "<":
				f.genInstr("", "setl", "%al", "");
				break;
			case "<=":
				f.genInstr("", "setle", "%al", "");
				break;
			case ">":
				f.genInstr("", "setg", "%al", "");
				break;
			case ">=":
				f.genInstr("", "setge", "%al", "");
				break;
			}
			
		}
	}
}
