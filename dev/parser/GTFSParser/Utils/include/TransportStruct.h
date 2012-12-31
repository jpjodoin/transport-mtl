///
/// \file TransportStruct.h
/// \brief Ce fichier contient les définitions de structure des différents types
/// \version 0.1
/// \date 28 mai 2009
///
#ifndef TRANSPORT_STRUCT_H
#define TRANSPORT_STRUCT_H

#ifndef _STRING_
#include <string>
#endif


///
/// \enum eDirection
///
enum eDirection //Possibilité d'inclure d'autre type si nécessaire 
{
	UNKNOWN=0,
	NORD,
	SUD,
	OUEST,
	EST,
	CENTRE,    //Type AMT Vers le Centre Montréal
	EXTERIEUR,  //Type AMT Vers l'extérieur de Montréal
	EXTRAINFO,	//Direction is Extra Info
	ALLER, //CIT
	RETOUR //CIT

};

// Heures...Valeur de 4000 à 9999 réservé pour 5 minutes et moins etc.


#endif //TRANSPORT_STRUCT