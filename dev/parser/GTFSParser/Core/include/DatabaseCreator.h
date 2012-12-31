///
/// \file DatabaseCreator.h
/// \brief Déclaration de la classe DatabaseCreator
/// \version 0.1
/// \date 28 mai 2009
///
#ifndef DATABASE_CREATOR_H
#define DATABASE_CREATOR_H

#include "Exports.h"
#include "Information.h"
#include "Circuit.h"
#include "DirectionHelpers.h"
class TransportService;
class ExtraInfoHelpers;


///
/// \brief Différent état de parsing..
///
enum eParsingState
{
	PARSING_STATE_UNKNOWN=0,
	PARSING_STATE_LISTE_CIRCUIT,
	PARSING_STATE_DIRECTION,
	PARSING_STATE_LISTE_ARRET,
	PARSING_STATE_HORAIRE_ARRET,
	PARSING_STATE_HORAIRE_ARRET_SEMAINE,
	PARSING_STATE_HORAIRE_ARRET_SAMEDI,
	PARSING_STATE_HORAIRE_ARRET_DIMANCHE,
	PARSING_STATE_INFO_PARSING,
	PARSING_STATE_REQUEST_MAKER,
	PARSING_STATE_INFO_PARSING_BUS,
	PARSING_STATE_REQUEST_MAKER_BUS,
	PARSING_STATE_NOPARSING,
	PARSING_STATE_TRACER

	//Nouveau état de parsing ici....
};

class RHATECCOREDLL_API DatabaseCreator
{
public:
	DatabaseCreator(Information transitInformation);
	virtual ~DatabaseCreator();

	virtual void Initialize();
	virtual void GetData()=0;	
	virtual void RxData(const std::string& strBuf)=0;
	void SaveDatabase();

	void SetParsingState(eParsingState eState);
	eParsingState GetParsingState();

protected:
	TransportService* m_TransportService;
	ExtraInfoHelpers* m_ExtraInfoCircuitHelpers;
	ExtraInfoHelpers* m_ExtraInfoDirectionHelpers;
	ExtraInfoHelpers* m_ExtraInfoHoraireHelpers;

private:
	eParsingState m_eState;
};

//Fonction inline
//------------------------------------------------------------------------
inline void DatabaseCreator::SetParsingState(eParsingState eState)
{
	m_eState=eState;
}
//------------------------------------------------------------------------
inline eParsingState DatabaseCreator::GetParsingState()
{
	return m_eState;
}

///
/// \brief Fonction qui retourne true si le # de circuit de cCircuit2 est plus grand que cCircuit1
/// \param[in]		cCircuit1		Circuit 1
/// \param[in]		cCircuit2		Circuit 2
/// \return True si Circuit 2 est plus grand que 1
///
struct AscendingCircuitSort
{
	bool operator()(Circuit* cCircuit1, Circuit* cCircuit2)
	{
		return ( cCircuit1->ObtenirNumero() < cCircuit2->ObtenirNumero() );
	}
};

struct AscendingHoraireDateSort
{
	bool operator()(Horaire* cHoraire1, Horaire* cHoraire2)
	{
		return (cHoraire1->ObtenirNom() < cHoraire2->ObtenirNom());
	}
};



#endif