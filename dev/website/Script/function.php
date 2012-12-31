<?php
function setLanguage()
{
	if(isset( $_POST['Language1'] ))
	{
		$_SESSION['language1'] = $_POST['Language1'];
		$timestamp_expire = time() + 365*24*3600;
		setcookie('language1', $_POST['Language1'], $timestamp_expire);
	}
	if(!isset( $_SESSION['language1'] ))
	{
		if(isset( $_COOKIE['language1'] ))
		{
			$_SESSION['language1'] = $_COOKIE['language1'];
		}
		else
		{
			$lang = $_SERVER['HTTP_ACCEPT_LANGUAGE'];
    			$lang = substr($lang, 0, 2); 
			if($lang != 'fr' || $lang != 'en')
				$lang = 'fr';
			$timestamp_expire = time() + 365*24*3600;
			setcookie('language1', $lang, $timestamp_expire);
			$_SESSION['language1'] = $lang;
		}
	}
}
function choixParDefaut($language)
{
	$par_defaut = '';

	if (isset($_SESSION['language1']))
	{
		if ($_SESSION['language1'] == $language)
		{
			$par_defaut=" selected='selected'";
		}
	}

	return $par_defaut;
}

function printLanguageSelection()
{
	echo "<form method='post' action='#'>\n";
	echo "<select name='Language1' onchange='this.form.submit()' STYLE='width:  90px'>";
	$handle = @fopen("./Language/Language.txt", "r");
	if ($handle)
	{
		while (!feof($handle)) 
		{
			$buffer = fgets($handle, 4096);
			echo "<option value='";
			echo $newbuf = strtok($buffer, ';');
			echo "'";
			echo choixParDefaut($newbuf);
			echo ">";
			echo strtok(';');
			echo "</option>";
		}
		fclose($handle);
	}
	echo "</select>";
	echo "</form>";
}

function doLink($name, $link)
{
	echo "<a href='?page=$link'>$name</a>";
}

function listImageThumb()
{
	$dir = "./Gallery/Thumbs/";

	if (is_dir($dir)) 
	{
    		if ($dh = opendir($dir)) 
		{
			$i=0;
        		while (($file = readdir($dh)) !== false) 
				{
					if(is_file($dir . $file))
					{
            				echo "<li><a href='?page=screenshots&ss=$i'><img src='$dir$file' alt='The thumb'/><span>image description</span></a></li>";
					}
					$i++;
        		}
        	closedir($dh);
    		}	
	}
}
function PrintImage($picnum)
{
	$dir = "./Gallery/Thumbs/";
	echo "<table border='1'><tr><td colspan='3'>Header</td></tr>";
	echo "<tr><td></td> <td>";
	if (is_dir($dir)) 
	{
    	if ($dh = opendir($dir)) 
		{
			$i=0;
        		while (($file = readdir($dh)) !== false) 
				{
					if(is_file($dir . $file) && $i == $picnum)
					{
            				echo "<img src='$dir$file' alt='The thumb'/>";
					}
					$i++;
        		}
        	closedir($dh);
    		}	
	}
	echo "</td> <td></td></tr>";
	echo "<tr><td colspan='3'>Footer</td></tr>";
	echo "<tr><td>";
	if($picnum > 3)
	{
		$picnum--;
		echo "<a href='?page=screenshots&ss=$picnum'><</a>";
		$picnum++;
	}
	echo "</td><td align='center'><a href='?page=screenshots'>";
	echo STR_GALLERY_HOME;
	echo "</a></td><td>";
	if($picnum+1 < $i)
	{
		$picnum++;
		echo "<a href='?page=screenshots&ss=$picnum'>></a>";
	}
	echo "</td></tr></table>";
}
?>