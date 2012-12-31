<?php
session_start();

include("Script/function.php");

setLanguage();
$Path = $_SESSION['language1'];
include("Language/lang-$Path.php");


include("Script/header.php");
include("Script/menu.php");

if(isset($_GET['page']))
{
	switch($_GET['page'])
	{
	default:
	case 'main' :
		include("home.php");
	break;
	case 'faq' :
		include("faq.php");
	break;
	case 'features' :
		include("features.php");
	break;
	case 'screenshots' :
		include("screenshots.php");
	break;
	case 'contact' :
		include("contact.php");
	break;

	}
}
else
{
	include("home.php");
}

include("Script/footer.php");
?>