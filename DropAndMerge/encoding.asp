%Il Programma prende in input i fatti da Java ovvero: 
%-	“cell(R, C, N)” dove R è un valore tra 0 a 5 (inclusi) che indica le righe della matrice, C tra 0 e 4 (inclusi) e N indica il valore corrente della cella  (ovvero 0 se la cella è vuota o un numero).
%-	“block(N)” che indica il valore del blocco che deve essere posizionato
%-	“maxValue(V)” ovvero il valore massimo che in quel momento può assumere il blocco da posizionare
 
%Nella prima parte del codice, vediamo come vengono definiti i movimenti. Il programma stabilisce che su ogni colonna della griglia è possibile effettuare una "mossa" o non effettuare alcuna mossa, tuttavia, il programma impone che, in ogni istante, vienga effettuata esattamente una mossa, evitando situazioni in cui non ci siano mosse o ce ne siano più di una.

%%%     CONTROLLO DEI BLOCCHI SOTTOSTANTI
%Il programma controlla costantemente cosa si trova sotto un blocco prima di effettuare un movimento. La regola ‘sotto(MIN)’ identifica il blocco più in basso nella colonna di destinazione
%Questa regola individua la riga `MIN` più bassa che contiene un valore maggiore di zero, sotto la colonna in cui si è deciso di muoversi. Inoltre, una regola più generale ‘ceSotto’ viene utilizzata per sapere se esiste o meno un blocco sotto il blocco corrente:

%%%     CONTROLLO DEI BLOCCHI LATERALI
%verifica cosa si trova ai lati del blocco corrente (sinistra o destra), queste regole dicono che, se non c'è un blocco sotto, il programma cerca un blocco a sinistra o a destra nella riga 5. In presenza di un blocco sotto, le regole cambiano leggermente e controllano la riga immediatamente sopra il blocco più basso (‘MIN-1’).

%%%     DIVISIONE E MERGE DEI BLOCCHI 
%Il programma decide come dividere i valori dei blocchi in base alla loro posizione e ai valori circostanti:
%Questa regola calcola la divisione tra il blocco corrente ‘X’ e il blocco alla sua sinistra ‘Y’.  Se ‘X’ è maggiore di ‘Y’, si divide ‘X’ per ‘Y’, sottraendo 1 dal risultato. In caso contrario, si divide ‘Y’ per ‘X’, generando il valore ‘N’.
%Lo stesso approccio viene applicato per la divisione a destra e sotto, con una piccola modifica per la divisione sotto che tiene conto del valore massimo ‘V’
%Inoltre, il programma gestisce anche il ‘Merge’ dei blocchi. Se un blocco ha lo stesso valore di un altro adiacente (a sinistra, destra o sotto), i due blocchi si fondono


%%%     WEACK CONSTRAINT 
%LIV. 6 
%A questo livello applica una penalità di 1 se la mossa si trova nella riga più in alto senza la possibilità di fare il Merge, al contrario non applica nessuna penalità se è possibile fare un merge.

%LIV 5
%Cerchiamo di minimizzare le mosse che non permettono di fare il merge verticalmente, pagando una penalità di 1 se non è possibile fare il merge altrimenti 0.

%LIV 4
%Paghiamo una penalità di valore N, ovvero il valore della divisione tra il blocco che sta cadendo e il valore del blocco sotto della colonna scelta, con l’obiettivo di scegliere la colonna più conveniente.

%LIV 3
%Se vengono effettate “divisioniLaterali” a sinistra o a destra, il programma calcola il valore minimo tra tutte e applica una penalità pari a tale valore. L’obietti è di limitare divisioni laterali con valori troppo piccoli, favorendo al contrario quella con blocchi con valori più grandi.

%LIV 2
%Si cerca di sfruttare al meglio la posizione del blocco più in basso, incentivando le mosse nelle colonne meno riempite, applicando una penalità di Z dove Z è l’indice dell’ultimo blocco nella colonna meno riempita, se non c’è alcun blocco sotto non viene applicata nessuna penalità.

%LIV 1 
%A questo livello vengono gestiti i merge laterali, se avviene un merge laterale, non si riceve alcuna penalità. Al contrario, se non avviene si riceve una penalità di 1 punto. Questo incoraggia il sistema a cercare merge laterali, che potrebbero essere strategicamente vantaggiose.



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%                       GUESS                              %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
move(X)|noMove(X):-cell(_,X,_).
%deve generare almeno una mossa
:-#count{X:move(X)}<>1.

%Controllo dei Blocchi Sottostanti
sotto(MIN):-move(X), #min{Y:cell(Y,X,N),N>0}=MIN, MIN>0.
%controllo se è presente un blocco sotto
ceSotto:-bloccoSotto(_).

%Controllo dei Blocchi Laterali
bloccoSinistra(N):- not ceSotto, move(X), cell(5, X-1, N), N>0.
bloccoSinistra(N):- ceSotto, sotto(MIN), move(X), cell(Z, X-1, N), Z=MIN-1, N>0.

bloccoDestra(N):- not ceSotto, move(X), cell(5, X+1, N), N>0.
bloccoDestra(N):- ceSotto, sotto(MIN), move(X), cell(Z, X+1, N), Z=MIN-1, N>0.

bloccoSotto(N):-sotto(MIN),move(X), cell(MIN,X,N), N>0.

%Divisione e Merge dei blocchi
divisioneSinistra(N):-block(X), bloccoSinistra(Y), maxValue(V), X>Y, N=((X/Y)-1).
divisioneSinistra(N):-block(X), bloccoSinistra(Y), maxValue(V), X<=Y, N=((Y/X)-1).

divisioneDestra(N):-block(X), bloccoDestra(Y), maxValue(V), X>Y, N=((X/Y)-1).
divisioneDestra(N):-block(X), bloccoDestra(Y), maxValue(V), X<=Y, N=((Y/X)-1).

divisioneSotto(N):-block(X), bloccoSotto(Y), maxValue(V), X>Y, N=((X/Y)-1)+(V/2).
divisioneSotto(N):-block(X), bloccoSotto(Y), X<=Y, N=(Y/X)-1.

divisioniLaterali(N):-divisioneSinistra(N).
divisioniLaterali(N):-divisioneDestra(N).

mergeLaterale:-bloccoSinistra(X), block(Y), X=Y.
mergeLaterale:-bloccoDestra(X), block(Y), X=Y.
mergeSotto:-bloccoSotto(X), block(Y), X=Y. 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%                       WEACK CONSTRAINT                     %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

:~move(X), cell(1,X,N), not mergeSotto, N>0. [1@6]
:~move(X), cell(1,X,N), mergeSotto, N>0. [0@6]

:~mergeSotto. [0@5]
:~not mergeSotto. [1@5]

:~divisioneSotto(N). [N@4]

:~#min{N:divisioniLaterali(N)}=MIN. [MIN@3]

:~ceSotto, sotto(MIN), Z=MIN-1. [Z@2]
:~not ceSotto. [0@2]

:~mergeLaterale. [0@1] 
:~not mergeLaterale. [1@1]


