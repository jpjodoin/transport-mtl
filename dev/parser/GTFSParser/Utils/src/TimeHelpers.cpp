///
/// \file TimeHelpers.cpp
/// \version 0.1
/// \date 28 mai 2009
///
#include "TimeHelpers.h"
#include <assert.h>
#include <fstream>
#include "FichierHelpers.h"
#include "StringHelpers.h"

namespace Utils
{
/// 
/// * Prend en entrée une string tel que "3:23 "  et la convertis en 0323
/// \param[in]	strTime			Temps sous la forme "3:23"
/// \return						Temps sous la forme interne
/// 
std::string Time::StringToInternalFormat(std::string strTime)
{
	strTime=String::RemoveString(strTime, " ");

	// Enlever les espaces au debut et a la fin du string
	strTime=String::EnleveEspaceDebutFin(strTime);

	std::string strConvertedData;

	int nSeparatorPos;
	//Convertir de 16h42  ---> 1642
	strTime=String::Replace(strTime, "h", ":");

	//08:16:12 vers -> 08:16 (Le jour où les sociétés de transport seront précise aux seconde, on en reparlera :P)
	if(Utils::String::Count(strTime, ':', 0) == 2)
	{
		int index = strTime.find_last_of(":");
		assert(index > 0);
		strTime = strTime.substr(0, index);
	}


	// Convertir de 16:42 ---> 1642
	if((nSeparatorPos=strTime.find(":",0))!=-1 && strTime.length()<6)
	{
		if(nSeparatorPos==1)
		{
			strConvertedData="0";
			strConvertedData.append(strTime, 0, 1 );
			strConvertedData.append(strTime, 2, 2 );
		}
		
		else if(nSeparatorPos==2)
		{
			strConvertedData.append(strTime, 0, 2 );
			strConvertedData.append(strTime, 3, 2 );
		}
		else
			assert(false);

		while(strConvertedData.size()<4)
			strConvertedData="0"+strConvertedData;
	}
	else
	{
		strConvertedData=strTime;
		std::fstream fLog;
		Utils::IO::Fichier::OuvrirEcritureLectureFin("logtimerhelpers.txt","\\",fLog);
		fLog<<strConvertedData<<std::endl;
		fLog.close();

	}

	return strConvertedData;
}

/// 
/// \brief Fonction qui rajoute les codes nécessaire au bitmask
/// \param[in]			nTime		Contient l'heure à rajouter
/// \param[in]			nTimeCode	Struct qui défini les paramètres utilisés 
/// \return				nTime		nombre avec les codes rajoutés
///
unsigned int Time::TimeToInternalFormatWithCode(unsigned int nTime, TimeCode nTimeCode)
{	
	nTime=nTimeCode.fHandicape?nTime+0x1000:nTime;		//bit 13
	nTime=nTimeCode.fExtraInfoHoraire?nTime+0x2000:nTime;		//bit 14
	nTime=nTimeCode.fLundi?nTime+0x4000:nTime;	//bit 15
	nTime=nTimeCode.fMardi?nTime+0x8000:nTime;		//bit 16
	nTime=nTimeCode.fMercredi?nTime+0x10000:nTime;	//bit 17
	nTime=nTimeCode.fJeudi?nTime+0x20000:nTime;		//bit 18
	nTime=nTimeCode.fVendredi?nTime+0x40000:nTime;	//bit 19
	nTime=nTimeCode.fSamedi?nTime+0x80000:nTime;	//bit 20
	nTime=nTimeCode.fDimanche?nTime+0x100000:nTime;	//bit 21
	return nTime;
}

boost::posix_time::ptime Time::StringToPtime(unsigned int time)
{
	unsigned int minutes = time%100;
	unsigned int hours = (time-minutes)/100;
	boost::posix_time::ptime boostTime(boost::posix_time::min_date_time);
	boostTime+=boost::posix_time::hours(hours);
	if(hours<2)
		boostTime+=boost::posix_time::hours(24);
	boostTime+=boost::posix_time::minutes(minutes);
	return boostTime;
}
boost::posix_time::ptime Time::StringToPtime( std::string strTime )
{	
	//TODO: Ce code est probablement bcp trop compliqué pour rien :P

	strTime=String::RemoveString(strTime, " ");

	// Enlever les espaces au debut et a la fin du string
	strTime=String::EnleveEspaceDebutFin(strTime);

	boost::posix_time::ptime time(boost::posix_time::min_date_time);

	int nSeparatorPos;
	//Convertir de 16h42  ---> 1642
	strTime=String::Replace(strTime, "h", ":");
	strTime=Utils::String::RemoveString(strTime, "\r");
	strTime=Utils::String::RemoveString(strTime, "\n");
	strTime=Utils::String::RemoveString(strTime, "\t");
	


	// Convertir de 16:42 ---> 1642
	if((nSeparatorPos=strTime.find(":",0))!=-1 && strTime.length()<6)
	{
		unsigned int nHour=0;
		unsigned int nMinute=0;
		if(nSeparatorPos==1)
		{
			if(String::StringToUInt(strTime.substr(0,1), nHour) && String::StringToUInt(strTime.substr(2,2), nMinute))
			{
				time+=boost::posix_time::hours(nHour);
				if(nHour<2)
					time+=boost::posix_time::hours(24);
				time+=boost::posix_time::minutes(nMinute);
			}
		}
		else if(nSeparatorPos==2)
		{
			if(String::StringToUInt(strTime.substr(0,2), nHour) && String::StringToUInt(strTime.substr(3,2), nMinute))
			{
				time+=boost::posix_time::hours(nHour);
				if(nHour<2)
					time+=boost::posix_time::hours(24);
				time+=boost::posix_time::minutes(nMinute);
			}
		}
		else
			assert(false);
	}
	else
	{
		assert(false);
	}

	return time;
}


///
/// \brief Cette fonction convertis du format 4h PM vers 16h
/// \param[in]		nTime		Le temps en 12h
/// \param[in]		fIsPM		En apres-midi?
/// \return						Le temps dans la forme voulu
///
unsigned int Time::TwelveHourToInternational(unsigned int nTime ,bool fIsPM)
{
	return fIsPM?nTime+1200:nTime;
}

}