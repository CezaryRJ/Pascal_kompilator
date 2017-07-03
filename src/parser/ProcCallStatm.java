package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;

public class ProcCallStatm extends Statement {

	String name;
	ArrayList<Expression> expr = new ArrayList<>();
	// har nevn og en vilaaerlig mengde statements
	ProcDecl procRef;

	ProcCallStatm(int lNum) {
		super(lNum);
		// TODO Auto-generated constructor stub
	}

	static ProcCallStatm parse(Scanner s) {
		enterParser("proccall-statm");

		ProcCallStatm pcs = new ProcCallStatm(s.curLineNum());
		pcs.name = s.curToken.id;
		s.skip(nameToken);

		if (s.curToken.kind == leftParToken) {

			s.skip(leftParToken);

			pcs.expr.add(Expression.parse(s));

			while (s.curToken.kind == commaToken) {
				s.skip(commaToken);
				pcs.expr.add(Expression.parse(s));

			}
			s.skip(rightParToken);

		}

		leaveParser("proccall-statm");
		return pcs;

	}

	public void prettyPrint() {
		Main.log.prettyIndent();
		Main.log.prettyPrint(name);

		if (expr.size() > 0) {
			Main.log.prettyPrint("(");
			expr.get(0).prettyPrint();

			for (int i = 1; i < expr.size() - 1; i++) {
				Main.log.prettyPrint(",");
				expr.get(i).prettyPrint();

			}

			Main.log.prettyPrint(")");
		}
		Main.log.prettyPrint(";");
		Main.log.prettyOutdent();
	}

	@Override
	public String identify() {

		return "<proccall-statm> on line " + lineNum;
	}

	void check(Block curScope, Library lib) {
		PascalDecl tmp = curScope.findDecl(name, this);
		procRef = (ProcDecl) tmp;

		for (int i = 0; i < expr.size(); i++) {
			expr.get(i).check(curScope, lib);
			if (name.equals("write")) {

			} else {
				expr.get(i).type.checkType(procRef.pdl.parameters.get(i).type.typeRef.type, "Correct parameter type",
						this, "Parameter type incorrect");
			}
		}
		procRef.checkWhetherProcedure(this);

	}
	public void genCode(CodeFile f) {
		if(name.equals("write")) {
			for (int i = 0; i < expr.size(); i++) {
				expr.get(i).genCode(f);
				f.genInstr("", "pushl", "%eax", "");
				if (expr.get(i).type instanceof types.IntType) {
					f.genInstr("", "call", "write_int", "");
				}
				else if (expr.get(i).type instanceof types.CharType) {
					f.genInstr("", "call", "write_char", "");
				}
				else {
					f.genInstr("", "call", "write_bool", "");
				}
				f.genInstr("", "addl", "$4,%esp", "");
			}
		}
		else {
			for (int i = expr.size() - 1; i >= 0; i--) {
				expr.get(i).genCode(f);
				f.genInstr("", "pushl", "%eax", "");
			}
			f.genInstr("", "call", "proc$" + name + "_" + procRef.labelnr, "");
			if (expr.size() > 0) {
				f.genInstr("", "addl", "$" + expr.size()*4 + ",%esp", "");
			}
		}
	}
}
