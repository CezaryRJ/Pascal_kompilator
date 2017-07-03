package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

public class ConstDeclPart extends PascalSyntax {
	//Denne inneholder en vilkaarlig mengde Constdecls
	ArrayList<ConstDecl> constants = new ArrayList<>();

	ConstDeclPart(int lnum) {
		super(lnum);
	}

	public String identify() {
		return "<ConstDeclPart> on line " + lineNum;
	}

	void prettyPrint() {
		Main.log.prettyPrintLn("const");
		Main.log.prettyIndent();
		for (int i = 0; i < constants.size(); i++) {
			constants.get(i).prettyPrint();
		}
		Main.log.prettyOutdent();
		Main.log.prettyPrintLn();
	}

	static ConstDeclPart parse(Scanner s) {
		enterParser("constdeclpart");

		ConstDeclPart cdp = new ConstDeclPart(s.curLineNum());
		s.skip(constToken);

		while (s.curToken.kind == nameToken) {
			cdp.constants.add(ConstDecl.parse(s));
		}
		leaveParser("constdeclpart");
		return cdp;
	}
	void check(Block curScope,Library lib){
		for(int i = 0; i<constants.size();i++){
			constants.get(i).check(curScope,lib);
		}
	}
}
