% definire matrice
Cols(0..4).
Rows(0..5).
Value(2..131072) :- 2*2^N, N = 0..16.
block(C,R,V):- Cols(C), Rows(R), Value(V).
% definire regole



