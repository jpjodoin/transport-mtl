///
/// \file Information.h
/// \brief Déclaration de la struct Information
/// \version 0.1
/// \date 25 septembre 2010
///
#ifndef INFORMATION
#define INFORMATION

#include "Exports.h"

#include <string>
#include <sstream>
///
/// \brief Type de société de transport
///
enum TransportTypeAffichage
{
	AUTOBUS = 0,
	TRAIN = 1,
	METRO = 2,
	UNKNOWNTYPE = 3
};


struct RHATECCOREDLL_API Information
{
	std::string m_ShortName;
	std::string m_LongName;
	TransportTypeAffichage m_type;
	bool m_AffichageExtraInfo;
	bool m_AffichageRecherche;
	bool m_AffichageCarte;
	bool m_AffichageExtraInfoCircuit;
	bool m_useStopId;
	bool m_useColor; //Some are really ugly with color (aka the rainbow STL  lol)
	
	Information(std::string _shortName, std::string _longName, TransportTypeAffichage _type, bool _aExtraInfo, bool _aRecherche, bool _aCarte, bool _aExtraInfoCircuit, bool useStopId, bool useColor = true)
	: m_ShortName(_shortName)
	, m_LongName(_longName)
	, m_type(_type)
	, m_AffichageExtraInfo(_aExtraInfo)
	, m_AffichageRecherche(_aRecherche)
	, m_AffichageCarte(_aCarte)
	, m_AffichageExtraInfoCircuit(_aExtraInfoCircuit)
	, m_useStopId(useStopId)
	, m_useColor(useColor)
	{
	}

	std::string serialize() const
	{
		std::stringstream ss;
		ss<<m_ShortName<<";"<<m_LongName<<";"<<m_type<<";"<<m_AffichageExtraInfo<<";"<<m_AffichageRecherche<<";"<<m_AffichageCarte<<";"<<m_AffichageExtraInfoCircuit << ";";
		return ss.str();
	}
};



#endif