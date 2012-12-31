#include "CelluleTableau.h"
#include <boost/regex.hpp>
#include "StringHelpers.h"
#include "HtmlHelpers.h"

namespace Utils
{
namespace Html
{
CelluleTableau::CelluleTableau(const std::string& source)
: m_text("")
, m_url("")
, m_source(source)
{
	boost::cmatch matches;
	static boost::regex linkRegex(".*<a\\s*href=\"(.*)\".*>(.*)</a>(.*)", boost::regex_constants::icase);
		 
	if(boost::regex_match(source.c_str(), matches, linkRegex, boost::match_default))
	{
		m_url = matches[1];
		m_text = String::EnleveEspaceDebutFin(HtmlDecode(RemoveHtmlTag(matches[2]+matches[3])));
	}
	else
	{
		m_text =  String::EnleveEspaceDebutFin(HtmlDecode(RemoveHtmlTag(source)));
	}
}

CelluleTableau::~CelluleTableau()
{

}

}
}
