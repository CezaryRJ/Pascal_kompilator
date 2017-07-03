package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class InnerExpr extends Factor {

	Expression exp;

	InnerExpr(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}

	public String identify() {
		return "<innerexpr> on line " + lineNum;
	}

	public void prettyPrint() {
		Main.log.prettyPrint("(");
		exp.prettyPrint();
		Main.log.prettyPrint(")");
	}

	static InnerExpr parse(Scanner s) {
		enterParser("innerexpr");
		InnerExpr expr = new InnerExpr(s.curLineNum());
		s.skip(leftParToken);
		expr.exp = Expression.parse(s);
		s.skip(rightParToken);

		leaveParser("innerexpr");
		return expr;
	}
	void check(Block curScope,Library lib){
		exp.check(curScope,lib);
		type = exp.type;
	}
	public void genCode(CodeFile f) {
		exp.genCode(f);
	}
}
