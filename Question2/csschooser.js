if(window.innerWidth >= 1600) //Should be 1680, but my monitor isn't good enough.
	document.write('<link rel="stylesheet" type="text/css" href="1680x1050.css">');
else if(window.innerWidth >= 440)
	document.write('<link rel="stylesheet" type="text/css" href="1024x768.css">');
else
	document.write('<link rel="stylesheet" type="text/css" href="small.css">');
	
window.onresize = function() {
	window.location.reload();
};

