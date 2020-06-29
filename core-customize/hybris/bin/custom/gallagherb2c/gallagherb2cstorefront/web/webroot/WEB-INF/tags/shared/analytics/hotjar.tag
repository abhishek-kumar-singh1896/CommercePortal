<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Global site tag (gtag.js) - Google Analytics -->
<c:if test="${not empty hotjarId}">
<!-- Hotjar Tracking Code for https://products.security.gallagher.com/ -->
<script>
    (function(h,o,t,j,a,r){
        h.hj=h.hj||function(){(h.hj.q=h.hj.q||[]).push(arguments)};
        h._hjSettings={hjid:${hotjarId},hjsv:6};
        a=o.getElementsByTagName('head')[0];
        r=o.createElement('script');r.async=1;
        r.src=t+h._hjSettings.hjid+j+h._hjSettings.hjsv;
        a.appendChild(r);
    })(window,document,'https://static.hotjar.com/c/hotjar-','.js?sv=');
</script>
</c:if>
<!-- End Google Tag Manager -->