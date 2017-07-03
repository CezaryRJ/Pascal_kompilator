package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;

public class FuncCall extends Factor {
	ArrayList<Expression> expr = new ArrayList<>();

	String name;
	// navn og vilkaaerlig antall expressions
	FuncDecl funkRef;

	FuncCall(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}

	public String identify() {
		return "<func-call> on line " + lineNum;
	}

	public void prettyPrint() {
		Main.log.prettyOutdent();
		Main.log.prettyPrint(name);

		Main.log.prettyPrint("(");
		expr.get(0).prettyPrint();
		for (int i = 1; i < expr.size(); i++) {
			Main.log.prettyPrint(",");
			expr.get(i).prettyPrint();

		}

		Main.log.prettyPrintLn(")");
		Main.log.prettyIndent();
	}

	static FuncCall parse(Scanner s) {
		enterParser("funccall");

		FuncCall pcs = new FuncCall(s.curLineNum());
		pcs.name = s.curToken.id;
		s.skip(nameToken);
		// hvis vi finner en parantes saa er det argumenter aa ta inn, ellers
		// gaa videre
		if (s.curToken.kind == leftParToken) {
			s.skip(leftParToken);

			pcs.expr.add(Expression.parse(s));
			while (s.curToken.kind == commaToken) {
				s.skip(commaToken);

				pcs.expr.add(Expression.parse(s));

			}

			s.skip(rightParToken);
		}

		leaveParser("funccall");
		return pcs;
	}

	void check(Block curScope, Library lib) {
		PascalDecl tmp = curScope.findDecl(name, this);
		funkRef = (FuncDecl) tmp;
		type = funkRef.type.typeRef.type;
		for (int i = 0; i < expr.size(); i++) {
			expr.get(i).check(curScope, lib);

			expr.get(i).type.checkType(funkRef.pdl.parameters.get(i).type.typeRef.type, "Correct parameter type", this,
					"Parameter type incorrect");
		}
		funkRef.checkWhetherFunction(this);
	}
	public void genCode(CodeFile f) {
		for (int i = expr.size() - 1; i >= 0; i--) {
			expr.get(i).genCode(f);
			f.genInstr("", "pushl", "%eax", "");
		}
		f.genInstr("", "call", "func$" + name + "_" + funkRef.labelnr, "");
		if (expr.size() > 0) {
			f.genInstr("", "addl", "$" + expr.size()*4 + ",%esp", "");
		}
	}
}
