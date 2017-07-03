package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Block extends PascalSyntax{
ConstDeclPart constants = null;
VarDeclPart variables = null;
ArrayList<ProcDecl> proc = new ArrayList<>();
ArrayList<FuncDecl> funk = new ArrayList<>();
HashMap<String,PascalDecl> decls = new HashMap<>();
StatmList stm;
int level = 0;
Block outerScope;
	Block(int n) {
		super(n);
		
	}

	@Override
	public String identify() {
		return "<Block> on line " + lineNum;
	}

	
	void prettyPrint(){
		
		if(constants != null){
			constants.prettyPrint();
			
		}
		if(variables != null){
			variables.prettyPrint();
			
		}
		for(int i = 0; i < proc.size();i++){

			proc.get(i).prettyPrint();
		}
		for(int i = 0; i < funk.size();i++){

			funk.get(i).prettyPrint();
		}
		
		Main.log.prettyPrint("begin ");
		Main.log.prettyPrintLn();
		Main.log.prettyIndent();
		stm.prettyPrint();
		Main.log.prettyOutdent();
		Main.log.prettyPrint("end");

	}
	
	static Block parse(Scanner s) {
		enterParser("block");
		
		Block b = new Block(s.curLineNum());
		if(s.curToken.kind == constToken){
			b.constants = ConstDeclPart.parse(s);
			
		}
		if(s.curToken.kind == varToken){
			b.variables = VarDeclPart.parse(s);
			
		}
		
		while (s.curToken.kind == functionToken || s.curToken.kind == procedureToken){
			if(s.curToken.kind == functionToken){

				b.funk.add(FuncDecl.parse(s));
			}
			else {
				b.proc.add(ProcDecl.parse(s));
			}
			
		}
		s.skip(beginToken);
		b.stm = StatmList.parse(s);
		s.skip(endToken);
		leaveParser("block");
		return b;
		
	}
	void addDecl(String id, PascalDecl d) {
		if (decls.containsKey(id))
		d.error(id + " declared twice in same block!");
		decls.put(id, d);
		}

	public void check(Block curScope, Library lib) {
		
		level = curScope.level + 1;
	
		
		outerScope = curScope;
		if(constants != null){
		constants.check(this,lib);
		}
		
		if(variables != null){
		variables.check(this,lib);
		}
		
		for(int i = 0; i < proc.size();i++){
			proc.get(i).check(this,lib);
			
		}
		for(int i = 0; i < funk.size();i++){
			funk.get(i).check(this,lib);
			
		}
	
		
		
		if(stm != null){
		stm.check(this,lib);
		}
		
	}
	
	PascalDecl findDecl(String id, PascalSyntax where) {
		PascalDecl d = decls.get(id);
		if (d != null) {
		Main.log.noteBinding(id, where, d);
		return d;
		}
		if (outerScope != null)
		return outerScope.findDecl(id,where);
		where.error("Name " + id + " is unknown!");
		return null; // Required by the Java compiler.
		}
	public void genCode(CodeFile f){
		stm.genCode(f);
	}
	
}
