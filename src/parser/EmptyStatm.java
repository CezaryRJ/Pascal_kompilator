package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class EmptyStatm extends Statement {
	// vel.........den er tom ?
	EmptyStatm(int lNum) {
		super(lNum);
		// TODO Auto-generated constructor stub
	}

	public String identify() {
		return "<empty-statm> on line " + lineNum;
	}

	public void prettyPrint() {

	}

	static EmptyStatm parse(Scanner s) {
		enterParser("empty-stmt");
		leaveParser("empty-stmt");

		return new EmptyStatm(s.curLineNum());

	}

	@Override
	void check(Block curScope, Library lib) {
		// TODO Auto-generated method stub
		
	}
	public void genCode(CodeFile f) {
		
	}
}
