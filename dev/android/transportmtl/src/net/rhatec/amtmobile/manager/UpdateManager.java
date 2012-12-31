package net.rhatec.amtmobile.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.dialog.MiseAJourDlg;
import net.rhatec.amtmobile.file.BufferedFileReader;
import net.rhatec.amtmobile.helpers.FileHelpers;
import net.rhatec.amtmobile.providers.TransportProvider;
import net.rhatec.amtmobile.types.TransportServiceInfo;
import net.rhatec.amtmobile.types.UpdateStruct;
import net.rhatec.amtmobile.update.request.AppVersionRequest;
import net.rhatec.amtmobile.update.request.DBVersionRequest;
import net.rhatec.amtmobile.update.request.ValidityRequest;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

import android.os.Handler;

public class UpdateManager
{
	
	private static final String	SERVER_IN_GET_DATABASE	= "2";
	MiseAJourDlg				m_Dialog;

	Vector<UpdateStruct>	m_vUpdate;
	UpdateState				m_State;
	boolean					m_ChangeState;
	String					m_strUpdateServer = "http://www.rhatec.net/update/transportmtlv2/";
	int						m_nUpdateProgress;
	String					m_strUpdateProgress;
	String					m_ErrorString;
	Vector<String>			m_strNameArray;
	Context					m_ApplicationContext;
	String					m_StartOfCommunication;

	public enum UpdateState
	{
		DEBUT, RECHERCHE_EN_COURS, RESULTAT_RECUS, TELECHARGEMENT_EN_COURS, FUTUR_RESULTAT_RECUS, FUTUR_RESULTAT_TELECHARGEMENT_EN_COURS, VERIFIER_VALIDITE, MISEAJOUR_TERMINE, ERREUR, UNKNOWN
	}



	public void Finish()
	{
		//m_ApplicationContext = null;		
		
	}
	
	
	public UpdateManager(MiseAJourDlg dialog, Context applicationContext)
	{
		m_StartOfCommunication = "update.php?l=" + applicationContext.getResources().getConfiguration().locale.getLanguage();
		m_Dialog = dialog;
		m_ChangeState = false;
		m_ApplicationContext = applicationContext;
	}

	// Need handler for callbacks to the UI thread
	final Handler	mHandler		= new Handler();

	final Runnable	mUpdateResults	= new Runnable() {
										@Override
										public void run()
										{
											updateResultsInUi();
										}
									};

	private void updateResultsInUi()
	{

		if (m_State == UpdateState.RESULTAT_RECUS)
		{
			m_Dialog.ChangeDbToGet(m_vUpdate);
		} else if (m_State == UpdateState.TELECHARGEMENT_EN_COURS)
		{
			m_Dialog.ChangeProgress(m_nUpdateProgress, m_strUpdateProgress);
		} else if (m_State == UpdateState.ERREUR)
		{
			m_Dialog.SetErrorString(m_ErrorString);
		}
		if (m_ChangeState)
		{
			m_Dialog.ChangeState(m_State);
			m_ChangeState = false;

		}

	}

	private void VerifierVersionApp()
	{

		AppVersionRequest request = new AppVersionRequest(m_strUpdateServer, m_ApplicationContext);
		String error = request.execute();
		if (!error.equals(""))
		{
			DisplayError(error);
		}
	}

	private void ObtenirValidite(UpdateState previousState)
	{
		ValidityRequest request = new ValidityRequest(m_strUpdateServer, m_ApplicationContext);
		String error = request.execute();
		if (error.equals(""))
		{
			if (previousState == UpdateState.RECHERCHE_EN_COURS)
			{
				DisplayError(m_ApplicationContext.getResources().getString(R.string.UpdateManager_aucune_mise_a_jour_disponible));
			} else if (previousState == UpdateState.TELECHARGEMENT_EN_COURS)
			{
				m_State = UpdateState.MISEAJOUR_TERMINE;
				m_ChangeState = true;
				mHandler.post(mUpdateResults);
			}
		} else
		{
			DisplayError(error);
		}
	}

	private void VerifierVersionDb()
	{
		DBVersionRequest request = new DBVersionRequest(m_strUpdateServer, m_ApplicationContext);
		String error = request.execute();
		if (error.equals(""))
		{
			m_vUpdate = request.getUpdateVector();
			if (!m_vUpdate.isEmpty())
			{
				m_State = UpdateState.RESULTAT_RECUS;
				m_ChangeState = true;
				mHandler.post(mUpdateResults);
			} else
			{
				m_State = UpdateState.VERIFIER_VALIDITE;
				m_ChangeState = true;
				mHandler.post(mUpdateResults);
			}

		} else
		{
			DisplayError(error);
		}
	}

	public void VerifierVersionAppEtDb()
	{
		Thread runningThread = new Thread() {
			@Override
			public void run()
			{
				VerifierVersionApp();
				VerifierVersionDb();
			}
		};
		runningThread.start();
	}

	public void VerifierValidite(final UpdateState previousState)
	{
		Thread runningThread = new Thread() {
			@Override
			public void run()
			{
				ObtenirValidite(previousState);
			}
		};
		runningThread.start();
	}

	public void DisplayError(String _errorString)
	{
		if (m_Dialog.GetState() != UpdateState.ERREUR)
		{
			m_ErrorString = _errorString;
			m_State = UpdateState.ERREUR;
			m_ChangeState = true;
			mHandler.post(mUpdateResults);
		}
	}

	public void GetDatabase(Vector<String> strNameArray)
	{

		m_strNameArray = strNameArray;
		// m_strName=strName;
		m_State = m_Dialog.GetState();
		// Fire off a thread to do some work that we shouldn't do directly in
		// the UI thread
		Thread runningThread = new Thread() {
			@Override
			public void run()
			{
				updateloop:
				for (String transportName: m_strNameArray)
				{
					// Make Folder for Update
					File RootFolder = new File(TransportProvider.getRootPath() + "");
					RootFolder.mkdir();

					HttpClient httpClient = new DefaultHttpClient();
					StringBuilder uriBuilder = new StringBuilder(m_strUpdateServer);
					uriBuilder.append(m_StartOfCommunication);
					uriBuilder.append("&" + SERVER_IN_GET_DATABASE + "=" + transportName);
					HttpGet request = new HttpGet(uriBuilder.toString());
					try
					{
						HttpResponse response = httpClient.execute(request);

						int status = response.getStatusLine().getStatusCode();
						if (status != HttpStatus.SC_OK)
						{
							ByteArrayOutputStream ostream = new ByteArrayOutputStream();
							response.getEntity().writeTo(ostream);

						} else
						{
							byte[] file_name_buffer = new byte[(int) (response.getEntity().getContentLength())];
							response.getEntity().getContent().read(file_name_buffer, 0, file_name_buffer.length);

							uriBuilder = new StringBuilder(m_strUpdateServer);
							uriBuilder.append(new String(file_name_buffer, "8859_1"));
							request = new HttpGet(uriBuilder.toString());
							response = httpClient.execute(request);
							status = response.getStatusLine().getStatusCode();
							if (status != HttpStatus.SC_OK)
							{
								ByteArrayOutputStream ostream = new ByteArrayOutputStream();
								response.getEntity().writeTo(ostream);

							} else
							{

								InputStream content = response.getEntity().getContent();
								int filelength = (int) response.getEntity().getContentLength();
								byte[] file_buffer = new byte[filelength]; // filelength
								int totalreadedBytes = 0;
								int readBytes = 0;
								byte[] stream_buffer = new byte[15000];
								while ((readBytes = content.read(stream_buffer)) != -1)
								{
									System.arraycopy(stream_buffer, 0, file_buffer, totalreadedBytes, readBytes);
									totalreadedBytes += readBytes;
									m_nUpdateProgress = (int) (100 * ((double) totalreadedBytes / (double) filelength));
									m_strUpdateProgress = m_ApplicationContext.getResources().getString(R.string.UpdateManager_telechargement_en_cours) + ": "
											+ String.valueOf(new DecimalFormat("0.##").format(100 * (double) totalreadedBytes / filelength)) + "%\n";
									mHandler.post(mUpdateResults);
								}
								if (totalreadedBytes > 0)
								{

									WriteLock(transportName); // D�but de
																// l'extraction
									InputStream fis = new ByteArrayInputStream(file_buffer, 0, filelength);

									ZipInputStream zin = new ZipInputStream(new BufferedInputStream(fis));
									// Get number of file in zip file
									ZipEntry entry;

									int fileCount = 0;
									while ((entry = zin.getNextEntry()) != null)
									{
										String strFileName = entry.getName();
										if (strFileName.charAt(strFileName.length() - 1) != '/')
											fileCount++;

									}

									// Extract file
									fis = new ByteArrayInputStream(file_buffer, 0, filelength);
									zin = new ZipInputStream(new BufferedInputStream(fis));
									BufferedOutputStream dest = null;
									int f_count = 0;

									while ((entry = zin.getNextEntry()) != null)
									{
										String strFileName = entry.getName();
										m_strUpdateProgress = m_ApplicationContext.getResources().getString(R.string.UpdateManager_Extraction_In_Progress) + " " + (f_count + "/" + fileCount) + " "
												+ m_ApplicationContext.getResources().getString(R.string.UpdateManager_Files) + "\n" + strFileName; // TODO:
																																					// Remove
																																					// file
																																					// name
										mHandler.post(mUpdateResults);

										int count;
										if (strFileName.charAt(strFileName.length() - 1) == '/')
										{
											File folder = new File(TransportProvider.getRootPath() + strFileName);
											folder.mkdir();
										} else
										{
											byte data[] = new byte[2048];
											// write the files to the disk
											FileOutputStream fos = new FileOutputStream(TransportProvider.getRootPath() + strFileName);
											dest = new BufferedOutputStream(fos, 2048);
											while ((count = zin.read(data, 0, 2048)) != -1)
											{
												dest.write(data, 0, count);
											}
											dest.flush();
											dest.close();
											f_count++;
										}
									}
									zin.close();
									RemoveLock();
								}
							}

						}

					} catch (ClientProtocolException e)
					{
						m_ErrorString = "ClientProtocolException: " + e.getMessage();
						m_State = UpdateState.ERREUR;
						m_ChangeState = true;
						mHandler.post(mUpdateResults);

					} catch (IOException e)
					{
						m_ErrorString = "IOException: " + e.getMessage();
						m_State = UpdateState.ERREUR;
						m_ChangeState = true;
						mHandler.post(mUpdateResults);
					} catch (Exception e)
					{
						m_ErrorString = "Exception: " + e.getMessage();
						m_State = UpdateState.ERREUR;
						m_ChangeState = true;
						mHandler.post(mUpdateResults);
					}
					if (m_State != UpdateState.ERREUR)
					{
						TransportServiceInfo service = TransportProvider.ObtenirTransportService(transportName);
						if(service != null)
							ListeSocieteManager.ajouterSociete(service);
						else
						{
							m_State = UpdateState.ERREUR;
							m_ChangeState = true;
							mHandler.post(mUpdateResults);
							break updateloop;
						}							
					}
					else
					{
						break updateloop;
					}

				}

				if (m_State != UpdateState.ERREUR)
				{

					m_strUpdateProgress = m_ApplicationContext.getResources().getString(R.string.UpdateManager_mise_a_jour_favoris_en_cours) + "\n";// \nFichier
																																					// #"+f_count;//+"
																																					// :
																																					// "+strFileName+"\n";
					mHandler.post(mUpdateResults);
					try
					{
						FavorisManager.updateFavoris(m_strNameArray);
					} catch (Exception e)
					{
						m_ErrorString = m_ApplicationContext.getResources().getString(R.string.UpdateManager_Echec_mise_a_jour_Favoris) + ": " + e.getMessage();
						m_State = UpdateState.ERREUR;
						m_ChangeState = true;
						mHandler.post(mUpdateResults);

					}

					if (m_State != UpdateState.ERREUR)
					{
						m_State = UpdateState.VERIFIER_VALIDITE;
						m_ChangeState = true;
						mHandler.post(mUpdateResults);
					}
				}

			}

		};
		runningThread.start();
	}

	public static String ReadLock()
	{
		String result = null;

		try
		{
			BufferedFileReader in = FileHelpers.createBufferedFileInputStream(TransportProvider.getRootPath() + "Lock.conf");
			if (in != null)
			{
				result = in.readLine();
				in.close();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	static void WriteLock(String transportLocked)
	{
		try
		{
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(TransportProvider.getRootPath() + "Lock.conf", true), "8859_1");
			out.write(transportLocked);
			out.close();

		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void RemoveLock()
	{
		try
		{
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(TransportProvider.getRootPath() + "Lock.conf", false), "8859_1");
			out.write("");
			out.close();

		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
