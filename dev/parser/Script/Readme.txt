----------------------------Fran�ais---------------------------
Le dossier GTFSParser contient la solution Visual Studio 2010 pour le parser C++ de Transport Montr�al. Cette partie est responsable d'une grande partie de la conversion entre le format GTFS et notre propre format mobile. Le dossier Script contient des scripts pythons 2.7 permettant de comparer la date des GTFS d�ploy� avec ceux sur les site web des soci�t�s de transport. Si la date est diff�rente, on r�cup�re le fichier et on effectue une conversion du format � l'aide du programme GTFSParsingServer.exe et des bons arguments. Le dossier conf contient les diff�rentes Soci�t� de transport support�. Le script principale est GTFSConversionServer.py.

----------------------------English----------------------------
The folder GTFSParser contains the C++ parser solution for Visual Studio 2010 of Transport Montr�al. 
This part of the project is responsible of most of the conversion between GTFS and our own mobile data format. The provided Script folder contains Python 2.7 script to compare the currently deployed date of the database to the one on the website. If the date of the GTFS file on the website is different, we download them, unzip them and launch the parser with the correct arguments. The conf folder contains configuration for each transport society. The main runner script is GTFSConversionServer.py.

