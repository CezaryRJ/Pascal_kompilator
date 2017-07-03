package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

public class VarDeclPart extends PascalSyntax {
	//Denne inneholder en vilkaarlig mengde vardecls
	ArrayList<VarDecl> variables = new ArrayList<>();

	VarDeclPart(int lnum) {
		super(lnum);
	}

	public String identify() {
		return "<VardeclPart> on line " + lineNum;
	}

	void prettyPrint() {
		Main.log.prettyPrintLn("var ");
		Main.log.prettyIndent();
		for (int i = 0; i < variables.size(); i++) {
			variables.get(i).prettyPrint();
		}
		Main.log.prettyOutdent();
		Main.log.prettyPrintLn();
	}

	static VarDeclPart parse(Scanner s) {
		enterParser("vardeclpart");

		VarDeclPart vdp = new VarDeclPart(s.curLineNum());
		s.skip(varToken);

		while (s.curToken.kind == nameToken) {
			vdp.variables.add(VarDecl.parse(s));
		}
		leaveParser("vardeclpart");
		return vdp;
	}
	void check(Block curScope,Library lib ){
		for(int i = 0; i<variables.size();i++){
			variables.get(i).check(curScope,lib);
			variables.get(i).rekke = i;
			
		}
		
	}
}
