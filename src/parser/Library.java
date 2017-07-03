package parser;

import main.CodeFile;

public class Library extends Block {
	int i = 0;
	public Library() {
		super(0);
		decls.put("write", new ProcDecl("write", 0));
		decls.put("true", new ConstDecl("true", 0));
		decls.put("false", new ConstDecl("false", 0));
		decls.get("false").type = booleantype;
		decls.get("true").type = booleantype;
		((ConstDecl) decls.get("true")).constval = 1; 
		((ConstDecl) decls.get("false")).constval = 0; 
		decls.put("eol", new ConstDecl("eol", 0));
		decls.get("eol").type = chartype;
		((ConstDecl) decls.get("eol")).constval = 10;
		decls.put("char", new TypeDecl("char", 0, chartype));
		decls.put("integer", new TypeDecl("integer", 0, inttype));
		decls.put("boolean", new TypeDecl("boolean", 0, booleantype));
	}

	//loggignen sier at library er paa linje 0 , istedenfor aa si at de er i library-
	types.BoolType booleantype = new types.BoolType();
	types.CharType chartype = new types.CharType();
	types.IntType inttype = new types.IntType();
	
	
	public void genCode(CodeFile f) {
	}
	public int counter(){
		i++;
		return i;
		
	}
}

