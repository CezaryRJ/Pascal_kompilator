package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class FuncDecl extends PascalDecl{
//	String name;
	ParamDeclList pdl = null;
	TypeName type;
	Block block;
	//int level = 0;
	int labelnr = 0;
	FuncDecl(String name, int lnum) {
		super(name, lnum);
	}
	public String identify() {
		return "<FuncDecl> on line " + lineNum;
	}
	void prettyPrint() {
		Main.log.prettyPrint("function ");
		Main.log.prettyPrint(name + " ");
		if (pdl != null) {
			pdl.prettyPrint();
		}
		Main.log.prettyPrint(": ");
		type.prettyPrint();
		Main.log.prettyPrintLn(";");
		block.prettyPrint();
		Main.log.prettyPrint(";"); 
		Main.log.prettyPrintLn();
	}
	static FuncDecl parse(Scanner s) {
		enterParser("funcdecl");
		
		FuncDecl fd = new FuncDecl(s.curToken.id,s.curLineNum());
		s.skip(functionToken);
		fd.name = s.curToken.id;
		s.skip(nameToken);
		if(s.curToken.kind == leftParToken) {
			fd.pdl = ParamDeclList.parse(s);
		}
		s.skip(colonToken);
		fd.type = TypeName.parse(s);
		s.skip(semicolonToken);
		fd.block = Block.parse(s);
		s.skip(semicolonToken);
		leaveParser("funcdecl");
		return fd;
	}
	@Override
	void checkWhetherAssignable(PascalSyntax where) {
	

		
	}
	@Override
	void checkWhetherFunction(PascalSyntax where) {
		
	}
	@Override
	void checkWhetherProcedure(PascalSyntax where) {
		where.error("A function isn't a procedure.");
		
	}
	@Override
	void checkWhetherValue(PascalSyntax where) {
		
	}
	
	void check(Block curScope,Library lib){
		labelnr = lib.counter();
		curScope.addDecl(name, this);
		level = curScope.level + 1;
		
	
		block.outerScope = curScope;
		block.level = curScope.level + 1;
		
		if(pdl != null){
			
			pdl.check(block,lib);
			
		}
		type.check(curScope, lib);
		block.check(curScope, lib);
		
	}
	public void genCode(CodeFile f){
		

		for(int i = 0; i < block.proc.size();i++){
			block.proc.get(i).genCode(f);
			
		}
		
		for(int i = 0; i < block.funk.size();i++){
			block.funk.get(i).genCode(f);
			
		}
			
		f.genInstr("func$" + name + "_" + labelnr ,"","","");
		
	
		if(block.variables != null){
			
			f.genInstr("","enter" ,"$" + (32 + ( block.variables.variables.size() * 4)) + ",$" + level, "");
		}
		else {
			f.genInstr("","enter" ,"$32,$" + level, "");
		}
		
		block.genCode(f);
		f.genInstr("", "movl", "-32(%ebp),%eax", "");
		f.genInstr("", "leave", "", "");
		f.genInstr("", "ret", "", "");
		
	
		
	}
}
