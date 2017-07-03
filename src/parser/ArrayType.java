package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class ArrayType extends Type {
	// Denne klassen er for arrays, og lagrer start og slutten paa arrayet.
	Constant start;
	Constant end;
	Type type;
	
	TypeDecl typeRef;
	ArrayType(int lnum) {
		super(lnum);
	}

	public String identify() {
		return "<ArrayType> on line " + lineNum;
	}

	void prettyPrint() {
		Main.log.prettyPrint("array");
		Main.log.prettyPrint("[");
		start.prettyPrint();
		Main.log.prettyPrint("..");
		end.prettyPrint();
		Main.log.prettyPrint("] ");
		Main.log.prettyPrint("of ");
		type.prettyPrint();
	}

	static ArrayType parse(Scanner s) {
		enterParser("arraytype");

		ArrayType at = new ArrayType(s.curLineNum());
		s.skip(arrayToken);
		s.skip(leftBracketToken);
		at.start = Constant.parse(s);
		s.skip(rangeToken);
		at.end = Constant.parse(s);
		s.skip(rightBracketToken);
		s.skip(ofToken);
		at.type = Type.parse(s);

		leaveParser("Arraytype");
		return at;
	}
void check(Block curScope,Library lib){
		start.check(curScope, lib);
		end.check(curScope, lib);
		type.check(curScope,lib);
		typeRef = type.typeRef;
	}
}
