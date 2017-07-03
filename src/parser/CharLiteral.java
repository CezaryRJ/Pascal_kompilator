package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class CharLiteral extends UnsignedConstant {
	// Denne klassen representerer en character.
	char character;
	
	CharLiteral(int lnum) {
		super(lnum);
	}

	public String identify() {
		return "<CharLiteral> on line " + lineNum;
	}

	void prettyPrint() {
		if (opr != null) {
			opr.prettyPrint();
		}
		Main.log.prettyPrint("'");
		Main.log.prettyPrint(String.valueOf(character));
		Main.log.prettyPrint("'");
	}

	static CharLiteral parse(Scanner s) {
		enterParser("charliteral");

		CharLiteral cl = new CharLiteral(s.curLineNum());
		cl.character = s.curToken.charVal;
		s.skip(charValToken);
		leaveParser("charliteral");
		return cl;
	}

	void check(Block curScope, Library lib) {
		type = lib.chartype;
		constval = (int) character;
	
		
	}
}
