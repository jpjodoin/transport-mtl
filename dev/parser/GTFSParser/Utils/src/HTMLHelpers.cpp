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
			//D�codage des principaux caract�re de la langue fran�aise
			//Ajout au besoin
			//http://www.ascii.cl/htmlcodes.htm
			strReturn=Utils::String::Replace(strReturn, "&agrave;", "�");
			strReturn=Utils::String::Replace(strReturn, "&aacute;", "�");
			strReturn=Utils::String::Replace(strReturn, "&acirc;", "�");
			strReturn=Utils::String::Replace(strReturn, "&auml;", "�");
			strReturn=Utils::String::Replace(strReturn, "&egrave;", "�");
			strReturn=Utils::String::Replace(strReturn, "&eacute;", "�");
			strReturn=Utils::String::Replace(strReturn, "&ecirc;", "�");
			strReturn=Utils::String::Replace(strReturn, "&euml;", "�");
			strReturn=Utils::String::Replace(strReturn, "&igrave;", "�");
			strReturn=Utils::String::Replace(strReturn, "&iacute;", "�");
			strReturn=Utils::String::Replace(strReturn, "&icirc;", "�");
			strReturn=Utils::String::Replace(strReturn, "&iuml;", "�");
			strReturn=Utils::String::Replace(strReturn, "&ograve", "�");
			strReturn=Utils::String::Replace(strReturn, "&oacute;", "�");
			strReturn=Utils::String::Replace(strReturn, "&ocirc;", "�");
			strReturn=Utils::String::Replace(strReturn, "&ouml;", "�");
			strReturn=Utils::String::Replace(strReturn, "&ugrave", "�");
			strReturn=Utils::String::Replace(strReturn, "&uacute;", "�");
			strReturn=Utils::String::Replace(strReturn, "&ucirc;", "�");
			strReturn=Utils::String::Replace(strReturn, "&uuml;", "�");
			strReturn=Utils::String::Replace(strReturn, "&ccedil;", "�");
			strReturn=Utils::String::Replace(strReturn, "&nbsp;", " ");
			
			//Version Majuscule
			strReturn=Utils::String::Replace(strReturn, "&Agrave;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Aacute;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Acirc;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Auml;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Egrave;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Eacute;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Ecirc;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Euml;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Igrave;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Iacute;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Icirc;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Iuml;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Ograve", "�");
			strReturn=Utils::String::Replace(strReturn, "&Oacute;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Ocirc;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Ouml;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Ugrave", "�");
			strReturn=Utils::String::Replace(strReturn, "&Uacute;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Ucirc;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Uuml;", "�");
			strReturn=Utils::String::Replace(strReturn, "&Ccedil;", "�");
			strReturn=Utils::String::Replace(strReturn, "&nbsp;","");
			strReturn=Utils::String::Replace(strReturn, "&#234;","�");
			strReturn=Utils::String::Replace(strReturn, "&#233;","�");
			strReturn=Utils::String::Replace(strReturn, "&#235;","�");
			strReturn=Utils::String::Replace(strReturn, "&#226;","�");
			strReturn=Utils::String::Replace(strReturn, "&#244;","�");
			strReturn=Utils::String::Replace(strReturn, "&mdash;","-");

		}
		if(Utils::String::Contains(strReturn, "�"))
		{
			strReturn=Utils::String::Replace(strReturn, "ô", "�");	
			strReturn=Utils::String::Replace(strReturn, "ê", "�");	
			strReturn=Utils::String::Replace(strReturn, "â", "�");			
			strReturn=Utils::String::Replace(strReturn, "é", "�");			
			strReturn=Utils::String::Replace(strReturn, "ë", "�");					
			strReturn=Utils::String::Replace(strReturn, "è", "�");			
			strReturn=Utils::String::Replace(strReturn, "à", "�");			
			strReturn=Utils::String::Replace(strReturn, "ç", "�");			
			strReturn=Utils::String::Replace(strReturn, "Â", "�");			
			strReturn=Utils::String::Replace(strReturn, "À", "�");			
			strReturn=Utils::String::Replace(strReturn, "É", "�");					
			strReturn=Utils::String::Replace(strReturn, "Ê", "�");			
			strReturn=Utils::String::Replace(strReturn, "Î", "�");	
		}
		if(Utils::String::Contains(strReturn, "�"))
		{
			strReturn=Utils::String::Replace(strReturn, "–", "-");
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
			//todo: �viter de recalculer ! c'est des vecteur apr�s tout ! (merge des deux boucles)
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
		static boost::regex regularExpression("<(.|\n)*?>"); //Ajouter code v�rification balise incompl�te
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