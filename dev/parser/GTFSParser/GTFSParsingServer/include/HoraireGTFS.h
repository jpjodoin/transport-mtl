#pragma once
#include "Horaire.h"
class HoraireGTFS :	public Horaire
{
public:
	HoraireGTFS(unsigned int horaireCode, const std::string& serviceId);
	virtual ~HoraireGTFS(void);
	const std::string& getServiceId() { return m_serviceId;}

private:
	std::string m_serviceId;
};

