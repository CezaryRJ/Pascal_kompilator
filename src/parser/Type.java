package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public abstract class Type extends PascalSyntax {
	// Type er superklassen for typename og arraytype.
	Type(int lnum) {
		super(lnum);
	}

	TypeDecl typeRef;
	abstract void prettyPrint();

	static Type parse(Scanner s) {
		enterParser("type");

		Type t = null;
		switch (s.curToken.kind) {
		case nameToken:
			t = TypeName.parse(s);
			break;
		case arrayToken:
			t = ArrayType.parse(s);
			break;
		}
		leaveParser("type");
		return t;
	}
abstract void check(Block curScope,Library lib);
}
