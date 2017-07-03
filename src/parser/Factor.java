package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

abstract class Factor extends PascalSyntax {
//superkalsse av typen factor
	Factor(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}
	types.Type type;

	@Override
	public String identify() {
		return "<factor> on line " + lineNum;
	}

	abstract void prettyPrint();

	static Factor parse(Scanner s) {

		enterParser("factor");
		Factor st = null;
		switch (s.curToken.kind) {
		case nameToken:
			switch (s.nextToken.kind) {

			case leftParToken:
				st = FuncCall.parse(s);
				break;
			default:
				st = Variable.parse(s);
				break;
			}
			break;
		case leftParToken:
			st = InnerExpr.parse(s);
			break;
		case notToken:
			st = Negation.parse(s);
			break;
		default:
			st = UnsignedConstant.parse(s);
			break;
		}

		leaveParser("factor");
		return st;
	}
	
abstract void check(Block curScope,Library lib);
abstract void genCode(CodeFile f);
}
