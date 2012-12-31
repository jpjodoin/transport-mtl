<div id="wrap">
<div id="header">
<div id="banner">
<img src="Images/banner.png"/>
</div>
<div id="language">
<div id="menu"><?php

echo "&nbsp";
echo STR_MENU_SEPARATOR;
doLink(STR_MENU_HOME, "home");
echo STR_MENU_SEPARATOR;
doLink(STR_MENU_SCREENSHOTS, "screenshots");
echo STR_MENU_SEPARATOR;
doLink(STR_MENU_FAQ, "faq");
echo STR_MENU_SEPARATOR;
doLink(STR_MENU_CONTACT, "contact");

?></div><!-- menu -->
<?php
printLanguageSelection();
?></div><!-- language -->
</div><!-- header -->

<div id="main">
<div id="info">

