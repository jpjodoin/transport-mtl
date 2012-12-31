///
/// \file TransportStruct.h
/// \brief Ce fichier contient les d�finitions de structure des diff�rents types
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
enum eDirection //Possibilit� d'inclure d'autre type si n�cessaire 
{
	UNKNOWN=0,
	NORD,
	SUD,
	OUEST,
	EST,
	CENTRE,    //Type AMT Vers le Centre Montr�al
	EXTERIEUR,  //Type AMT Vers l'ext�rieur de Montr�al
	EXTRAINFO,	//Direction is Extra Info
	ALLER, //CIT
	RETOUR //CIT

};

// Heures...Valeur de 4000 � 9999 r�serv� pour 5 minutes et moins etc.


#endif //TRANSPORT_STRUCT