package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public abstract class Constant extends Factor {
	// Dette er den abstracte klassen constant, den er en subklasse av factor,
	// fordi subklassen til constant unsigned constant skal ogsaa vaere en
	// subklasse av factor.
	PrefixOpr opr = null;
	int constval;
	Constant(int lnum) {
		super(lnum);
	}

	abstract void prettyPrint();

	static Constant parse(Scanner s) {
		enterParser("constant");
		PrefixOpr o = null;
		if (s.curToken.kind == addToken || s.curToken.kind == subtractToken) {
			o = PrefixOpr.parse(s);
		}
		Constant c = UnsignedConstant.parse(s);
		if (o != null) {
			c.opr = o;
		}
		leaveParser("constant");
		return c;
	}
void check(Block curScope,Library lib){
	
		
	}
}
