package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class CompoundStatm extends Statement {

	StatmList stm;

	CompoundStatm(int lNum) {
		super(lNum);
		// TODO Auto-generated constructor stub
	}

	public String identify() {
		return "<Compound Statement> on line " + lineNum;
	}

	public void prettyPrint() {
		Main.log.prettyPrint("begin");
		Main.log.prettyIndent();
		Main.log.prettyPrintLn();
		stm.prettyPrint();
		Main.log.prettyPrintLn();
		;
		Main.log.prettyPrint("end");
		Main.log.prettyOutdent();
		Main.log.prettyPrintLn();

	}

	static CompoundStatm parse(Scanner s) {

		s.skip(beginToken);
		enterParser("compound statement");
		CompoundStatm statm = new CompoundStatm(s.curLineNum());
		statm.stm = StatmList.parse(s);
		s.skip(endToken);
		leaveParser("compound statement");
		return statm;

	}
void check(Block curScope,Library lib){
		stm.check(curScope, lib);
		
	}
	public void genCode(CodeFile f) {
		stm.genCode(f);
	}
}
