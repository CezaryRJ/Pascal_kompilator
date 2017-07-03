package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class VarDecl extends PascalDecl{

	Type type;
	int rekke;
	//String name;
	VarDecl(String name ,int lnum) {
		super(name, lnum);
	}
	public String identify() {
		return "<VarDecl> on line " + lineNum;
	}
	void prettyPrint() {
		Main.log.prettyPrint(name);
		Main.log.prettyPrint(": ");
		type.prettyPrint();
		Main.log.prettyPrintLn(";");
	}
	static VarDecl parse(Scanner s) {
		enterParser("vardecl");
		
		VarDecl vd = new VarDecl(s.curToken.id,s.curLineNum());
		vd.name = s.curToken.id;
		s.skip(nameToken);
		
		s.skip(colonToken);
		vd.type = Type.parse(s);
		s.skip(semicolonToken);
		leaveParser("vardecl");
		return vd;
	}
	@Override
	void checkWhetherAssignable(PascalSyntax where) {
		
	}
	@Override
	void checkWhetherFunction(PascalSyntax where) {
		where.error("A variable isn't a function.");
		
	}
	@Override
	void checkWhetherProcedure(PascalSyntax where) {
		where.error("A variable isn't a procedure.");
		
	}
	@Override
	void checkWhetherValue(PascalSyntax where) {
		
	}
	
	void check(Block curScope,Library lib){
		level = curScope.level;
		curScope.addDecl(name, this);
		type.check(curScope, lib);
		
	}
}
