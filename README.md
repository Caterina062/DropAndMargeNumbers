# DropAndMargeNumbers

![Screenshot 2024-09-13 220331](https://github.com/user-attachments/assets/18951e33-f03c-48c4-b671-a283f3b0e1a9)


Introduzione.


Drop and Merge-Numbers è un progetto implementato per il corso di Intelligenza Artificiale, che combina una strategia di gioco con l'implementazione di un intelligenza artificiale basata su Answer Set Programming (ASP), la parte grafica è stata impelmentata utilizzando le Swing, una libreria di Java.


Regole del gioco

Il campo da gioco è formato da una matrice 5x6 celle, Si basa sulla stessa logica del gioco 2048, lo scopo è fare un punteggio più alto possibile associando blocchi dello stesso valore, i blocchi cadono dall'alto della matrice ed è proprio il giocatore a decidere in quale colonna farli cadere in modo da scegliere la soluzione migliore. Il merge dei blocchi può avvenire sia in verticale che in orizzontale, una volta fatto il merge il valore del blocco aumenterò per il doppio del suo valore. Il valore del blocco che stiamo per inserire è segnalato sinistro della matrice con "prossimo valore". Per far compiere la mossa alla nostra intelligeza basta premere sul bottone a sinistra "Mossa" prima dello scadere del tempo, altrimeni quando il tempo arriva a zero il gioco faarà cadere il blocco nella colonna dove abbiamo scelto di far cadere il blocco precedente. 
Il gioco finisce quando un blocco arriva a toccare la cima della matrice.
Quando si perde il gioco mostra a schermo i tre punteggi più alti raggiunti nelle giocate precedenti con "best score" e il nostro punteggio "your score".



Tecnologie usate

Nel progetto vengono utilizzati varie tecnologie, icluse:

Answer Set Programming (ASP): Utilizzato per implementare l'intelligenza artificiale per scegliere la mossa migliore da fare.
Java Swing: Usata per la parte dell'interfaccia grafica.
EmbASP: Libreria Java per interagire con ASP nel progetto Java.
DLV2: ASP usato per l'esecuzione del programma ASP.




![Screenshot 2024-09-13 220412](https://github.com/user-attachments/assets/8e78da06-e85f-4bc9-825e-3a04769dd500)



