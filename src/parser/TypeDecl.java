package parser;

public class TypeDecl extends PascalDecl {
	
	types.Type type;
	
	TypeDecl(String n, int lnum , types.Type t) {
		super(n, lnum);
		type = t;
		
	}

	
	@Override
	void checkWhetherAssignable(PascalSyntax where) {
		where.error("You cannot assign to a type.");
		
	}

	@Override
	void checkWhetherFunction(PascalSyntax where) {
		where.error("Types aren't Functions");
		
	}

	@Override
	void checkWhetherProcedure(PascalSyntax where) {
		where.error("Types aren't procedures.");
		
	}

	@Override
	void checkWhetherValue(PascalSyntax where) {
		where.error("Types don't have a value.");
	}

	@Override
	public String identify() {
		return "<TypeDecl> on line " + lineNum;
	}
	
}
