<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="static/font-awesome/css/font-awesome.min.css">
        <title>Smivle Search System</title>
        <style>
            input.query{
                font-size: 20px;
                padding: 5px;
                max-width: 600px;
                width: 90%;
                border-color: #88a;
            }
            input.query:hover{
                border-color: #335;
            }    
            .query-button{
                color: #335;
                font-size: 30px;
                cursor: pointer;
            }
            .query-button:hover{
                color: #668;
            }    
            .doc-block{
                display: inline-block;
                border-radius: 5px;
                background-color: #fff;
                padding: 10px;
                margin: 10px;
                vertical-align: top;
            }
            .doc-block-text{
                width: 400px;
            }
            .result{
                text-align: center;
                margin: 20px;
            }
            .doc-link{
                text-decoration: none;
                color: #335;
            }
        </style>  
        <script src="static/js/jquery-2.0.3.min.js"></script>
        <script>
            $(document).ready(function(){
                $(".query-button").click(function(){
                    window.location = "search?query=" + $(".query").val();
                });
            });
        </script>    
    </head>
    <body style="margin: 0 auto;    background-color: #f1f1f1;">
        <div style="height: 40px; background-color: #335;">
            <a href="search">
                <img style="margin-left: 20px" height="40px" src="static/smivle.png">
            </a>
            <span style="float: right; line-height: 40px;font-size: 30px; margin-right: 20px">
                <a href="search" style="color: #fff;">
                    <i class="fa fa-bars" aria-hidden="true"></i>
                </a>
            </span>    
        </div>
        <div style="text-align: center;margin-top:20px;color:#335;font-size: 30px">
            Кластер из ${cluster.count} записей
        </div>
        <div class="result">
            <c:forEach items="${docs}" var="doc">
                <div class="doc-block">
                    <div class="doc-block-text">
                        ${doc.text}
                    </div>
                    <div>
                        <a class="doc-link" href="doc?id=${doc.docHash}">Перейти к документу ...</a>
                    </div>
                </div>
            </c:forEach>    
        </div>    
    </body>
</html>
