<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<title>LochChat - <g:layoutTitle /></title>
    <link rel="icon" type="image/png" href="${resource(src: "favicon.png")}">
		<asset:stylesheet src="application.css"/>
		<asset:javascript src="application.js"/>
		<g:layoutHead/>
	</head>
	<body>
    <div id="wrap">
      <g:render template="../shared/navigation" />

      <div class="content">
        <g:layoutBody/>
      </div>
    </div>
    <g:render template="../shared/footer" />
	</body>
  <script>
    $(function() {
        <g:if test="${notificationEndpoint}">
          subscribeToNotificationEndpoint("${notificationEndpoint}");
        </g:if>
    });
  </script>
</html>
