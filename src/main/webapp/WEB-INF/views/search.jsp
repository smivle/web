<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/x-mathjax-config">
            MathJax.Hub.Config({tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}});
        </script>
        <script type="text/javascript" async
          src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS_CHTML">
        </script>
        <title>STAGIRS</title>
        <script src="static/jquery/jquery.js"></script>
        <link rel="stylesheet" href="static/awesome/css/font-awesome.min.css">
        <link rel="stylesheet" href="static/bootstrap-select.min.css">
        <script src="static/bootstrap-select.min.js"></script>
        <link rel="stylesheet" href="static/bootstrap.min.css">
        <script src="static/jquery.min.js"></script>
        <script src="static/bootstrap.min.js"></script>
        <script src="static/overlay/loadingoverlay.js"></script>
        <script src="static/angular.min.js"></script>
        <link href="static/nouislider/nouislider.min.css" rel="stylesheet">
        <script src="static/nouislider/nouislider.min.js"></script>
        <style>
            body{
                background-color: #f1f1f1
            }
            .body{
                width:1024px;
                margin: 20px auto;
                font-size: 20px;
                font-family: serif;
                border-radius: 5px;
                background-color: #fff;
                padding: 20px
            }
            .meta{
                width:1024px;
                margin: 20px auto;
                text-align: center; 
            }
            p {
                text-indent: 40px; 
                text-align: left;
            }
            .title{
                font-size: 22px;
                font-family: serif;
                font-weight: bold;
            }
            .thanks{
                font-size: 13px;
                font-style: italic;
                font-family: serif;
                margin: 20px;
            }
            .author{
                font-size: 20px;
                font-family: serif;
                font-weight: bold;
                margin: 20px;
            }
            .info{
                font-size: 15px;
                font-style: italic;
                font-family: serif;
                margin: 20px;
            }
            .keyword{
                text-decoration: underline;
                font-weight: bold;    
            }
            i{
                text-indent: 0px;
            }
        </style>
        <script>
            $(document).ready(function(){
                $(".query-button").click(function(){
                    window.location = "${type}?query=" + $(".query").val();
                });
                $(".query").keypress(function (e) {
                    if (e.which == 13) {
                      $("#query-button").click();
                      return false;    //<---- Add this line
                    }
                });
            });
            
        </script>    
    </head>
    <body style="margin: 0 auto;    background-color: #f1f1f1;">
        <div class="meta">
            <a href="searchDocs" style="color: #5bc0de; text-decoration: none; font-size: 30px; font-weight: bold; font-family: sans-serif;float: left">
                STAGIRS
            </a>
            <div style="text-align: center;margin-top:20px;display: inline-block">
                <input class="query form-control" type="text" value="${query}" style="width:400px;margin-right: 5px;display: inline-block;" ng-model="text">
                <button class="query-button btn btn-info" type="button"><i class="fa fa-search" data-original-title="" title=""></i></button>
            </div>
            <c:if test="${not empty items}">
            <div style="font-size: 15px; font-weight: bold; font-family: sans-serif;display: inline-block;margin-left:20px">
                Результаты: ${items.size()}
            </div>   
            </c:if>
            <c:if test="${type eq 'searchDocs'}">
                <a href="searchTags?query=${query}" style="margin-left: 20px">Искать теги...</a>
            </c:if>    
            <c:if test="${type eq 'searchTags'}">
                <a href="searchDocs?query=${query}" style="margin-left: 20px">Искать документы...</a>
            </c:if>      
        </div>
        
        
            <c:forEach items="${items}" var="item">
                <div class="body">
                <c:if test="${item.isSentence()}">
                    <div class="sentence-block">
                        <div class="title"><a href="doc?docId=${item.getDocument().id}#sentence${item.getSentence().number}">${item.getDocument().title}</a></div>
                        <span class="sentence"  number="${item.getSentence().number}" >
                            <c:forEach items="${item.getSentence().parts}" var="part">
                                <c:if test="${empty part.className}">${part.text.replaceAll(keywords, "<span class='keyword'>$1</span>")}</c:if>
                                <c:if test="${not empty part.className}"><span class="${part.className}" ng-non-bindable>${part.text}</span></c:if>
                            </c:forEach>
                        </span>
                    </div>
                </c:if>
                <c:if test="${item.isDocument()}">
                    <div class="doc-block">
                        <div class="title"><a href="doc?docId=${item.getDocument().id}">${item.getDocument().title}</a></div>
                        <div class="thanks">${item.getDocument().thanks}</div>
                        <div class="author" style="display: inline-block">${item.getDocument().author}</div>
                        <div class="info" style="display: inline-block">${item.getDocument().classifier}; ${item.getDocument().output}</div>
                    </div>
                </c:if>
                <c:if test="${item.isSection()}">
                    <div class="section-block">
                        <div class="title"><a href="doc?docId=${item.getDocument().id}">${item.getDocument().title}</a></div>
                        <div class="thanks">${item.getDocument().thanks}</div>
                        <div class="author" style="display: inline-block">${item.getDocument().author}</div>
                        <div class="info" style="display: inline-block">${item.getDocument().classifier}; ${item.getDocument().output}</div>
                        <div class="section-title">Раздел: ${item.getSection().title}</div>
                    </div>
                </c:if>
                </div> 
            </c:forEach> 
           
    </body>
</html>
