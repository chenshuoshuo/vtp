document.write('<script src="'+the_host+'/vtpConfig"></script>');

getParameter = function(e) {
	var d = window.location.search;
	var a = e.length;
	var c = d.indexOf(e);
	if (c == -1) {
		return ""
	}
	c += a + 1;
	var b = d.indexOf("&", c);
	if (b == -1) {
		return d.substring(c)
	}
	return d.substring(c, b)
};
