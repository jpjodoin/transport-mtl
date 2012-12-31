///
/// \file HtmlHelpersh
/// \brief Une série de helpers pour le html 
/// \version 0.1
/// \date 2009/11/08 
///

#ifndef HTMLHELPERS_H
#define HTMLHELPERS_H

#ifndef _STRING_
#include <string>
#endif

#ifndef DATA_ARRAY_2D_H
#include "DataArray2D.h"
#endif


namespace Utils
{
namespace Html
{
	std::string HtmlDecode(const std::string &strSource);
	DataArray2D<std::string> TableauHtmlVersDataArray(const std::string &strSource);
	std::vector<DataArray2D<std::string>>  TableauHtmlVersDataArrayVector(const std::string &strSource);
	std::string RemoveHtmlTag(const std::string & strSource);
}
}

#endif