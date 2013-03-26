// class Accou ntBox

function AccountBox() {
	this.name = "[AccountBox]";
	this.view = null;
}

AccountBox.prototype.setParent = function(parent) {
	// alert("setParent = " + parent);
	// var doc = parent.ownerDocument;
	var view = this.getView();
	parent.appendChild(view);
};

AccountBox.prototype.getView = function() {
	var view = this.view;
	if (view == null) {
		var doc = document;
		view = doc.createElement("div");
		var text = doc.createTextNode("hello world");
		view.appendChild(text);

		this.view = view;
	}
	return view;
};

AccountBox.prototype.load = function() {

	var theBox = this;

	var xhttp = new XMLHttpRequest();
	xhttp.open("GET", "Account", true);
	xhttp.onreadystatechange = function() {
		var status = xhttp.status;
		var ready = xhttp.readyState;
		var json = xhttp.responseText;
		if (status == 200 && ready == 4) {
			theBox.getView().innerHTML = json;
		}
	};
	xhttp.send(null);
};
