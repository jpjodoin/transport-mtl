#include "Tableau.h"
#include "StringHelpers.h"
#include <boost/regex.hpp>
#include "HtmlHelpers.h"
#include <iostream>
#include <iomanip>


namespace Utils
{
namespace Html
{
Tableau::Tableau(const std::string& tableString)
{
	static boost::regex rowRegex("<tr[^>]*>(.*?)</tr>", boost::regex_constants::icase); 
	static boost::regex columnRegex("<td[^>]*>(.*?)(</td>|<td)", boost::regex_constants::icase);  //"<td[^>]*>(.*?)</td>" //Fix pour contourner le fait que la STM ferme pas toute ces cellules....
	std::string::const_iterator row_start = tableString.begin();
	std::string::const_iterator row_end   = tableString.end();
	boost::match_results<std::string::const_iterator> what;
	
	while(boost::regex_search(row_start, row_end, what, rowRegex, boost::match_default))
	{
		row_start = what[0].second; 
		std::string rowContains = what[1];
		std::string::const_iterator col_start = rowContains.begin();
		std::string::const_iterator col_end = rowContains.end();

			
		std::vector<CelluleTableau> ligne;
		while(boost::regex_search(col_start, col_end, what, columnRegex, boost::match_default))
		{
			ligne.push_back(CelluleTableau(what[1]));
				//String::EnleveEspaceDebutFin(HtmlDecode(RemoveHtmlTag(what[1])))); //Todo: Replace
			col_start = what[0].second; 
		}
		m_tableau.push_back(ligne);
	}
}





Tableau::~Tableau()
{

}


void Tableau::Afficher()
{
	std::vector<unsigned int> width_vector;
	unsigned int numLigne = m_tableau.size();
	for(unsigned int i=0; i < numLigne; ++i) //On calcule les espaces des colonnes
	{
		const std::vector<CelluleTableau>& ligne = m_tableau.at(i);
		for(unsigned int j=0; j<ligne.size(); ++j)
		{
			if(j < width_vector.size())
			{				
				if(width_vector.at(j) <ligne.at(j).getText().size())
				{
					width_vector.at(j) = ligne.at(j).getText().size();
				}	
			}
			else
			{
				width_vector.push_back(ligne.at(j).getText().size());
			}
		}
	}
	for(unsigned int i=0; i < numLigne; ++i)
	{
		const std::vector<CelluleTableau>& ligne = m_tableau.at(i);
		for(unsigned int j=0; j<ligne.size(); ++j)
		{
			std::cout<< std::left <<std::setw(width_vector.at(j))<<ligne.at(j).getText()<<" ";
		}
		std::cout<<std::endl;
	}
}

std::vector<CelluleTableau> Tableau::ObtenirLigne(int i)
{
	return m_tableau.at(i);
}

bool Tableau::Contient(const std::string& strMatch)
{
	bool found = false;
	unsigned int numLigne = m_tableau.size();
	for(unsigned int i=0; i < numLigne; ++i)
	{
		const std::vector<CelluleTableau>& ligne = m_tableau.at(i);
		for(unsigned int j=0; j<ligne.size(); ++j)
		{			
			if(Utils::String::Contains(ligne.at(j).getText(), strMatch))
				found = true; 
		}
	}
	return found;
}


bool Tableau::Contient(const boost::regex& regex)
{
	bool found = false;
	unsigned int numLigne = m_tableau.size();
	for(unsigned int i=0; i < numLigne; ++i)
	{
		const std::vector<CelluleTableau>& ligne = m_tableau.at(i);
		for(unsigned int j=0; j<ligne.size(); ++j)
		{
			if(boost::regex_match(ligne.at(j).getText().c_str(), regex))
				found = true; 
		}
	}
	return found;
}

unsigned int Tableau::ObtenirNumLigne()
{
	return m_tableau.size();
}


}
}