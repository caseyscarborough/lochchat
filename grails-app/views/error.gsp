<!DOCTYPE html>
<html>
	<head>
		<title><g:if env="development">Grails Runtime Exception</g:if><g:else>Error</g:else></title>
		<meta name="layout" content="main">
		<g:if env="development"><asset:stylesheet src="errors.css"/></g:if>
	</head>
	<body>
    <div class="container">
      <div class="row">
        <div class="col-md-10 col-md-offset-1">
          <g:if env="development">
            <g:renderException exception="${exception}" />
          </g:if>
          <g:else>
            <div class="alert alert-danger">
              An error has occurred. Please try your request again later.
            </div>
          </g:else>
        </div>
      </div>
    </div>
	</body>
</html>
