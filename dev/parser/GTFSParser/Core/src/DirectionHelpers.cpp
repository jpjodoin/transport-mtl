#include "DirectionHelpers.h"
namespace Utils
{
	///
	/// \brief Fonction qui converti une eDirection vers un char.
	/// \param[in]		oDirection	Direction à convertir.
	/// \return			Le caratère de la direction.
	///
	char String::DirectionToChar(eDirection oDirection)
	{
		char cDirection;
		switch(oDirection)
		{
		case NORD:
			cDirection='n';
			break;
		case OUEST:
			cDirection='o';
			break;
		case EST:
			cDirection='e';
			break;
		case SUD:
			cDirection='s';
			break;
		case CENTRE:
			cDirection='c';
			break;
		case EXTERIEUR:
			cDirection='x';
			break;
		case EXTRAINFO:
			cDirection='a';
			break;
		default:
			cDirection=' ';
			//Log Error
		}
		return cDirection;
	}

	///
	/// \brief Fonction qui converti une string de direction vers un eDirection.
	/// \param[in]		direction	Direction à convertir.
	/// \return			eDirection contenant la direction.
	///
	eDirection String::DirectionStringToChar(const std::string &direction)
	{
		eDirection Direction;


		if (direction =="nord")
			Direction=NORD;
		else if(direction == "ouest")
			Direction=OUEST;
		else if (direction =="est")
			Direction=EST;
		else if(direction == "sud")
			Direction=SUD;
		else if (direction =="centre")
			Direction=CENTRE;
		else if(direction == "exterieur")
			Direction=EXTERIEUR;
		else if(direction == "extrainfo")
			Direction=EXTRAINFO;
		else{
			Direction=UNKNOWN;
			//LogError
		}	

		return Direction;
	}
	///
	/// \brief Fonction qui inverse la direction.
	/// Ex: Nord -> Sud, Est->Ouest et vice-versa.
	/// \param[in]		oDirection	Direction à convertir.
	/// \return			Le caratère de la direction.
	///
	eDirection String::InverserDirection(eDirection oDirection)
	{
		eDirection Direction;
		switch(oDirection)
		{
		case NORD:
			Direction=SUD;
			break;
		case OUEST:
			Direction=EST;
			break;
		case EST:
			Direction=OUEST;
			break;
		case SUD:
			Direction=NORD;
			break;
		case CENTRE:
			Direction=EXTERIEUR;
			break;
		case EXTERIEUR:
			Direction=CENTRE;

		case EXTRAINFO:
			Direction=EXTRAINFO;
			break;
		default:
			Direction=UNKNOWN;
			//LogError

		}
		return Direction;
	}
	///
	/// \brief Fonction qui converti un char en direction
	/// \param[in]		cDirection	Direction à convertir.
	/// \return			Le eDirection.
	///
	eDirection String::CharToDirection(char cDirection)
	{
		eDirection Direction;
		switch(cDirection)
		{
		case 'N':
			Direction=NORD;
			break;
		case 'O':
			Direction=OUEST;
			break;
		case 'E':
			Direction=EST;
			break;
		case 'S':
			Direction=SUD;
			break;
		case 'C':
			Direction=CENTRE;
			break;
		case 'X':
			Direction=EXTERIEUR;
			break;
		case 'A':
			Direction=EXTRAINFO;
			break;

		default:
			Direction=UNKNOWN;
			//LogError

		}
		return Direction;


	}


}
