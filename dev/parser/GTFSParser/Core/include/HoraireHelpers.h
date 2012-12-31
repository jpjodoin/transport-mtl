///
/// \file HoraireHelpers.h
/// \brief Déclaration de la classe qui convertis des noms d'horaire en code
/// \version 0.1
/// \date 16 décembre 2009
///

#ifndef HORAIREHELPERS_H
#define HORAIREHELPERS_H

#include "Exports.h"

#ifndef _STRING_
#include <string>
#endif

#ifndef _MAP_
#include <map>
#endif 

#ifndef _LIST_
#include <list>
#endif 
namespace Utils
{
	class RHATECCOREDLL_API HoraireHelpers
	{
	public:
		///
		/// \enum eHoraireType
		///
		enum eHoraireType
		{
			LUNDI = 1,
			MARDI = 2,
			MERCREDI = 4,
			JEUDI = 8,
			VENDREDI = 16,
			SEMAINE =LUNDI + MARDI + MERCREDI + JEUDI + VENDREDI,
			SAMEDI = 32,
			DIMANCHE = 64,
			JOURFERIE = 128,
			/*INCONNU=0,
			SEMAINE=1,
			SAMEDI=2,
			DIMANCHE=3,
			FINDESEMAINE=4,

			JOURFERIE,*/
			LAST
		};

		HoraireHelpers(std::string _nomFichierJourFerie);
		bool VerifyDateExist(unsigned int horaireName);

		void ResetUsedHolidayMap();
		std::string GetHoraireNameToCodeList();

	private:

		bool ChargerFichierJourFeries(std::string _nomFichier);
		std::string m_NomFichierJour;
		std::map<unsigned int, std::string> m_DateNameDbMap;
		std::map<unsigned int, std::string> m_UsedHolidayNameMap;
	};
}




#endif

