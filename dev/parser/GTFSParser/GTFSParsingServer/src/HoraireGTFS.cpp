#include "HoraireGTFS.h"


HoraireGTFS::HoraireGTFS(unsigned int horaireCode, const std::string& serviceId)
: Horaire(horaireCode)
, m_serviceId(serviceId)
{
}


HoraireGTFS::~HoraireGTFS(void)
{
}
