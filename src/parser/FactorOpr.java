package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class FactorOpr extends PascalSyntax {
	// dette er en operator mellom Factorer. siden de kan vaere flere chars
	// lange, lagres de som en string.
	String operator;

	FactorOpr(int lnum) {
		super(lnum);
	}

	public String identify() {
		return "<FactorOpr> on line " + lineNum;
	}

	void prettyPrint() {
		Main.log.prettyPrint(" " + operator);
	}

	static FactorOpr parse(Scanner s) {
		enterParser("factoropr");
		FactorOpr fo = new FactorOpr(s.curLineNum());
		switch (s.curToken.kind) {
		case divToken:
			fo.operator = "div";
			s.skip(divToken);
			break;
		case modToken:
			fo.operator = "mod";
			s.skip(modToken);
			break;
		case andToken:
			fo.operator = "and";
			s.skip(andToken);
			break;
		case multiplyToken:
			fo.operator = "*";
			s.skip(multiplyToken);
			break;
		}
		leaveParser("factoropr");
		return fo;
	}
}
