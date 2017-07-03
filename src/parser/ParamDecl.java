package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class ParamDecl extends PascalDecl{
	//String name;
	TypeName type;
	int rekke;
	
	ParamDecl(String name,int lnum) {
		super(name, lnum);
	}
	public String identify() {
		return "<ParamDecl> on line " + lineNum;
	}
	void prettyPrint() {
		Main.log.prettyPrint(name);
		Main.log.prettyPrint(": ");
		type.prettyPrint();
	}
	static ParamDecl parse(Scanner s) {
		enterParser("paramdecl");
		
		ParamDecl pd = new ParamDecl(s.curToken.id,s.curLineNum());
		pd.name = s.curToken.id;
		s.skip(nameToken);
		s.skip(colonToken);
		pd.type = TypeName.parse(s);
		leaveParser("paramdecl");
		return pd;
	}
	@Override
	void checkWhetherAssignable(PascalSyntax where) {
		where.error("You can't assign to a parameter");
		
	}
	@Override
	void checkWhetherFunction(PascalSyntax where) {
		where.error("A parameter isn't a function.");
		
	}
	@Override
	void checkWhetherProcedure(PascalSyntax where) {
		where.error("A parameter isn't a procedure.");
		
	}
	@Override
	void checkWhetherValue(PascalSyntax where) {
		
	}
	void check(Block curScope,Library lib){
		curScope.addDecl(name, this);

		type.check(curScope, lib);
		level = curScope.level ;
	
		
	}
}
