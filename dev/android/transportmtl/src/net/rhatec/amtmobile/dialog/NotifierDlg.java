package net.rhatec.amtmobile.dialog;

import java.util.Calendar;

import net.rhatec.amtmobile.R;
import net.rhatec.amtmobile.baseactivity.ActivityWithMenu;
import net.rhatec.amtmobile.constants.TypeString;
import net.rhatec.amtmobile.helpers.StringHelpers;
import net.rhatec.amtmobile.notifications.OnetimeAlarmReceiver;
import net.rhatec.amtmobile.types.DateHelpers;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Hocine
 * @description:
 */
public class NotifierDlg extends ActivityWithMenu implements OnClickListener, OnItemSelectedListener, DialogInterface.OnClickListener
{
	private static final String	NOTIFICATION_TIME		= "NOTIFICATION_TIME";
	static final int			DATE_DIALOG_ID			= 0;

	// Heure du passage de l'autobus, ne devrait pas changer.
	// TODO trouver un moyen de la mettre final
	private Time				m_heurePassage;
	private int					m_delaiNotificationChoisi;

	// Bloc d'infos récuperer via le bundel.
	private Time				m_heureNotification;
	private Time				m_dateNotification;
	private String				m_strNoAutobus;
	private String				m_NomTransportService;
	private String				m_strDirectionBus;
	private String				m_NoArret;
	private String				m_NomIntersection;

	// Pour l'interface
	private Button				m_buttonDate;
	private Spinner				m_spinnerDelaiNotification;
	private Button				m_buttonSet;
	private Button				m_buttonClear;

	private TextView			m_heureNotificationTxt;

	// Structure de données
	String[]					ArrayStringDelaiNotification;									// =
	// {"5 "
	// +
	// R.string.notifier_minuteBeforeBusStop,
	// "10 "
	// +
	// R.string.notifier_minuteBeforeBusStop,"15 "
	// +
	// R.string.notifier_minuteBeforeBusStop
	// };
	int[]						ArrayDelaiNotification	=
	{ 5, 10, 15, 20, 25, 30, 35, 45, 60 };
	private boolean				m_dst;
	private SharedPreferences	m_Preferences;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifier);
		this.setTitle(getResources().getText(R.string.notification_titre));
		m_delaiNotificationChoisi = 5;
		ArrayStringDelaiNotification = new String[ArrayDelaiNotification.length];
		m_Preferences = getPreferences(MODE_PRIVATE);
		// faire la boucle et le remplir
		int numDelaiNotification = ArrayDelaiNotification.length;
		for (int i = 0; i < numDelaiNotification; ++i)
		{
			if (ArrayDelaiNotification[i] < 10)// Hack pour éviter que la Notification Time soit sur deux lignes
				ArrayStringDelaiNotification[i] = "0" + String.valueOf(ArrayDelaiNotification[i]) + " " + getString(R.string.notifier_minuteBeforeBusStop);
			else
				ArrayStringDelaiNotification[i] = String.valueOf(ArrayDelaiNotification[i]) + " " + getString(R.string.notifier_minuteBeforeBusStop);
		}

		// L'ordre d'appel des deux fonctions plus bas est important
		m_heureNotification = new Time();
		// Recuperation des informations passer par l'activité précédente.
		this.recupererInformationsPasseesParBundle();

		Calendar unCalendrier = Calendar.getInstance();

		m_heureNotification.year = unCalendrier.get(Calendar.YEAR);
		m_heureNotification.month = unCalendrier.get(Calendar.MONTH);
		m_heureNotification.monthDay = unCalendrier.get(Calendar.DAY_OF_MONTH);
		m_dst = true;
		if (DateHelpers.horaireApresMinuit(m_heureNotification) && DateHelpers.horaireApresMinuit(DateHelpers.obtenirObjetTimeActuel()) == false)
		{
			m_heureNotification.monthDay += 1;
			// @warnings faire attention ici avec des effets de bords
			// android/text/format/Time.html#toMillis%28boolean%29
			m_heureNotification.normalize(false);
			m_dst = false;
		}

		m_heurePassage = new Time(m_heureNotification);
		// Objet time modifier par le spinner.
		m_dateNotification = new Time(m_heureNotification);
		// numero Arret
		TextView textNoArret = (TextView) findViewById(R.id.notifier_NoArretTxt);
		textNoArret.setText(m_NoArret);

		// Intersection
		TextView textNomIntersection = (TextView) findViewById(R.id.notifier_IntersectionTxt);
		textNomIntersection.setText(m_NomIntersection);

		// Heure de la notification
		m_heureNotificationTxt = (TextView) findViewById(R.id.notifier_HeureReelNotification_DATA);
		if (DateFormat.is24HourFormat(this.getApplicationContext()))
			m_heureNotificationTxt.setText(m_heureNotification.format("%Y/%m/%d %H:%M"));
		else
			m_heureNotificationTxt.setText(m_heureNotification.format("%Y/%m/%d %I:%M%p"));

		// Reseau Transport
		TextView textReseauTransport = (TextView) findViewById(R.id.notifier_ReseauTransportTxt);
		textReseauTransport.setText(m_NomTransportService);

		// Autobus + Direction
		TextView txtDirectionBus = (TextView) findViewById(R.id.notifier_AutobusTxt);
		txtDirectionBus.setText(m_strNoAutobus + " " + StringHelpers.charDirectionToViewableString(m_strDirectionBus, this.getBaseContext()));

		// Heure Passage De l'autobus
		TextView heurePassageAutobus = (TextView) findViewById(R.id.notifier_HeureReelPassage_DATA);
		if (DateFormat.is24HourFormat(this.getApplicationContext()))
			heurePassageAutobus.setText(m_heurePassage.format("%H:%M"));
		else
			heurePassageAutobus.setText(m_heurePassage.format("%I:%M%p"));

		/* Déclaration des boutons de la vue */

		// Date de la notification
		m_buttonDate = (Button) findViewById(R.id.notifier_DateActuelle);

		// On va afficher la date actuelle
		this.updateAffichageDateNotification();

		m_buttonDate.setOnClickListener(this);

		// format("%Y%m%dT%H%M%S").

		// SpinnerDelaiNotification
		m_spinnerDelaiNotification = (Spinner) findViewById(R.id.notifier_delaiTimer);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ArrayStringDelaiNotification);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		m_spinnerDelaiNotification.setAdapter(adapter);
		m_spinnerDelaiNotification.setOnItemSelectedListener(this);

		// Button Set
		m_buttonSet = (Button) findViewById(R.id.notifier_setButton);
		m_buttonSet.setOnClickListener(this);

		// Button Clear
		m_buttonClear = (Button) findViewById(R.id.notifier_clearButton);
		m_buttonClear.setOnClickListener(this);
		// m_Notifier = new NotifierManager();

	}

	/**
	 * @function: onClick
	 * @description:
	 * @author: Hocine
	 * @params[in]:
	 * @params[out]:
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{

		if (v == m_buttonSet)
		{
			/*
			 * Est ce que l'heure et la date de notification choisi par
			 * l'utilisateur est cohérente. Cohérente si la date est pas dans le
			 * passée et si l'heure de notification est plus tôt que l'heure de
			 * passage (cliquée)
			 */
			Time timeActuel = DateHelpers.obtenirObjetTimeActuel();
			// TODO supprimer les strings NotifierDlg_* non utilisees
			if (m_heureNotification.before(timeActuel) == false)
			{

				String time = m_Preferences.getString(NOTIFICATION_TIME, new Time().format2445());
				Time savedTime = new Time(); // Last notification time
				savedTime.parse(time);

				if (savedTime.after(timeActuel)) // Si il y a une alerte valide
					// en attente
				{
					String notifTime = null;
					if (DateFormat.is24HourFormat(this.getApplicationContext()))
						notifTime = savedTime.format("%Y/%m/%d %H:%M");
					else
						notifTime = savedTime.format("%Y/%m/%d %I:%M%p");
					afficherQuestion(getResources().getText(R.string.NotifierDlg_Already_a_notification) + " " + notifTime + getResources().getText(R.string.NotifierDlg_Which_to_replace));

				} else
				{
					this.enregistrerUneAlarmePourNotification();
					Toast.makeText(this, getResources().getText(R.string.NotifierDlg_Notification_ajoutee), Toast.LENGTH_LONG).show();
					finish();
				}
			} else
			{
				// heure dans le passe
				this.afficherMessage(getResources().getString(R.string.NotifierDlg_Notification_heure_passage_dans_passee));

			}
		} else if (v == m_buttonClear)
		{
			finish();
		} else if (v == m_buttonDate)
		{
			// Va demander le pop-up de data picker
			showDialog(DATE_DIALOG_ID);

		}

	}

	// On click des alert dialog pour avertir qu'une seul notification possible
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		if (which == DialogInterface.BUTTON_POSITIVE)
		{
			this.enregistrerUneAlarmePourNotification(); // On remplace l'autre
			// notification
			Toast.makeText(this, getResources().getText(R.string.NotifierDlg_Notification_ajoutee), Toast.LENGTH_LONG).show();
			finish();
		} else if (which == DialogInterface.BUTTON_NEGATIVE)
		{
			// On ne met pas de notifications
		}
	}

	/**
	 * @function: onItemSelected
	 * @description: Fonction appelée lorsque le spinner du délai de
	 *               notification bouge. La fonction met à jour l'heure de
	 *               notification en fonction du délai de notification choisi
	 *               dans le spinner.
	 * @author: Hocine
	 * @params[in]:
	 * @params[out]:
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView,
	 *      android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int nItemCourant, long arg3)
	{

		// Ici on recupere l'heure du passage
		m_heureNotification.set(m_heurePassage);

		// Ici on récupère l'annee, le mois et le jour
		m_heureNotification.year = m_dateNotification.year;
		m_heureNotification.month = m_dateNotification.month;
		m_heureNotification.monthDay = m_dateNotification.monthDay;

		// Calcul de la nouvelle heure de notification
		m_delaiNotificationChoisi = ArrayDelaiNotification[m_spinnerDelaiNotification.getSelectedItemPosition()];

		m_heureNotification.minute -= m_delaiNotificationChoisi;
		m_heureNotification.normalize(m_dst);
		if (DateFormat.is24HourFormat(this.getApplicationContext()))
			m_heureNotificationTxt.setText(m_heureNotification.format("%Y/%m/%d %H:%M"));
		else
			m_heureNotificationTxt.setText(m_heureNotification.format("%Y/%m/%d %I:%M%p"));

	}

	/**
	 * @function: onNothingSelected
	 * @description:
	 * @author: Hocine
	 * @params[in]:
	 * @params[out]:
	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
	 */

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @function: updateAffichageDateNotification
	 * @description: Gère de mettre a jour la date affichée par le
	 *               boutonDate(spinnerDate) et de et de mettre à jour la date
	 *               que l'utilisateur à choisi pour la notification.
	 * @author: Hocine
	 * @params[in]:
	 * @params[out]:
	 */
	private void updateAffichageDateNotification()
	{
		char[] buttonDate = m_dateNotification.format("%d/%B/%Y").toCharArray();
		m_buttonDate.setText(buttonDate, 0, buttonDate.length);
		m_heureNotification.year = m_dateNotification.year;
		m_heureNotification.month = m_dateNotification.month;
		m_heureNotification.monthDay = m_dateNotification.monthDay;

		if (DateFormat.is24HourFormat(this.getApplicationContext()))
			m_heureNotificationTxt.setText(m_heureNotification.format("%Y/%m/%d %H:%M"));
		else
			m_heureNotificationTxt.setText(m_heureNotification.format("%Y/%m/%d %I:%M%p"));
	}

	/**
	 * @function: onCreateDialog
	 * @description: Fonction appeler lorsque d'un dialogue appele avec
	 *               showDialog(id)
	 * @author: Hocine
	 * @params[in]:
	 * @params[out]:
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
		case DATE_DIALOG_ID:
			/*
			 * Pop-up de data picker emet la fonction mDateSetListener comme la
			 * fonction a appeler lorsque l'utilisateur appuis sur set
			 */
			return new DatePickerDialog(this, mDateSetListener, m_heureNotification.year, m_heureNotification.month, m_heureNotification.monthDay);
		}
		return null;
	}

	public void recupererInformationsPasseesParBundle()
	{
		Bundle b = this.getIntent().getExtras();

		m_strNoAutobus = b.getString(TypeString.NOCIRCUIT);
		m_NomTransportService = b.getString(TypeString.SOCIETECODE);
		m_strDirectionBus = b.getString(TypeString.DIRECTION);
		m_NoArret = b.getString(TypeString.NOARRET);
		m_NomIntersection = b.getString(TypeString.INTERSECTIONSCONCAT);

		m_heureNotification.minute = b.getInt(TypeString.MINUTE);
		m_heureNotification.hour = b.getInt(TypeString.HEURE);

	}

	/**
	 * @function: afficherMessage
	 * @description: Affiche un message avec un alert dialogue
	 * @author: Hocine
	 * @params[in]: message: message à afficher.
	 */
	private void afficherMessage(String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(R.string.notification_titre);

		alertDialog.setMessage(message);
		alertDialog.setButton(this.getString(R.string.general_ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				return;
			}
		});
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.show();
	}

	/**
	 * @function: afficherQuestion
	 * @description: Afficher une question
	 * @author: JP
	 * @params[in]: message: Question à afficher
	 */
	private void afficherQuestion(String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(R.string.notification_titre);
		alertDialog.setMessage(message);

		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, this.getString(R.string.general_continue), this);
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, this.getString(R.string.general_cancel), this);
		alertDialog.show();
	}

	/**
	 * @function: enregistrerUneAlarmePourNotification
	 * @description:
	 * @author: Hocine
	 * @params[in]:
	 * @params[out]:
	 */
	private void enregistrerUneAlarmePourNotification()
	{

		Intent intentToFire = new Intent(this, OnetimeAlarmReceiver.class);
		// On récupere l'information reçue et on la passe à l'activité suivante.
		m_Preferences.edit().putString(NOTIFICATION_TIME, m_heureNotification.format2445()).commit();

		Bundle b = this.getIntent().getExtras();
		b.putInt("DELAI", m_delaiNotificationChoisi);
		intentToFire.putExtras(b);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentToFire, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, m_heureNotification.toMillis(true), pendingIntent);

	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener	mDateSetListener	= new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			m_dateNotification.year = year;
			m_dateNotification.month = monthOfYear;
			m_dateNotification.monthDay = dayOfMonth;
			updateAffichageDateNotification();
		}
	};

}
