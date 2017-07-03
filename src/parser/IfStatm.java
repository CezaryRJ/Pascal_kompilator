package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class IfStatm extends Statement {

	IfStatm(int lNum) {
		super(lNum);
		// TODO Auto-generated constructor stub
	}

	Expression check;
	Statement a;
	Statement b;

	public String identify() {
		return "<if-statm> on line " + lineNum;
	}

	public void prettyPrint() {
		Main.log.prettyPrint("if ");
		check.prettyPrint();
		Main.log.prettyPrintLn(" then");
		a.prettyPrint();
		if (b != null) {
			Main.log.prettyPrint(" else ");
			Main.log.prettyPrintLn();
			b.prettyPrint();

		}

	}

	static IfStatm parse(Scanner s) {
		enterParser("if-statm");
		IfStatm ifstm = new IfStatm(s.curLineNum());
		s.skip(ifToken);
		ifstm.check = Expression.parse(s);
		s.skip(thenToken);
		ifstm.a = Statement.parse(s);

		if (s.curToken.kind == elseToken) {
			s.skip(elseToken);
			ifstm.b = Statement.parse(s);
		}
		return ifstm;

	}
	void check(Block curScope,Library lib){
		check.check(curScope, lib);
		check.type.checkType(lib.booleantype, "Boolen type", this, "Not a boolean");
		a.check(curScope, lib);
		if(b != null){
		b.check(curScope, lib);
		}
	}
	public void genCode(CodeFile f) {
		String ifLabel = f.getLocalLabel(),
				elseLabel = f.getLocalLabel();
		
		check.genCode(f);
		f.genInstr("", "cmpl", "$0,%eax", "");
		f.genInstr("", "je", elseLabel, "");
		a.genCode(f);
		if (b!= null) {
			f.genInstr("", "jmp", ifLabel, "");
			f.genInstr(elseLabel, "", "", "");
			b.genCode(f);
			f.genInstr(ifLabel, "", "", "");
		}
		else {
			f.genInstr(elseLabel, "", "", "");
		}
	}
	
}
