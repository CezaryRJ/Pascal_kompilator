package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class Variable extends Factor {

	String name;
	Expression exps = null;
	PascalDecl varRef;

	Variable(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String identify() {
		return "<variable> on line " + lineNum;
	}

	public void prettyPrint() {
		Main.log.prettyPrint(" " + name);
		if (exps != null) {
			exps.prettyPrint();

		}

	}

	static Variable parse(Scanner s) {
		enterParser("variable");
		Variable var = new Variable(s.curLineNum());
		var.name = s.curToken.id;
		s.skip(nameToken);
		if (s.curToken.kind == leftBracketToken) {
			s.skip(leftBracketToken);
			var.exps = Expression.parse(s);
			s.skip(rightBracketToken);
		}
		leaveParser("variable");
		return var;
	}

	void check(Block curScope, Library lib) {

		PascalDecl tmp = curScope.findDecl(name, this);
		varRef = tmp;
		if (exps != null) {
			exps.check(curScope, lib);
		}
		if (varRef instanceof FuncDecl) {

			FuncDecl tmp1 = (FuncDecl) varRef;
			type = tmp1.type.typeRef.type;
		}
		if (varRef instanceof VarDecl) {
			VarDecl tmp2 = (VarDecl) varRef;
			type = tmp2.type.typeRef.type;

		}
		if (varRef instanceof ConstDecl) {
			ConstDecl tmp3 = (ConstDecl) varRef;
			type = tmp3.type;

		}
		if (varRef instanceof ParamDecl) {
			ParamDecl tmp4 = (ParamDecl) varRef;
			type = tmp4.type.typeRef.type;

		}
		if (varRef instanceof TypeDecl) {
			TypeDecl tmp5 = (TypeDecl) varRef;
			type = tmp5.type;

		}
	}

	@Override
	void genCode(CodeFile f) {

		if (varRef instanceof FuncDecl) {
			f.genInstr("","call", "proc$" + name + "_n", "");
			

		} else if (varRef instanceof VarDecl) {
			f.genInstr("", "movl", -4 * varRef.level + "(%ebp),%edx", "");
			f.genInstr("", "movl", (((VarDecl) varRef).rekke * -4) - 36 + "(%edx),%eax", "");

		

		} else if (varRef instanceof ConstDecl) {

			f.genInstr("","movl", "$" + ((ConstDecl) varRef).constval + ",%eax", "");
			
			
		} else if (varRef instanceof ParamDecl) {

			f.genInstr("", "movl", varRef.level * -4 + "(%ebp),%edx", "");
			f.genInstr("", "movl",((((ParamDecl) varRef).rekke * 4) + 8) + "(%edx),%eax" , "");
			
			
		} else if (varRef instanceof TypeDecl) {
					System.out.println("FEIL I KODEGEN , TYPEFEIL");
					System.exit(0);
		}

	}

}
