package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class NumericLiteral extends UnsignedConstant {
	// Denne klassen representerer et nummer.
	int number;

	NumericLiteral(int lnum) {
		super(lnum);
		
	}

	public String identify() {
		return "<NumericLiteral> on line " + lineNum;
	}

	void prettyPrint() {
		if (opr != null) {
			opr.prettyPrint();
		}
		Main.log.prettyPrint(String.valueOf(number));
	}

	static NumericLiteral parse(Scanner s) {
		enterParser("numericliteral");

		NumericLiteral nl = new NumericLiteral(s.curLineNum());
		nl.number = s.curToken.intVal;
		s.skip(intValToken);
		leaveParser("numericliteral");
		return nl;
	}

	void check(Block curScope, Library lib) {
		type = lib.inttype;
		constval = number;
	}
}
