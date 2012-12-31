///
/// \file HTMLHelperscpp
/// \brief Implementation file for HTML manipulation
/// \version 0.1
/// \date 2009/11/08 
///

#include "HTMLHelpers.h"
#include "StringHelpers.h"
#include <boost/regex.hpp>

namespace Utils
{
namespace Html
{

	///
	/// \brief Fonction qui remplace les encodages html
	/// \param[in]		strSource		String source
	/// \return							La string strSource sans les encodages html
	///
	std::string HtmlDecode(const std::string &strSource)
	{
		std::string strReturn(strSource);
		if(Utils::String::Contains(strReturn, ";")) //TODO:remplacer par Regex avec & ;
		{
			//Décodage des principaux caractère de la langue française
			//Ajout au besoin
			//http://www.ascii.cl/htmlcodes.htm
			strReturn=Utils::String::Replace(strReturn, "&agrave;", "à");
			strReturn=Utils::String::Replace(strReturn, "&aacute;", "á");
			strReturn=Utils::String::Replace(strReturn, "&acirc;", "â");
			strReturn=Utils::String::Replace(strReturn, "&auml;", "ä");
			strReturn=Utils::String::Replace(strReturn, "&egrave;", "è");
			strReturn=Utils::String::Replace(strReturn, "&eacute;", "é");
			strReturn=Utils::String::Replace(strReturn, "&ecirc;", "ê");
			strReturn=Utils::String::Replace(strReturn, "&euml;", "ë");
			strReturn=Utils::String::Replace(strReturn, "&igrave;", "ì");
			strReturn=Utils::String::Replace(strReturn, "&iacute;", "í");
			strReturn=Utils::String::Replace(strReturn, "&icirc;", "î");
			strReturn=Utils::String::Replace(strReturn, "&iuml;", "ï");
			strReturn=Utils::String::Replace(strReturn, "&ograve", "ò");
			strReturn=Utils::String::Replace(strReturn, "&oacute;", "ó");
			strReturn=Utils::String::Replace(strReturn, "&ocirc;", "ô");
			strReturn=Utils::String::Replace(strReturn, "&ouml;", "ö");
			strReturn=Utils::String::Replace(strReturn, "&ugrave", "ù");
			strReturn=Utils::String::Replace(strReturn, "&uacute;", "ú");
			strReturn=Utils::String::Replace(strReturn, "&ucirc;", "û");
			strReturn=Utils::String::Replace(strReturn, "&uuml;", "ü");
			strReturn=Utils::String::Replace(strReturn, "&ccedil;", "ç");
			strReturn=Utils::String::Replace(strReturn, "&nbsp;", " ");
			
			//Version Majuscule
			strReturn=Utils::String::Replace(strReturn, "&Agrave;", "À");
			strReturn=Utils::String::Replace(strReturn, "&Aacute;", "Á");
			strReturn=Utils::String::Replace(strReturn, "&Acirc;", "Â");
			strReturn=Utils::String::Replace(strReturn, "&Auml;", "Ä");
			strReturn=Utils::String::Replace(strReturn, "&Egrave;", "È");
			strReturn=Utils::String::Replace(strReturn, "&Eacute;", "É");
			strReturn=Utils::String::Replace(strReturn, "&Ecirc;", "Ê");
			strReturn=Utils::String::Replace(strReturn, "&Euml;", "Ë");
			strReturn=Utils::String::Replace(strReturn, "&Igrave;", "Ì");
			strReturn=Utils::String::Replace(strReturn, "&Iacute;", "Í");
			strReturn=Utils::String::Replace(strReturn, "&Icirc;", "Î");
			strReturn=Utils::String::Replace(strReturn, "&Iuml;", "Ï");
			strReturn=Utils::String::Replace(strReturn, "&Ograve", "Ò");
			strReturn=Utils::String::Replace(strReturn, "&Oacute;", "Ó");
			strReturn=Utils::String::Replace(strReturn, "&Ocirc;", "Ô");
			strReturn=Utils::String::Replace(strReturn, "&Ouml;", "Ö");
			strReturn=Utils::String::Replace(strReturn, "&Ugrave", "Ù");
			strReturn=Utils::String::Replace(strReturn, "&Uacute;", "Ú");
			strReturn=Utils::String::Replace(strReturn, "&Ucirc;", "Û");
			strReturn=Utils::String::Replace(strReturn, "&Uuml;", "Ü");
			strReturn=Utils::String::Replace(strReturn, "&Ccedil;", "Ç");
			strReturn=Utils::String::Replace(strReturn, "&nbsp;","");
			strReturn=Utils::String::Replace(strReturn, "&#234;","ê");
			strReturn=Utils::String::Replace(strReturn, "&#233;","é");
			strReturn=Utils::String::Replace(strReturn, "&#235;","ë");
			strReturn=Utils::String::Replace(strReturn, "&#226;","â");
			strReturn=Utils::String::Replace(strReturn, "&#244;","ô");
			strReturn=Utils::String::Replace(strReturn, "&mdash;","-");

		}
		if(Utils::String::Contains(strReturn, "Ã"))
		{
			strReturn=Utils::String::Replace(strReturn, "Ã´", "ô");	
			strReturn=Utils::String::Replace(strReturn, "Ãª", "ê");	
			strReturn=Utils::String::Replace(strReturn, "Ã¢", "â");			
			strReturn=Utils::String::Replace(strReturn, "Ã©", "é");			
			strReturn=Utils::String::Replace(strReturn, "Ã«", "ë");					
			strReturn=Utils::String::Replace(strReturn, "Ã¨", "è");			
			strReturn=Utils::String::Replace(strReturn, "Ã ", "à");			
			strReturn=Utils::String::Replace(strReturn, "Ã§", "ç");			
			strReturn=Utils::String::Replace(strReturn, "Ã‚", "Â");			
			strReturn=Utils::String::Replace(strReturn, "Ã€", "À");			
			strReturn=Utils::String::Replace(strReturn, "Ã‰", "É");					
			strReturn=Utils::String::Replace(strReturn, "ÃŠ", "Ê");			
			strReturn=Utils::String::Replace(strReturn, "ÃŽ", "Î");	
		}
		if(Utils::String::Contains(strReturn, "â"))
		{
			strReturn=Utils::String::Replace(strReturn, "â€“", "-");
		}


		
		return strReturn;
	}

	DataArray2D<std::string> TableauHtmlVersDataArray(const std::string &strSource)
	{
		int posDebutTableau=strSource.find("<table");
		int posFinTableau=strSource.find("</table>");

		int nbLigne=Utils::String::Count(strSource, "</tr>", posDebutTableau, posFinTableau);
		//int nbCellule=Utils::String::Count(strSource, "</td>", posDebutTableau, posFinTableau);

		StringArray ligneArray=Utils::String::GetArrayOfBetween(strSource, "<tr", "</tr>", posDebutTableau, posFinTableau);

		int nbColonne=0;
		for(unsigned int i=0; i<ligneArray.size(); i++)
		{
			int tempCol=Utils::String::Count(ligneArray.at(i), "</td>",0);
			nbColonne=(tempCol>nbColonne)?tempCol:nbColonne;
		}

		DataArray2D<std::string> tableauDeDonnnees(nbLigne, nbColonne);
		for(unsigned int i=0; i<ligneArray.size(); i++)
		{
			//todo: éviter de recalculer ! c'est des vecteur après tout ! (merge des deux boucles)
			StringArray colArray=Utils::String::GetArrayOfBetween(ligneArray.at(i), "<td", "</td>");
			for(unsigned int j=0; j<colArray.size(); j++)
			{
				std::string strData(Utils::Html::HtmlDecode(Utils::Html::RemoveHtmlTag("<td"+colArray.at(j)+"</td>"))); // Remove html crap and extract data
				tableauDeDonnnees.AjouterDonnees(i,j, strData);
			}
		}
		return tableauDeDonnnees;
	}

	std::string RemoveHtmlTag( const std::string & strSource )
	{
		std::string cleanString(strSource);
		static boost::regex regularExpression("<(.|\n)*?>"); //Ajouter code vérification balise incomplète
		cleanString=boost::regex_replace(cleanString, regularExpression , "", boost::match_default | boost::format_sed);
		return cleanString;
	}

	std::vector<DataArray2D<std::string>> TableauHtmlVersDataArrayVector(const std::string& strSource)
	{
		std::string strCopyOfSource(Utils::String::Replace(strSource, "<tbody>", "</table><table>"));
		strCopyOfSource=Utils::String::Replace(strCopyOfSource, "<TBODY", "</table><table>");
		strCopyOfSource=Utils::String::Replace(strCopyOfSource, "<TABLE", "</table><table>");
		strCopyOfSource=Utils::String::Replace(strCopyOfSource, "<table", "</table><table>");
		strCopyOfSource=Utils::String::Replace(strCopyOfSource, "</tbody>", "</table><table>");
		strCopyOfSource=Utils::String::Replace(strCopyOfSource, "</TBODY>", "</table><table>");
		std::vector<DataArray2D<std::string>> dataArrayVector;
		StringArray tableArray = Utils::String::GetArrayOfBetween(strCopyOfSource, "<table", "</table>");
		for(unsigned int i=0; i<tableArray.size(); i++)
		{
			dataArrayVector.push_back(Utils::Html::TableauHtmlVersDataArray("<table"+tableArray.at(i)+" </table>"));
		}
		return dataArrayVector;
	}
}
}