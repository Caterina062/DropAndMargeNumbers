%guess
move(X)|noMove(X):-cell(_,X,_).
%deve generare almeno una mossa
:-#count{X:move(X)}<>1.
%non deve arrivare in cima se può fare altre mosse

%:~move(X), cell(0,X,_). [3000@10]

%prendo indice di riga per sotto e lati
sotto(MIN):-move(X), #min{Y:cell(Y,X,N),N>0}=MIN, MIN>0.
%controllo se il blocco è alla prima riga(indice 5), per essere safe.

ceSotto:-bloccoSotto(_).

%valore blocco sinistra, con la prima regola controllo la riga indice 5, e con la secodna le altre righe
bloccoSinistra(N):- not ceSotto, move(X), cell(5, X-1, N), N>0.
bloccoSinistra(N):- ceSotto, sotto(MIN), move(X), cell(Z, X-1, N), Z=MIN-1, N>0.
%valore blocco destra
bloccoDestra(N):- not ceSotto, move(X), cell(5, X+1, N), N>0.
bloccoDestra(N):- ceSotto, sotto(MIN), move(X), cell(Z, X+1, N), Z=MIN-1, N>0.
%valore del blocco sotto
bloccoSotto(N):-sotto(MIN),move(X), cell(MIN,X,N), N>0.


%il blocco che cade ha valore maggiore del blocco sotto (livello tre perché meglio cadere su quelli più grandi)
%:~block(X), bloccoSotto(Y),X>Y. [X-Y@3, X, Y]

%il blocco che cade ha valore minore del blocco sotto
%:~block(X), bloccoSotto(Y),Y>X. [Y-X@2, X, Y]


divisioneSinistra(N):-block(X), bloccoSinistra(Y), maxValue(V), X>Y,N=((X/Y)-1).
divisioneSinistra(N):-block(X), bloccoSinistra(Y), maxValue(V), X<=Y, N=((Y/X)-1).

divisioneDestra(N):-block(X), bloccoDestra(Y), maxValue(V), X>Y, N=((X/Y)-1).
divisioneDestra(N):-block(X), bloccoDestra(Y), maxValue(V), X<=Y, N=((Y/X)-1).

divisioneSotto(N):-block(X), bloccoSotto(Y), maxValue(V), X>Y, N=((X/Y)-1)+(V/2).
divisioneSotto(N):-block(X), bloccoSotto(Y), X<=Y, N=(Y/X)-1.

divisioniLaterali(N):-divisioneSinistra(N).
divisioniLaterali(N):-divisioneDestra(N).
%divisioni(N):-divisioneSotto(N).

%vedo se posso fare il merge, solo il blocco sotto, preferisco quello sotto perché quello laterale dipende dai i casi in cui mi trovo
mergeLaterale:-bloccoSinistra(X), block(Y), X=Y.
mergeLaterale:-bloccoDestra(X), block(Y), X=Y.
mergeSotto:-bloccoSotto(X), block(Y), X=Y. 

%non deve mai arrivare in cima se può evitarlo
:~move(X), cell(1,X,N), not mergeSotto, N>0. [1@6]
:~move(X), cell(1,X,N), mergeSotto, N>0. [0@6]

%se è possibile fare il merge sotto preferiscilo 
:~mergeSotto. [0@5]
:~not mergeSotto. [1@5]

%decide dove cadere
:~divisioneSotto(N). [N@4]
:~#min{N:divisioniLaterali(N)}=MIN. [MIN@3]

%preferisco la colonna più bassa
:~ceSotto, sotto(MIN), Z=MIN-1. [Z@2]
:~not ceSotto. [0@2]

%per ultimo preferisco fare il merge laterale piuttosto che niente
:~mergeLaterale. [0@1]
:~not mergeLaterale. [1@1]