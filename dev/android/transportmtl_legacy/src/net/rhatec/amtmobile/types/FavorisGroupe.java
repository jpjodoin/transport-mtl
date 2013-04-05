package net.rhatec.amtmobile.types;

import java.util.ArrayList;
import android.graphics.Color;


public class FavorisGroupe implements FavorisNode
{
	@Override
	public FavorisType getType() {
		return FavorisType.FavorisGroupe;
	}
	
	
	private String mName;
	private int mColor;
	private ArrayList<Favoris> mListeFavoris;
	
	public FavorisGroupe()
	{
		
	}
	
	public FavorisGroupe(String name, String color)
	{
		definirNom(name);
		definirCouleur(color);
		mListeFavoris = new ArrayList<Favoris>();
	}
	
	public void definirNom(String nouveauNom)
	{		
		mName = nouveauNom.replace(';', ':'); // Pour éviter les problèmes de split si quelqu'un met un ,
	}
	
	public String obtenirNom()
	{
		return mName;
	}
	
	public void definirCouleur(String nouvelleCouleur)
	{		
		mColor = Color.parseColor(nouvelleCouleur);
	}
	
	public int obtenirCouleur()
	{
		return mColor;
	}
	
	
	public void ajouterFavoris(Favoris f)
	{
		mListeFavoris.add(f);
	}
	
	  public ArrayList<Favoris> obtenirFavoris() 
	  {
		 return mListeFavoris;
	  }
	  

	  
	  public void definirListeDeFavoris(ArrayList<Favoris> favoris)
	  {
		  mListeFavoris = favoris;
	  }
	  
	  @Override
	  public String Serialize()
		{
			StringBuilder sb = new StringBuilder(2048); //Regarder pour taille...
			sb.append("g;").append(mName).append(';').append(String.format("#%X", mColor)).append('\n');
			for (Favoris f : mListeFavoris)
			{
				sb.append(f.Serialize());
			}
			sb.append("e;\n");
			/*sb.append("f;").append(m_strTransportService).append(";").append(m_strNoBus).append(";").append(m_strDirection).append(";").append(m_strNoArret).append(";").append(m_strIntersection).append(";").append(m_nLigneFavoris).append(";").append(m_codeInfoDirection).append(";").append(m_codeInfoCircuit).append("\n");
			for (Horaire h : m_vHoraire)
			{
				sb.append(h.Serialize());
			}*/	
			
			return sb.toString();
		}
		
		public boolean UnSerialize(String str)
		{
			boolean groupeValide = false;
			String[] favorisArray = str.split("\n");
			mListeFavoris = new ArrayList<Favoris>();
			int length = favorisArray.length;
			if(favorisArray.length > 0)
			{
				String infoArray[] = favorisArray[0].split(";");
				if(infoArray.length == 3 && infoArray[0].equals("g"))
				{
					definirNom(infoArray[1]);
					definirCouleur(infoArray[2]);
					groupeValide = true;
					int i = 1;
					StringBuilder sb = null;
					while(i < length)
					{					
						String line = favorisArray[i];
						if(line.length()>0)
						{
							if(line.charAt(0) == 'f' || line.charAt(0) == 'e')
							{
								if(sb != null)
								{
									Favoris f = new Favoris();
									f.UnSerialize(sb.toString(), true, -1);
									mListeFavoris.add(f);
								}
								sb = new StringBuilder(2048);
							}
							sb.append(line + '\n');						
						}
						
						++i;				
					}	
					if(sb!= null)
					{
						Favoris f = new Favoris();
						f.UnSerialize(sb.toString(), true, -1);
						mListeFavoris.add(f);
					}
				}	
			}	
			return groupeValide;		
		}	
}
