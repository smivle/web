<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<p number="${point.number}">
    <c:forEach items="${point.sentences}" var="sentence">
        <c:if test="${sentenceIds == null or sentenceIds.contains(sentence.number)}">
            <span class="sentence"  number="${sentence.number}" semantic="${sentence.semantic}" ng-show="${sentence.semantic} * 100 / maxSemantic >= minShow && ${sentence.semantic} * 100 / maxSemantic <= maxShow">
                <a href="doc?docId=${doc.id}#sentence${sentence.number}"><i class="fa fa-expand" aria-hidden="true"></i></a>
                <a name="sentence${sentence.number}"></a>
                <c:forEach items="${sentence.parts}" var="part">
                    <c:if test="${empty part.className}">${part.text.replaceAll(keywords, "<span class='keyword'>$1</span>")}</c:if>
                    <c:if test="${not empty part.className}"><span class="${part.className}" ng-non-bindable>${part.text}</span></c:if>
                </c:forEach>
            </span>
        </c:if>
    </c:forEach> 
<p>