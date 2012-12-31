<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
<title><?php echo STR_HEADER_TITLE;?></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta http-equiv="Window-target" content="_top" />
<meta http-equiv="Content-Language" content="fr, en" />
<meta name="MSSmartTagsPreventParsing" content="TRUE" />
<!--TransportMontréal vous permet d'accéder facilement dans un mode hors-ligne à l'horaire des sociétés de transport montréalaises à partir d'un téléphone Android.-->
<meta name="Description" content="TransportMontréal vous permet d'accéder facilement dans un mode hors-ligne à l'horaire des sociétés de transport montréalaises à partir d'un téléphone Android." />
<meta name="Abstract" content="TransportMontréal vous permet d'accéder facilement dans un mode hors-ligne à l'horaire des sociétés de transport montréalaises à partir d'un téléphone Android." />
<meta name="Keywords" content="rhatec,Android,Transport,Montreal,Montréal,Laval,Longueuil,RTL,STL,AMT,STM,Metro,Métro,Train,Trains,Schedule,Schedules,Horaire,Horraire,Horaires,Horraires,Mobile,free,telephone,application,cellulaire" />
<meta name="Robots" content="index, follow, noarchive, noimageclick" />
<meta name="Revisit-After" content="10 days" />

<meta name="google-site-verification" content="jZv3drvBXv1uzrJA-N08FpO2yZebuVdKOMksIPgBmUk" />
<meta name="google-site-verification" content="ad9s9TFM-sGanWX695uWwG2zjmXdXM5kabll3eyzl4k" />
<link rel="stylesheet" type="text/css" href="theme.css" />
<script type='text/javascript' src='jquery.js'> </script>
<script type="text/javascript" src="jquery.innerfade.js"></script>
<script type="text/javascript">
	   $(document).ready(
	   			function(){
					$('#fade').show();
					$('ul#portfolio').innerfade({
						speed: 1000,
						timeout: 5000,
						type: 'random',
						containerheight: '300px'
					});
				
			});
  	</script>
<?php
if(isset($_GET['page']))
{
if( $_GET['page'] == 'faq' || $_GET['page'] == 'features' || $_GET['page'] == 'home')
echo "
<script type='text/javascript'>
$(document).ready(function() 
{									
	$(\"a[name^='faq-']\").each(function() 
	{
		$(\"#\" + this.name).hide();
		$(this).click(function() 
		{
			if( $(\"#\" + this.name).is(':hidden') ) 
			{
				$(\"#\" + this.name).fadeIn('fast');
			} else {
				$(\"#\" + this.name).fadeOut('fast');
			}			
			return false;
		});
	});
});
</script>";
if( $_GET['page'] == 'screenshots')
{
	echo "<link rel='stylesheet' type='text/css' href='gallery.css' />
	<script type='text/javascript' src='jquery.galleriffic.js'></script>
	<script type='text/javascript' src='jquery.opacityrollover.js'></script>
	<script type='text/javascript'>
			document.write('<style>.noscript { display: none; }</style>');
		</script>
";
	
	echo "
	<script type='text/javascript'>
			jQuery(document).ready(function($) {
				// We only want these styles applied when javascript is enabled
				$('div.navigation').css({'width' : '205px', 'float' : 'left', 'text-align' : 'center'});
				$('div.content').css('display', 'block');

				// Initially set opacity on thumbs and add
				// additional styling for hover effect on thumbs
				var onMouseOutOpacity = 0.67;
				$('#thumbs ul.thumbs li').opacityrollover({
					mouseOutOpacity:   onMouseOutOpacity,
					mouseOverOpacity:  1.0,
					fadeSpeed:         'fast',
					exemptionSelector: '.selected'
				});
				
				// Initialize Advanced Galleriffic Gallery
				var gallery = $('#thumbs').galleriffic({
					delay:                     2500,
					numThumbs:                 3,
					preloadAhead:              -1,
					enableTopPager:            false,
					enableBottomPager:         true,
					maxPagesToShow:            5,
					imageContainerSel:         '#slideshow',
					controlsContainerSel:      '#controls',
					captionContainerSel:       '#caption',
					loadingContainerSel:       '#loading',
					renderSSControls:          false,
					renderNavControls:         false,
					playLinkText:              'Play Slideshow',
					pauseLinkText:             'Pause Slideshow',
					prevLinkText:              '&lsaquo; Previous Photo',
					nextLinkText:              'Next Photo &rsaquo;',
					nextPageLinkText:          '&rsaquo;',
					prevPageLinkText:          '&lsaquo;',
					enableHistory:             false,
					autoStart:                 false,
					syncTransitions:           false,
					defaultTransitionDuration: 350,
					onSlideChange:             undefined, 
					onPageTransitionOut:       function(callback) {
						this.fadeTo('fast', 0.0, callback);
					},
					onPageTransitionIn:        function() {
						this.fadeTo('fast', 1.0);
					}
				});
			});
		</script>";
}}
?>

</head>
<body>
