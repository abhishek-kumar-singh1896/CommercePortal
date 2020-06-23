<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Global site tag (gtag.js) - Google Analytics -->
<c:if test="${not empty gtagId}">
	<!-- Global site tag (gtag.js) - Google Analytics -->
	<script async
		src="https://www.googletagmanager.com/gtag/js?id=${gtagId}"></script>
	<script>
		window.dataLayer = window.dataLayer || [];
		function gtag() {
			dataLayer.push(arguments);
		}
		gtag('js', new Date());

		gtag('config', '${gtagId}');
	</script>
</c:if>
<!-- End Google Tag Manager -->