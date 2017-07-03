package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class PrefixOpr extends PascalSyntax {
	// Denne er kan ha + eller -, og lagrer det som en character.
	char type;

	PrefixOpr(int lnum) {
		super(lnum);
	}

	public String identify() {
		return "<PrefixOpr> on line " + lineNum;
	}

	public void prettyPrint() {
		Main.log.prettyPrint(String.valueOf(type));
		Main.log.prettyPrint(" ");
	}

	static PrefixOpr parse(Scanner s) {
		enterParser("prefixopr");
		PrefixOpr po = new PrefixOpr(s.curLineNum());
		if (s.curToken.kind == addToken) {
			po.type = '+';
			s.skip(addToken);
		} else {
			po.type = '-';
			s.skip(subtractToken);
		}
		leaveParser("prefixopr");
		return po;
	}
}
