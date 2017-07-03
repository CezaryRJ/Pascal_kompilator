package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

public class ParamDeclList extends PascalSyntax {
	// denne har en vilkarlig mengde med parametere.
	ArrayList<ParamDecl> parameters = new ArrayList<>();

	
	ParamDeclList(int lnum) {
		super(lnum);
	}

	public String identify() {
		return "<ParamDeclList> on line " + lineNum;
	}

	void prettyPrint() {
		Main.log.prettyPrint("(");
		parameters.get(0).prettyPrint();
		for (int i = 1; i < parameters.size(); i++) {
			Main.log.prettyPrint("; ");
			parameters.get(i).prettyPrint();
		}
		Main.log.prettyPrint(")");
	}

	static ParamDeclList parse(Scanner s) {
		enterParser("paramdecllist");
		ParamDeclList pdl = new ParamDeclList(s.curLineNum());
		s.skip(leftParToken);
		pdl.parameters.add(ParamDecl.parse(s));
		// en semicolon betyr at vi skal ha enda en parameter.
		while (s.curToken.kind == semicolonToken) {
			s.skip(semicolonToken);
			pdl.parameters.add(ParamDecl.parse(s));
		}
		s.skip(rightParToken);
		leaveParser("paramdecllist");
		return pdl;
	}
	void check(Block curScope,Library lib){
		for(int i = 0; i< parameters.size();i++){
			parameters.get(i).check(curScope,lib);
			parameters.get(i).rekke = i;
			
		}
		
	}
}
