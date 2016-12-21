<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="section">
    <div class="section-title">${section.title}</div>
    <c:forEach items="${section.blocks}" var="block">

        <c:if test="${block.isPoint()}">
            <c:set var="point" value="${block}" scope="request" />
            <jsp:include page="point.jsp" />
        </c:if>
        <c:if test="${block.isSection()}">
            <c:set var="section" value="${block}" scope="request" />
            <jsp:include page="subsection.jsp" />
        </c:if>
    </c:forEach> 
</div>    