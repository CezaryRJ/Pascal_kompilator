package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class UnsignedConstant extends Constant {
	// Denne klasses er en unsigned constant, og brukes bare hvis man skal ha et
	// navn i den, skal man ha en int eller en char har egne subklasser for det.
	String name;
	
	PascalDecl declRef;

	UnsignedConstant(int lnum) {
		super(lnum);
	}

	public String identify() {
		return "<UnsignedConstant> on line " + lineNum;
	}

	void prettyPrint() {
		if (opr != null) {
			opr.prettyPrint();
		}
		Main.log.prettyPrint(name);
	}

	static UnsignedConstant parse(Scanner s) {
		enterParser("unsignedconstant");
		UnsignedConstant uc = null;
		switch (s.curToken.kind) {
		case nameToken:
			uc = new UnsignedConstant(s.curLineNum());
			uc.name = s.curToken.id;
			s.skip(nameToken);
			break;
		case intValToken:
			uc = NumericLiteral.parse(s);
			break;
		case charValToken:
			uc = CharLiteral.parse(s);
			break;
		}
		leaveParser("unsignedconstant");
		return uc;
	}
	void check(Block curScope,Library lib){
		
		
		
		PascalDecl tmp = curScope.findDecl(name, this);
		declRef = tmp;
		declRef.checkWhetherValue(this);
		type = tmp.type;
		
		ConstDecl tmp1 = (ConstDecl) tmp;
		constval = tmp1.constval;
		
		
	}
	public void genCode(CodeFile f) {
		f.genInstr("", "movl","$" +  constval + ",%eax", "");
	}
}
