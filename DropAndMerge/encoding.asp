% tra i fatti che compongono il gioco, passiamo tramite Java, cell(R, C, N), dove R è il numero delle righe che va da 0 a 5, C il numero delle colonne che va da 0 a 4 e 
% infine N che indica il valore della cella, zero se vuota un numero altrimenti
% poi passiamo il valore del blocco che sta cadendo con block(N) e infine passiamo il valore massimo che possiamo avere in quel momento come valore del blocco che sta 
% cadendo con maxValue(N)

%                                                           WEACK CONTRAINT
% LIV 5 all'ultimo livello essendo quello più importante controlliamo che il blocco non cada nell'ultimo rigo della matrice, se non ha la possibilità di fare merge con 
%       un blocco sotto, facendo così finire il gioco perdendo 
% LIV 4 in questo livello sceglialo la mossa migliore da fare in base se è possibile o meno fare il merge con un blocco che si trova sotto 
% LIV 3
% LIV 4
% LIV 5
% LIV 6

%guess
move(X)|noMove(X):-cell(_,X,_).
%deve generare almeno una mossa
:-#count{X:move(X)}<>1.

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
%:~bloccoSotto(X), block(Y), X>=Y*2. [0@4] %preferire le caselle con il valore del blocco che cade *2, dovrebbe già farlo con quella sotto il resto N+ 1 in modo che non preferisca dove non c'è niente se ho un valore diviso due dove poter mettere il quadratino che sta cadendo così da preferirlo in modo da fare una pila più alta e quando fa il merge fa tanti merge di fila
:~divisioneSotto(N). [N@4]
%controllo divisioni laterali
%...
:~#min{N:divisioniLaterali(N)}=MIN. [MIN@3]

%preferisco la colonna più bassa
:~ceSotto, sotto(MIN), Z=MIN-1. [Z@2]
:~not ceSotto. [0@2]

%per ultimo preferisco fare il merge laterale piuttosto che niente, controllare il doppio sotto, se lo è meglio
:~mergeLaterale. [0@1] %, bloccoSotto(X), block(Y), X>Y*2
:~not mergeLaterale. [1@1]


%valore x*2 lo preferisco
%:~block(X), bloccoSotto(Y), move(N), X=Y*2. [0@..]
%:~block(X), bloccoSotto(Y), move(N), X<>Y*2. [1@ ..]


%merge doppio ad L (forse non 5)
%:~move(X), bloccoSotto(Y), block(N), Y<=(N*2). [2@5]
%:~move(X), bloccoSotto(Y), bloccoSinistra(Z), block(N), N=Z, N>Y, Y>N*2. [0@5]
%:~move(X), bloccoSotto(Y), bloccoSinistra(Z), block(N), N<>Z.[1@5]
%:~move(X), bloccoSotto(Y), bloccoDestra(Z), block(N), N=Z, N>Y, Y>N*2. [0@5]
%:~move(X), bloccoSotto(Y), bloccoDestra(Z), block(N), N<>Z.[1@5]





%preferire se non ho merge quidni a livello 4? preferire metterlo sul quadratino di valore blocco*2 se c'è altrimenti continuare  afre come fa adesso, quindi aggiungere un livello
%provare a fare la regola del merge a tre, quindi controllare se in caso posso fare il merge per tre di avere il valore sotto la casella che farà il merge del valore del blocco per tre almeno, oppure etc aaltri casi facendo costo alto se per caso abbiamo un merg a tre e sotto abbiamo il doppio del blocco e merg basso probabilmente zero se invece abbiamo numeri molto più grandi
%vedere se possibile fare il merge doppio, quindi scegliere le colonne dove dopo aver fatto il primo merge possiamo farlo ricorsivamente, controllando però che non ci siano merge laterali che alterano il valore del blocco che fa il merge

%:~bloccoSotto(X), block(Y), X=Y*2. [0@4]
%:~bloccoSotto(X), block(Y), X<>Y*2. [1@4]

%%liv1 come ulrimo controllo più ttosto che metterlo a caso preferisco fare i merge laterali
%%liv2 preferisco la collonna più bassa altrimenti quella più a sinistra
%%liv3 se sono tutti più piccoli i valori nella matrice allora cade su quello più grande
%%liv4 cade sulla blocco con il miglior punteggio, quindi o quello più grande
%%...nuovo liv4... preferire cadere su i blocchi che hanno il valore del blocco che sta cadndo per due se possibile  ...........
%%liv5 controlla se può fare il merge sotto, se può farlo sicuramente cadre li
%%liv6 priorità assoluta al merge sotto, non deve arriavre in cima, non metterlo mai in cima, ma se può fare il merge lo mette
%%liv7






%256|     preferisco metterla su 32 perché c'è possibilità che io somma due 16 e faccia un 32 così facendo il merge il quadratino si sposta
%32 | 16
%controllo unico se per caso si trova al primissimo rigo e abbiaqmo la possibilità di fare merge dobbio con laterale
%controllo che se il numero sotto è più grande di me per tre allora metterlo li altrimenti no, oppure se è vuoto quidni non c'è niente sotto sono all'ultima riga, (questo per fare il merge a tre)
%prevedere il caso in cui è ad L 

