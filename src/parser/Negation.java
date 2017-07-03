package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class Negation extends Factor {

	Factor fact;
	//this is NOT a fact 
	
	Negation(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}

	public String identify() {
		return "<negation> on line " + lineNum;
	}

	public void prettyPrint() {
		Main.log.prettyPrint("not ");
		fact.prettyPrint();

	}

	static Negation parse(Scanner s) {
		enterParser("negation");

		Negation neg = new Negation(s.curLineNum());
		s.skip(notToken);
		neg.fact = Factor.parse(s);
		leaveParser("negation");
		return neg;
	}
	void check(Block curScope,Library lib){
		fact.check(curScope,lib);
		fact.type.checkType(lib.booleantype, "Boolean type", this, "Not a boolean");
		type = fact.type;
	}
	public void genCode(CodeFile f) {
		fact.genCode(f);
		f.genInstr("", "xorl", "$1,%eax", "");
	}
}
