#include "Page.h"
#include <iostream>
#include "StringHelpers.h"

namespace Utils
{
namespace Html
{
Page::Page(const std::string& source)
{
	GetTableFromPage(source);
}

Page::~Page(void)
{

}

	
void Page::AfficherTableaux()
{
	for(unsigned int i=0; i < m_vectorTableau.size(); ++i)
	{
		std::cout<<"Tableau "<<i<<std::endl;
		m_vectorTableau.at(i).Afficher();
	}
}

const Tableau& Page::ObtenirTableau(int index)
{
	return m_vectorTableau.at(index);
}

void Page::GetTableFromPage(const std::string& source)
{

	//http://blog.stevenlevithan.com/archives/match-innermost-html-element
	static boost::regex tableRegex(/*"<table[^>]*>(.*?)</table>"*/"<table\\b[^>]*>(?:(?>[^<]+)|<(?!table\\b[^>]*>))*?</table>", boost::regex_constants::icase); 
	boost::match_results<std::string::const_iterator> what;
	std::string::const_iterator t_start, t_end;
	t_start = source.begin();
	t_end = source.end();
	while(boost::regex_search(t_start, t_end, what, tableRegex, boost::match_default))
	{
		t_start = what[0].second; 
		//std::cout << "what[0]: " << what[0];
		//std::cout << "what[1]: " << what[1];
		//std::cout << "what[2]: " << what[2];
		std::string tableContains = Utils::String::EnleveEspaceDebutFin(what[0]);
		if(tableContains != "")
		{
			Tableau t(tableContains);
			m_vectorTableau.push_back(t);
		}
		
	}
}


std::vector<Tableau*> Page::Contient(const std::string& string)
{
	std::vector<Tableau*> vTableau; 
	for(unsigned int i=0; i < m_vectorTableau.size(); ++i)
	{
		if(m_vectorTableau.at(i).Contient(string))
			vTableau.push_back(&m_vectorTableau.at(i));
	}
	return vTableau;


}
std::vector<Tableau*> Page::Contient(const boost::regex& regex)
{
	std::vector<Tableau*> vTableau; 
	for(unsigned int i=0; i < m_vectorTableau.size(); ++i)
	{
		if(m_vectorTableau.at(i).Contient(regex))
			vTableau.push_back(&m_vectorTableau.at(i));
	}
	return vTableau;

}

std::vector<Tableau*> Page::ContientNLignes(unsigned int n)
{
	std::vector<Tableau*> vTableau; 
	for(unsigned int i=0; i < m_vectorTableau.size(); ++i)
	{
		if(m_vectorTableau.at(i).ObtenirNumLigne() == n)
			vTableau.push_back(&m_vectorTableau.at(i));
	}
	return vTableau;
}


}
}