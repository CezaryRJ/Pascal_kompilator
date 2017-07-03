package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class TypeName extends Type {
	// Denne klassen er for vanlige typer, som bare lagrer navnet paa typen.
	String name;

	TypeName(int lnum) {
		super(lnum);
	}

	public String identify() {
		return "<TypeName> on line " + lineNum;
	}

	void prettyPrint() {
		Main.log.prettyPrint(name);

	}

	static TypeName parse(Scanner s) {
		enterParser("typename");
		TypeName tn = new TypeName(s.curLineNum());
		tn.name = s.curToken.id;
		s.skip(nameToken);
		leaveParser("typename");
		return tn;
	}
	
void check(Block curScope,Library lib){
	
	PascalDecl tmp = curScope.findDecl(name, this);
	typeRef = (TypeDecl) tmp;
	}

}
