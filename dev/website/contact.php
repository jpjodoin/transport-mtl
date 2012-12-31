<?php
$erreur_contact_name = "black";
$erreur_contact_email = "black";
$erreur_contact_subject = "black";
$erreur_contact_descriptions ="black";
$erreur_contact_captcha ="black";
$erreur = false;

if(isset($_GET['sent']))
{
	$mailto = 'rhatec@gmail.com' ;
	$fullname = $_POST['fullname'];
	$email = $_POST['email'] ;
	$categorie = $_POST['categorie'];
	$subject = $_POST['subject'] ;
	$comments = $_POST['comments'];
	$my_recaptcha_private_key = '6LcpwwsAAAAAAD8LHzv_P6V4Vi9jsQSlox5gHaZn' ;
		

	if( empty($fullname) || !filter_var($email, FILTER_VALIDATE_EMAIL) || empty($comments) || empty($subject) || preg_match( "/[\r\n]/", $fullname ) || preg_match( "/[\r\n]/", $email ))
		$erreur = true;
	if(empty($subject))
		$erreur_contact_subject = "red";
	if(empty($fullname))
		$erreur_contact_name = "red";
	if(empty($email) || !filter_var($email, FILTER_VALIDATE_EMAIL))
		$erreur_contact_email = "red";
	if(empty($comments))
		$erreur_contact_descriptions = "red";
	if ( preg_match( "/[\r\n]/", $fullname ) || preg_match( "/[\r\n]/", $email ) ) 
	{
		exit ;
	}
	if (strlen( $my_recaptcha_private_key )) 
	{
		require_once( 'recaptchalib.php' );
		$resp = recaptcha_check_answer ( $my_recaptcha_private_key, $_SERVER['REMOTE_ADDR'], $_POST['recaptcha_challenge_field'], $_POST['recaptcha_response_field'] );
		if (!$resp->is_valid) 
		{
			$erreur = true;
			$erreur_contact_captcha ="red";
		}
	}
	if (function_exists( 'get_magic_quotes_gpc' ) && get_magic_quotes_gpc()) 
	{
		$comments = stripslashes( $comments );
	}

	$headers =
		"From: \"$fullname\" <$email>" . $headersep;

	if($erreur == false)
	{
		mail($mailto, ("[".$categorie."]".$subject), $comments, $headers );
	}
}
if(isset($_GET['sent']))
{
	if($erreur == false)
	{
		echo "<b><font color='green'>" . STR_CONTACT_SENT_CORRECTLY . "</font></b>";
		$fullname = "";
		$email = "";
		$comments ="";
	}
	else
	{
		echo "<b><font color='red'>" . STR_CONTACT_SENT_ERROR . "</font></b>";
	}
}

echo"<div id='stylized' class='myform'>
<form id='form' name='form' method='post' action='?page=contact&sent=1'>
<h1>" . STR_CONTACT_FORM . "</h1>
<p>". STR_CONTACT_INFORMATION ."</p>

<table> <tr><td class='nametop'>
<label><font color=" . $erreur_contact_name . ">" . STR_CONTACT_INFORMATION_NAME . "</font>
<span class='small'>" . STR_CONTACT_INFORMATION_NAME_ADD . "</span>
</label>
</td><td>
<input type='text' name='fullname' id='name' value='". $fullname ."'/>
</td></tr>

<tr><td class='nametop'>
<label><font color=" . $erreur_contact_email . ">" . STR_CONTACT_INFORMATION_EMAIL . "</font>
<span class='small'>". STR_CONTACT_INFORMATION_ADD_EMAIL ."</span>
</label>
</td><td>
<input type='text' name='email' id='email' value='". $email ."'/>
</td></tr></table>
<br/>
<p>" . STR_CONTACT_QUESTION . "</p>

<table><tr><td class='nametop'>
<label>" . STR_CONTACT_QUESTION_CATEGORIE . "
<span class='small'>" . STR_CONTACT_QUESTION_CATEGORIE_CHOOSE . "</span>
</label>
</td><td>
<select name='categorie' id='tssubject'>
	<option value='sells'>" . STR_CONTACT_QUESTION_CATEGORIE_SELL. "</option>
	<option value='support'>" . STR_CONTACT_QUESTION_CATEGORIE_SUPPORT. "</option>
	<option value='baddatabase'>" . STR_CONTACT_QUESTION_CATEGORIE_BADDATABASE. "</option>
</select>
</td></tr>

<tr><td class='nametop'>
<label><font color=" . $erreur_contact_subject . ">" . STR_CONTACT_QUESTION_SUBJECT . "</font>
<span class='small'>". STR_CONTACT_QUESTION_SUBJECT_ADD ."</span>
</label>
</td><td>
<input type='text' name='subject' id='email' value='". $subject ."'/>
</td></tr>

<tr><td class='nametop'>
<label><font color=" . $erreur_contact_descriptions . ">" . STR_CONTACT_QUESTION_DESCRIPTION . "</font>
<span class='small'>" . STR_CONTACT_QUESTION_DESCRIPTION_WRITE . "</span>
</label>
</td><td>
<textarea name='comments' id='tswcomments'>" . $comments . "</textarea>
</td></tr></table>

<p>" . STR_CONTACT_CAPTCHA . "</p>
<table><tr><td class='nametop'>
<label><font color=" . $erreur_contact_captcha . ">" . STR_CONTACT_CAPTCHA . "</font>
<span class='small'>" . STR_CONTACT_CAPTCHA_HUMAN . "</span>
</label>
</td><td width=300><center>
<script type='text/javascript' src='http://api.recaptcha.net/challenge?k=6LcpwwsAAAAAAJtYtV1oLmkGo3HUG3kePLATKuZF'></script>
	<noscript>
	<iframe src='http://api.recaptcha.net/noscript?k=6LcpwwsAAAAAAJtYtV1oLmkGo3HUG3kePLATKuZF' height='300' width='500' frameborder='0' title='CAPTCHA test'></iframe>
	<br />
	<label for='tswcaptcha'>Copy and paste the code provided in above box here:</label><br />
	<textarea name='recaptcha_challenge_field' id='tswcaptcha' rows='3' cols='40'></textarea>
	<input type='hidden' name='recaptcha_response_field' value='manual_challenge' />
	</noscript>
</center></td></tr></table>
<button type='submit'>". STR_CONTACT_SEND . "</button>
<div class='spacer'></div>

</form>
</div>";

?>
