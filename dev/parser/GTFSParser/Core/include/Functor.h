#ifndef FUNCTOR_H
#define FUNCTOR_H


class TFunctor
{
public:
	virtual void operator()()=0;
	virtual void Call()=0;
};


template <class TClass> 
class TSpecificFunctor : public TFunctor
{
public:

	TSpecificFunctor(TClass* _pt2Object, void(TClass::*_fpt)())
	{ 
		m_pt2Object = _pt2Object;
		fpt=_fpt;
	};

	virtual void operator()()
	{ 
		Call();
	};

	virtual void Call()
	{ 
		(*m_pt2Object.*fpt)();
	}; 

private:
	void (TClass::*fpt)();
	TClass* m_pt2Object;
};

template <class TClass, class TArg1> 
class TSpecificFunctor1 : public TFunctor
{
public:

	TSpecificFunctor1(TClass* _pt2Object, void(TClass::*_fpt)(TArg1), TArg1 arg1)
	{ 
		m_pt2Object = _pt2Object;
		fpt=_fpt;
		m_oArg1 = arg1;
	};

	virtual void operator()()
	{ 
		Call();
	};

	virtual void Call()
	{ 
		(*m_pt2Object.*fpt)(m_oArg1);
	}; 

private:
	void (TClass::*fpt)(TArg1);
	TClass* m_pt2Object;
	TArg1 m_oArg1;
};

template <class TClass, class TArg1, class TArg2> 
class TSpecificFunctor2 : public TFunctor
{
public:

	TSpecificFunctor2(TClass* _pt2Object, void(TClass::*_fpt)(TArg1, TArg2), TArg1 arg1, TArg2 arg2)
	{ 
		m_pt2Object = _pt2Object;
		fpt=_fpt;
		m_oArg1 = arg1;
		m_oArg2 = arg2;

	};

	virtual void operator()()
	{ 
		Call();
	};

	virtual void Call()
	{ 
		(*m_pt2Object.*fpt)(m_oArg1, m_oArg2);
	}; 

private:
	void (TClass::*fpt)(TArg1);
	TClass* m_pt2Object;
	TArg1 m_oArg1;
	TArg2 m_oArg2;
};

template <class TClass, class TArg1, class TArg2, class TArg3> 
class TSpecificFunctor3 : public TFunctor
{
public:

	TSpecificFunctor3(TClass* _pt2Object, void(TClass::*_fpt)(TArg1, TArg2, TArg3), TArg1 arg1, TArg2 arg2, TArg3 arg3)
	{ 
		m_pt2Object = _pt2Object;
		fpt=_fpt;
		m_oArg1 = arg1;
		m_oArg2 = arg2;
		m_oArg3 = arg3;

	};

	virtual void operator()()
	{ 
		Call();
	};

	virtual void Call()
	{ 
		(*m_pt2Object.*fpt)(m_oArg1, m_oArg2, m_oArg3);
	}; 

private:
	void (TClass::*fpt)(TArg1);
	TClass* m_pt2Object;
	TArg1 m_oArg1;
	TArg2 m_oArg2;
	TArg3 m_oArg3;
};
#endif