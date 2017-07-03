package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class TermOpr extends PascalSyntax {
	// Dette er en operatr mellom termer. siden en av mulighetene er 2
	// characters lang lagres de som string.
	String operator;

	TermOpr(int lnum) {
		super(lnum);
	}

	public String identify() {
		return "<TermOpr> on line " + lineNum;
	}

	void prettyPrint() {
		Main.log.prettyPrint(" " + operator + " ");
	}

	static TermOpr parse(Scanner s) {
		enterParser("termopr");
		TermOpr to = new TermOpr(s.curLineNum());
		switch (s.curToken.kind) {
		case orToken:
			to.operator = "or";
			s.skip(orToken);
			break;
		case addToken:
			to.operator = "+";
			s.skip(addToken);
			break;
		case subtractToken:
			to.operator = "-";
			s.skip(subtractToken);
			break;
		}
		leaveParser("termopr");
		return to;
	}
}
