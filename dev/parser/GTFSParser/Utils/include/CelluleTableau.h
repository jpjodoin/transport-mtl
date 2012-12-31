#pragma once

#include <string>

namespace Utils
{
namespace Html
{
class CelluleTableau
{
public:
	CelluleTableau(const std::string& source);
	~CelluleTableau();
	const std::string getText() const { return m_text; }
	const std::string getUrl() const { return m_url; }
	const std::string getSource() const { return m_source; }

private:
	std::string m_text;
	std::string m_url;
	std::string m_source;
};
}
}