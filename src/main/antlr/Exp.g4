grammar Exp;

file : block EOF;

block : statement* ;

blockWithBraces : '{' block '}' ;

statement
    : function
    | variable
    | expression
    | whileExpr
    | ifExpr
    | assignment
    | returnExpr
    ;

function : 'fun' Identifier '(' parameterNames ')' blockWithBraces ;

variable : 'var' Identifier ('=' expression)? ;

parameterNames : (Identifier (',' Identifier)*)? ;

whileExpr : 'while' '(' expression ')' blockWithBraces ;

ifExpr : 'if' '(' expression ')' trueBlock=blockWithBraces ('else' falseBlock=blockWithBraces)? ;

assignment : Identifier '=' expression ;

returnExpr : 'return' expression ;

functionCall : Identifier '(' arguments ')' ;

arguments : (expression (',' expression)*)? ;

Identifier : ('A'..'Z' | 'a'..'z' | '_') ('A'..'Z' | 'a'..'z' | '_' | '0'..'9')* ;

expression
    : atomExpression
    | <assoc=left> left=expression op=('*' | '/' | '%') right=expression
    | <assoc=left> left=expression op=('+' | '-') right=expression
    | left=expression op=('>' | '>=' | '<=' | '<') right=expression
    | left=expression op=('==' | '!=') right=expression
    | left=expression op='&&' right=expression
    | left=expression op='||' right=expression
    ;

atomExpression
    : functionCall
    | Literal
    | Identifier
    | '(' expression ')'
    ;

Literal
    : ('-')? ('1'..'9') ('0'..'9')*
    | '0'
    ;

WS : (' ' | '\t' | '\r'| '\n' | '//' (.)*? ('\n' | EOF)) -> skip;
