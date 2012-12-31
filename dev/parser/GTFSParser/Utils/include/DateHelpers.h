///
/// \file DateHelpers.h
/// \brief Déclaration de la classe DatabaseCreator
/// \version 0.1
/// \date 28 mai 2009
///
#ifndef DATEHELPERS_H
#define DATEHELPERS_H


#ifndef _STRING_
#include <string>
#endif

namespace Utils
{
namespace Date
{
	std::string DateTextuelleVersFormatInterne(std::string strDate);
	std::string ObtenirNumeroVersion();
}
}


#endif //DATEHELPERS