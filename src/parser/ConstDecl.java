package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class ConstDecl extends PascalDecl{
//	String name;
	Constant c;
	
	int constval;
	ConstDecl(String name,int lnum) {
		super(name,lnum);
	}
	public String identify() {
		return "<ConstDecl> on line " + lineNum;
	}
	void prettyPrint() {
		Main.log.prettyPrint(name);
		Main.log.prettyPrint(" = ");
		c.prettyPrint();
		Main.log.prettyPrintLn(";");
	}
	static ConstDecl parse(Scanner s) {
		enterParser("constdecl");
		
		ConstDecl cd = new ConstDecl(s.curToken.id,s.curLineNum());
		cd.name = s.curToken.id;
		s.skip(nameToken);
		s.skip(equalToken);
		
		cd.c = Constant.parse(s);
		s.skip(semicolonToken);
		
		leaveParser("consdecl");
		return cd;
	}
	@Override
	void checkWhetherAssignable(PascalSyntax where) {
		where.error("You cannot assign to a constant.");

		
	}
	@Override
	void checkWhetherFunction(PascalSyntax where) {
		where.error("A constant isn't a function.");
		
	}
	@Override
	void checkWhetherProcedure(PascalSyntax where) {
		where.error("A constant isn't a procedure.");
		
	}
	@Override
	void checkWhetherValue(PascalSyntax where) {
		// TODO Auto-generated method stub
		
	}
	
	void check(Block curScope,Library lib ){
		curScope.addDecl(name, this);
		c.check(curScope, lib);
		type = c.type;
		constval = c.constval;
		
	}
}
