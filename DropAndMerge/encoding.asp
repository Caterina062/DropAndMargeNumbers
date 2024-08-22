% Una cella non può contenere più di un numero alla volta
%cell(0,0,0). cell(0,1,0). cell(0,2,0). cell(0,3,0). cell(0,4,0).
%cell(1,0,0). cell(1,1,0). cell(1,2,0). cell(1,3,0). cell(1,4,0).
%cell(2,0,0). cell(2,1,0). cell(2,2,0). cell(2,3,0). cell(2,4,0).
%cell(3,0,0). cell(3,1,0). cell(3,2,0). cell(3,3,0). cell(3,4,0).
%cell(4,0,0). cell(4,1,0). cell(4,2,0). cell(4,3,0). cell(4,4,0).
%cell(5,0,0). cell(5,1,0). cell(5,2,0). cell(5,3,0). cell(5,4,0).
%block(2).


move(X)|noMove(X):-cell(_,X,_).
:-move(X), cell(1,X,N), N>0.

bloccoSotto(N):-sotto(MIN),move(X), cell(MIN,X,N), N>0.

%trovare l'indice riga blocco sotto
sotto(MIN):-move(X), #min{Y:cell(Y,X,N),N>0}=MIN, MIN>0.

:~move(X). [X@1,X]

%deve generare almeno una mossa
vuoto:-#count{X:move(X)}=0.
:-vuoto.

%non generare due mosse diverse
:-move(X), move(Y), X!=Y.

:~block(X), bloccoSotto(Y),X>Y. [X-Y@2, X, Y]
:~block(X), bloccoSotto(Y),Y>X. [Y-X@2, X, Y]