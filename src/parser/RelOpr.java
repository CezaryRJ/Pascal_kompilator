package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class RelOpr extends PascalSyntax {
	// Dette er operatorer som finner booleans, altsaa true og false. er en
	// constant mindre enn den andre? det lagres om en string for noen av
	// operatorene er mer enn en character lang.
	String operator;

	RelOpr(int lnum) {
		super(lnum);
	}

	public String identify() {
		return "<RelOpr> on line " + lineNum;
	}

	void prettyPrint() {
		Main.log.prettyPrint(operator);
	}

	static RelOpr parse(Scanner s) {
		enterParser("relopr");

		RelOpr ro = new RelOpr(s.curLineNum());
		switch (s.curToken.kind) {
		case equalToken:
			ro.operator = "=";
			s.skip(equalToken);
			break;
		case notEqualToken:
			ro.operator = "<>";
			s.skip(notEqualToken);
			break;
		case lessToken:
			ro.operator = "<";
			s.skip(lessToken);
			break;
		case lessEqualToken:
			ro.operator = "<=";
			s.skip(lessEqualToken);
			break;
		case greaterToken:
			ro.operator = ">";
			s.skip(greaterToken);
			break;
		case greaterEqualToken:
			ro.operator = ">=";
			s.skip(greaterEqualToken);
			break;
		}
		leaveParser("relopr");
		return ro;
	}
}
