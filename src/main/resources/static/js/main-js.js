function disableDates(form) {
	
	var elements = form.elements;

	for (var i = 0, element; element = elements[i++];) {
	    if (element.type === "date" && element.value === "")
	        element.disabled = true;
	}
	
}