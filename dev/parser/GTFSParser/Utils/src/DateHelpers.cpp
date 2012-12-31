///
/// \file DateHelpers.cpp
/// \brief Déclaration de la classe DatabaseCreator
/// \version 0.1
/// \date 28 mai 2009
///
#include "DateHelpers.h"
#include <assert.h>
#include <time.h>

namespace Utils
{
namespace Date
{
	///
	/// \todo utiliser les fonction pour mettre en minuscule le string entrant et la fonction qui enleve les espace pour s'assurer du bon fonctionnement
	/// \brief Prends une date du format "18 juin" vers le format "1806" utilisé à l'interne
	/// \param[in]		strDate		Date textuelle ex: "janvier"
	/// \return						Retourne une valeur dans le format internet
	std::string DateTextuelleVersFormatInterne(std::string strDate)
	{
		std::string strDateInterne("");
		int nIndex=strDate.find(" ");
		std::string strChiffre=strDate.substr(0, nIndex);
		std::string strMois=strDate.substr(nIndex+1, strDate.size()-(nIndex+1));
		strDateInterne+=strChiffre;
		if(strMois=="janvier"  || strMois=="Janvier")
			strDateInterne+="01";
		else if(strMois=="février" || strMois=="Février" )
			strDateInterne+="02";
		else if(strMois=="mars" || strMois=="Mars")
			strDateInterne+="03";
		else if(strMois=="avril" || strMois=="Avril")
			strDateInterne+="04";
		else if(strMois=="mai" || strMois=="Mai")
			strDateInterne+="05";
		else if(strMois=="juin" || strMois=="Juin")
			strDateInterne+="06";
		else if(strMois=="juillet" || strMois=="Juillet")
			strDateInterne+="07";
		else if(strMois=="août" || strMois=="Août")
			strDateInterne+="08";
		else if(strMois=="septembre" || strMois=="Septembre")
			strDateInterne+="09";
		else if(strMois=="octobre" ||  strMois=="Octobre")
			strDateInterne+="10";
		else if(strMois=="novembre" || strMois=="Novembre")
			strDateInterne+="11";
		else if(strMois=="décembre" || strMois=="Décembre")
			strDateInterne+="12";
		else
		{
			strDateInterne+="00";
			//Log Error
		}
		return strDateInterne;

	}

	///
	/// \brief Permet d'avoir un numéro de version
	/// \return			Retourne un numéro de version du type: "vYYYYMMDD"
	///
	std::string ObtenirNumeroVersion()
	{
		time_t rawtime;	
		time(&rawtime);
		struct tm * timeinfo=localtime(&rawtime);
		char timebuffer[10];
		strftime (timebuffer,10,"%Y%m%d",timeinfo);
		return std::string(timebuffer);		
	}


}
}