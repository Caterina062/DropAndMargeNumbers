# DropAndMargeNumbers

![Screenshot 2024-09-13 220331](https://github.com/user-attachments/assets/18951e33-f03c-48c4-b671-a283f3b0e1a9)

Introduzione.


Drop and Merge- Numbers è un progetto implementato per il corso di Intelligenza Artificiale, che combina una strategia di gioco con l'implementazione di un intelligenza artificiale basata su Answer Set Programming (ASP), la parte grafica è stata impelmentata utilizzando  le Swing, una libreria di Java.

Regole del gioco

Il campo da gioco è formato da una matrice 5x6 celle, Si basa sulla stessa logica del gioco 2048, lo scopo è fare un punteggio più alto possibile associando blocchi dello stesso valore, i blocchi cadono dall'alto della matrice ed è proprio il giocatore a decidere in quale colonna farli cadere in modo da scegliere la soluzione migliore. Il merge dei blocchi può avvenire sia in verticale che in orizzontale, una volta fatto il merge il valore del blocco aumenterò per il doppio del suo valore. Il valore del blocco che stiamo per inserire è segnalato sinistro della matrice con "prossimo valore". Per far compiere la mossa alla nostra intelligeza basta premere sul bottone a sinistra "Mossa" prima dello scadere del tempo, altrimeni quando il tempo arriva a zero il gioco faarà cadere il blocco nella colonna dove abbiamo scelto di far cadere il blocco precedente. 
Il gioco finisce quando un blocco arriva a toccare la cima della matrice 



Technologies Used
The project makes use of various technologies, including:

Answer Set Programming (ASP): Used to implement the artificial intelligence module that makes strategic decisions for players.
Java Swing: Used for the development of the game's graphical interface.
EmbASP: Java library for integrating ASP into Java projects.
DLV2: ASP system used for executing ASP programs.
Starting the Project
To start the project, follow these steps:


immagine fine
![Screenshot 2024-09-13 220412](https://github.com/user-attachments/assets/8e78da06-e85f-4bc9-825e-3a04769dd500)


Make sure you have Java installed on your system.
Clone the repository from GitHub.
Import the project into a compatible Java IDE.
Add the two encodings in the "encodings" folder
Compile and run the project.
Enjoy playing Ghost in the Cell! 🤖🎮
