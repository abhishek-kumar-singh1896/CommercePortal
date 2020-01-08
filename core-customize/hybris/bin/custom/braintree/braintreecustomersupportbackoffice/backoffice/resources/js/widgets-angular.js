AngularCNG.init = function(widgetName, controller, services) {
	return new AngularCNG( "widgets." + widgetName ).controller(controller, services);
};