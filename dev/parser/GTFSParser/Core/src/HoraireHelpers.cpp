#include "HoraireHelpers.h"
#include "StringHelpers.h"
#include "HTMLHelpers.h"
#include <iostream>
#include <fstream>
#include "FichierHelpers.h"
#include <assert.h>
#include "Logger.h"
namespace Utils
{

HoraireHelpers::HoraireHelpers( std::string _nomFichierJourFerie )
: m_NomFichierJour(_nomFichierJourFerie)
{
	ChargerFichierJourFeries(_nomFichierJourFerie);
}

bool HoraireHelpers::VerifyDateExist(unsigned int horaireName)
{
	bool success = false;
	std::map<unsigned int, std::string>::iterator it;
	if(horaireName < LAST)  //((int)LUNDI+(int)MARDI+(int)MERCREDI+(int)JEUDI+(int)VENDREDI+(int)SAMEDI+(int)DIMANCHE)/*== SEMAINE || horaireName == SAMEDI || horaireName == DIMANCHE*/
	{
		success = true; 
	}
	else if ((it = m_DateNameDbMap.find(horaireName)) != m_DateNameDbMap.end())
	{
		m_UsedHolidayNameMap[it->first] = it->second; 
		success = true;
	}
	else
	{
		
		m_DateNameDbMap[horaireName] = Utils::String::toString(horaireName);
		m_UsedHolidayNameMap[horaireName] = Utils::String::toString(horaireName);
		LOGERROR("Schedule " << horaireName << " is not part of the holiday database.");
	}
	return success;
}

void HoraireHelpers::ResetUsedHolidayMap()
{
	m_UsedHolidayNameMap.clear();
}

std::string HoraireHelpers::GetHoraireNameToCodeList()
{
	std::string strReturn;
	std::map<unsigned int, std::string>::iterator iter=m_UsedHolidayNameMap.begin();
	while(iter!=m_UsedHolidayNameMap.end())
	{			
		strReturn.append(Utils::String::toString((*iter).first)+";"+(*iter).second+";\n");
		++iter;
	}
	return strReturn;
}

bool HoraireHelpers::ChargerFichierJourFeries(std::string _nomFichier)
{
	std::ifstream fileStream;
	while(!Utils::IO::Fichier::OuvrirFichierLecture(_nomFichier, "", fileStream))
	{
		LOGERROR("fetedb.txt not found... Press any key to try again");
		fileStream.close();
		std::string dummy;
		std::getline(std::cin, dummy);
	}


	std::string line;	
	while(std::getline(fileStream, line) && line!="")
	{
		StringArray namedatearray = Utils::String::Split(line, ";");
		if(!(namedatearray.size() == 2))
		{
			LOGERROR("fetedb.txt is not formatted correctly...");
			assert(false); 
		}
		unsigned int date;
		if(Utils::String::StringToUInt(namedatearray[0], date))
			m_DateNameDbMap[date]=namedatearray[1];
		else
			LOGERROR(namedatearray[0] << " is an invalid date format...");
	}
	fileStream.close();
	return true;
	
}
}
